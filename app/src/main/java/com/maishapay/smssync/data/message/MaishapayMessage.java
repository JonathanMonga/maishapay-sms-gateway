/*
 * Copyright (c) 2010 - 2015 Ushahidi Inc
 * All rights reserved
 * Contact: team@ushahidi.com
 * Website: http://www.ushahidi.com
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Ushahidi developers at team@ushahidi.com.
 */

package com.maishapay.smssync.data.message;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.entity.MaishapayResponse;
import com.maishapay.smssync.data.entity.Message;
import com.maishapay.smssync.data.entity.MessagesUUIDSResponse;
import com.maishapay.smssync.data.entity.QueuedMessages;
import com.maishapay.smssync.data.entity.SmssyncResponse;
import com.maishapay.smssync.data.entity.SoldeEpargneResponse;
import com.maishapay.smssync.data.entity.SoldeResponse;
import com.maishapay.smssync.data.entity.SyncUrl;
import com.maishapay.smssync.data.net.MaishapayHttpClient;
import com.maishapay.smssync.data.net.MessageHttpClient;
import com.maishapay.smssync.data.repository.datasource.filter.FilterDataSource;
import com.maishapay.smssync.data.repository.datasource.message.MessageDataSource;
import com.maishapay.smssync.data.repository.datasource.webservice.WebServiceDataSource;
import com.maishapay.smssync.data.util.Logger;
import com.maishapay.smssync.data.util.Utility;
import com.maishapay.smssync.smslib.sms.ProcessSms;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Posts {@link Message} to a configured web service
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Singleton
public class MaishapayMessage extends ProcessMessage {
    private static final List<String> keywords = new ArrayList<>(Arrays.asList("TXN Id", "Trans. ID", "Transaction ID", "PP", StatusSMS.SOLDE_CODE, StatusSMS.SOLDE_EPARGNE_CODE));

    private MaishapayHttpClient maishapayHttpClient;
    private ProcessMessageResult mProcessMessageResult;
    private String mErrorMessage;

    @Inject
    public MaishapayMessage(Context context,
                            PrefsFactory prefsFactory,
                            MaishapayHttpClient maishapayHttpClient,
                            MessageDataSource messageDataSource,
                            WebServiceDataSource webServiceDataSource,
                            FilterDataSource filterDataSource,
                            ProcessSms processSms,
                            FileManager fileManager,
                            ProcessMessageResult processMessageResult) {
        super(context, prefsFactory, messageDataSource, webServiceDataSource, filterDataSource, processSms, fileManager);

        this.maishapayHttpClient = maishapayHttpClient;
        mProcessMessageResult = processMessageResult;
    }

    /**
     * Processes the incoming SMS to figure out how to exactly route the message. If it fails to be
     * synced online, cache it and queue it up for the scheduler to process it.
     *
     * @param message The sms to be routed
     * @return boolean
     */
    public boolean routeSms(Message message) {
        Logger.log(TAG, "routeSms uuid: " + message.toString());

        // Send auto response from phone not server
        if (mPrefsFactory.enableReply().get()) {
            // send auto response as SMS to user's phone
            logActivities(R.string.auto_response_sent);
            Message msg = new Message();
            msg.setMessageBody(mPrefsFactory.reply().get());
            msg.setMessageFrom(message.getMessageFrom());
            msg.setMessageType(message.getMessageType());
            mProcessSms.sendSms(map(msg), false);
        }

        if (isConnected()) {
            if (postMessage(message)) {
                postToSentBox(message);
                deleteFromSmsInbox(message);
            } else {
                savePendingMessage(message);
            }

            return true;
        }

        // There is no internet save message
        savePendingMessage(message);
        return false;
    }

    /**
     * Sync pending messages to the configured sync URL.
     */
    public boolean syncPendingMessages() {
        Logger.log(TAG, "syncPendingMessages: push pending messages to the Sync URL");

        final List<Message> messages = mMessageDataSource.syncFetchPending();
        if (messages != null && messages.size() > 0) {
            return postMessage(messages);
        }

        return false;
    }

    public boolean postMessage(List<Message> messages) {
        Logger.log(TAG, "postMessages");
        for (Message messg : messages) {
            if (postMessage(messg)) {
                postToSentBox(messg);
            }
        }
        return true;
    }


    public boolean routePendingMessage(Message message) {
        Logger.log(TAG, "postMessages");
        if (postMessage(message)) {
            postToSentBox(message);
        }

        return true;
    }

    private void sendSMSWithMessageResultsAPIEnabled(SyncUrl syncUrl, List<Message> msgs) {
        QueuedMessages messagesUUIDs = new QueuedMessages();
        for (Message msg : msgs) {
            msg.setMessageType(Message.Type.TASK);
            messagesUUIDs.getQueuedMessages().add(msg.getMessageUuid());
        }

        MessagesUUIDSResponse response = mProcessMessageResult.sendQueuedMessagesPOSTRequest(syncUrl, messagesUUIDs);
        if (null != response && response.isSuccess() && response.hasUUIDs()) {
            for (Message msg : msgs) {
                msg.setMessageType(Message.Type.TASK);
                if (response.getUuids().contains(msg.getMessageUuid())) {
                    sendTaskSms(msg);
                    mFileManager.append(mContext.getString(R.string.processed_task,
                            msg.getMessageBody()));
                }
            }
        }
    }

    private void sendSMSWithMessageResultsAPIDisabled(List<Message> msgs) {
        for (Message msg : msgs) {
            msg.setMessageType(Message.Type.TASK);
            sendTaskSms(msg);
        }
    }

    /**
     * Send the response received from the server as SMS
     *
     * @param response The JSON string response from the server.
     */
    private void smsServerResponse(Message message, MaishapayResponse response) {
        Logger.log(TAG, "performResponseFromServer(): " + " response:" + response);
        if (!mPrefsFactory.enableReplyFrmServer().get()) {
            return;
        }

        if ((response != null) && (response.getResultat() == 1) && (response.getMessage() != null) && (response.getMessage().length() > 0)) {
            Message localMessage = new Message();
            localMessage.setMessageBody(response.getMessage());
            localMessage.setMessageFrom(message.getMessageFrom());
            sendTaskSms(localMessage);
        }
    }

    /**
     * Send the response received from the server as SMS
     *
     * @param response The JSON string response from the server.
     */
    private void smsSoldeServerResponse(Message message, SoldeResponse response) {
        Logger.log(TAG, "performResponseFromServer(): " + " response:" + response);
        if (!mPrefsFactory.enableReplyFrmServer().get()) {
            return;
        }

        if (response != null && response.getResultat() == 1) {
            Message localMessage = new Message();

            String currency = message.getMessageBody().split(" ")[3];
            String messageResponse;
            if (currency.equalsIgnoreCase("CDF"))
                messageResponse = String.format("Merci d'avoir choisi Maishapay. Votre solde actuel est %s FC", response.getFrancCongolais());
            else
                messageResponse = String.format("Merci d'avoir choisi Maishapay. Votre solde actuel est %s USD", response.getFrancCongolais());

            localMessage.setMessageBody(messageResponse);
            localMessage.setMessageFrom(message.getMessageFrom());
            sendTaskSms(localMessage);
        }
    }

    /**
     * Send the response received from the server as SMS
     *
     * @param response The JSON string response from the server.
     */
    private void smsSoldeEpargneServerResponse(Message message, SoldeEpargneResponse response) {
        Logger.log(TAG, "performResponseFromServer(): " + " response:" + response);
        if (!mPrefsFactory.enableReplyFrmServer().get()) {
            return;
        }

        if (response != null && response.getResultat() == 1) {
            Message localMessage = new Message();

            String currency = message.getMessageBody().split(" ")[3];
            String messageResponse;
            if (currency.equalsIgnoreCase("CDF"))
                messageResponse = String.format("Merci d'avoir choisi Maishapay. Votre solde actuel est %s FC", response.getFrancCongolais());
            else
                messageResponse = String.format("Merci d'avoir choisi Maishapay. Votre solde actuel est %s USD", response.getFrancCongolais());

            localMessage.setMessageBody(messageResponse);
            localMessage.setMessageFrom(message.getMessageFrom());
            sendTaskSms(localMessage);
        }
    }

    private boolean postMessage(Message message) {
        // Process filter text (keyword or RegEx)
        if (filterByKeywords(message.getMessageBody(), keywords) || filterByRegex(message.getMessageBody(), keywords)) {
            return postToWebService(message);
        }

        return postToWebService(message);
    }

    private boolean postToWebService(Message message) {
        boolean posted = false;

        if (message.getMessageType().equals(Message.Type.PENDING)) {
            Logger.log(TAG, "Process message with keyword filtering enabled " + message);

            if (message.getMessageBody().toLowerCase().startsWith(StatusSMS.SOLDE_CODE)) {
                String[] splits = message.getMessageBody().split(" ");
                if (splits.length == 4) {
                    posted = maishapayHttpClient.postSmsToSoldeWebService(splits[2].toLowerCase());
                    // Process server side response so they are sent as SMS
                    smsSoldeServerResponse(message, maishapayHttpClient.getSoldeServerSuccessResp());
                } else {
                    Message msg = new Message();
                    msg.setMessageBody("Une erreur se produit lors de la demande de votre solde.");
                    msg.setMessageFrom(message.getMessageFrom());
                    msg.setMessageType(message.getMessageType());
                    mProcessSms.sendSms(map(msg), false);
                }
            } else if (message.getMessageBody().toLowerCase().startsWith(StatusSMS.SOLDE_EPARGNE_CODE)) {
                String[] splits = message.getMessageBody().split(" ");
                if (splits.length == 4) {
                    posted = maishapayHttpClient.postSmsToSoldeWebService(splits[2].toLowerCase());
                    // Process server side response so they are sent as SMS
                    smsSoldeEpargneServerResponse(message, maishapayHttpClient.getSoldeEpargneServerSuccessResp());
                } else {
                    Message msg = new Message();
                    msg.setMessageBody("Une erreur se produit, lors de la démande de votre solde.");
                    msg.setMessageFrom(message.getMessageFrom());
                    msg.setMessageType(message.getMessageType());
                    mProcessSms.sendSms(map(msg), false);
                }
            } else {
                posted = maishapayHttpClient.postSmsToWebService(
                        "",
                        "", "",
                        "",
                        "",
                        "",
                        "");
                // Process server side response so they are sent as SMS
                smsServerResponse(message, maishapayHttpClient.getServerSuccessResp());
            }
        } else {
            posted = sendTaskSms(message);
        }
        if (!posted) {
            processRetries(message);
        }
        return posted;
    }

    public void performTask() {
        if ((!mPrefsFactory.serviceEnabled().get()) || (!mPrefsFactory.enableTaskCheck().get())) {
            // Don't continue
            return;
        }
        MessageHttpClient messageHttpClient = new MessageHttpClient(mContext, mFileManager);
        Logger.log(TAG, "performTask(): perform a task");
        logActivities(R.string.perform_task);
        List<SyncUrl> syncUrls = mWebServiceDataSource.get(SyncUrl.Status.ENABLED);
        for (SyncUrl syncUrl : syncUrls) {
            StringBuilder uriBuilder = new StringBuilder(syncUrl.getUrl());
            final String urlSecret = syncUrl.getSecret();
            uriBuilder.append("?task=send");

            if (!TextUtils.isEmpty(urlSecret)) {
                String urlSecretEncoded = urlSecret;
                uriBuilder.append("&secret=");
                try {
                    urlSecretEncoded = URLEncoder.encode(urlSecret, "UTF-8");
                } catch (java.io.UnsupportedEncodingException e) {
                    Logger.log(TAG, e.getMessage());
                }
                uriBuilder.append(urlSecretEncoded);
            }

            messageHttpClient.setUrl(uriBuilder.toString());
            SmssyncResponse smssyncResponses = null;
            Gson gson = null;
            try {
                messageHttpClient.execute();
                gson = new Gson();
                final String response = messageHttpClient.getResponse().body().string();
                mFileManager.append("HTTP Client Response: " + response);
                smssyncResponses = gson.fromJson(response, SmssyncResponse.class);
            } catch (Exception e) {
                Logger.log(TAG, "Task checking crashed " + e.getMessage() + " response: "
                        + messageHttpClient.getResponse());
                try {
                    mFileManager.append(
                            "Task crashed: " + e.getMessage() + " response: " + messageHttpClient
                                    .getResponse().body().string());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (smssyncResponses != null) {
                Logger.log(TAG, "TaskCheckResponse: " + smssyncResponses.toString());
                mFileManager.append("TaskCheckResponse: " + smssyncResponses.toString());

                if (smssyncResponses.getPayload() != null) {
                    String task = smssyncResponses.getPayload().getTask();
                    Logger.log(TAG, "Task " + task);
                    boolean secretOk = TextUtils.isEmpty(urlSecret) ||
                            urlSecret.equals(smssyncResponses.getPayload().getSecret());
                    if ((secretOk) && (task != null) && (task.equals("send"))) {
                        if (mPrefsFactory.messageResultsAPIEnable().get()) {
                            sendSMSWithMessageResultsAPIEnabled(syncUrl,
                                    smssyncResponses.getPayload().getMessages());
                        } else {
                            //backwards compatibility
                            sendSMSWithMessageResultsAPIDisabled(
                                    smssyncResponses.getPayload().getMessages());
                        }

                    } else {
                        Logger.log(TAG, mContext.getString(R.string.no_task));
                        logActivities(R.string.no_task);
                        mErrorMessage = mContext.getString(R.string.no_task);
                    }

                } else { // 'payload' data may not be present in JSON
                    Logger.log(TAG, mContext.getString(R.string.no_task));
                    logActivities(R.string.no_task);
                    mErrorMessage = mContext.getString(R.string.no_task);
                }
            }

            mFileManager.append(
                    mContext.getString(R.string.finish_task_check) + " " + mErrorMessage + " for "
                            + syncUrl.getUrl());
        }
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public boolean isConnected() {
        return Utility.isConnected(mContext);
    }

    public String getPhoneNumber() {
        return Utility.getPhoneNumber(mContext, mPrefsFactory);
    }

    protected interface StatusSMS {
        String SOLDE_CODE = "maishapay s1";
        String SOLDE_EPARGNE_CODE = "maishapay s2";
    }
}
