/*
 * Copyright (c) 2018 - 2019 Maishapay
 * All rights reserved
 * Contact: contact@maishapay.online
 * Website: http://www.maishapay.online
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Maishapay developers at contact@maishapay.online.
 */

package com.maishapay.smssync.data.message;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.entity.MaishapayResponse;
import com.maishapay.smssync.data.entity.Message;
import com.maishapay.smssync.data.entity.MessagesUUIDSResponse;
import com.maishapay.smssync.data.entity.QueuedMessages;
import com.maishapay.smssync.data.entity.SmssyncResponse;
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
 * Posts {@link MaishapayMessage} to a configured web service
 *
 * @author Maishapay Team <online@maishapay.online>
 */
@Singleton
public class MaishapayMessage extends ProcessMessage {
    private static final List<String> keywords = new ArrayList<>(Arrays.asList(
            "TXN Id",
            "Trans. ID",
            "Transaction ID",
            "PP",
            "CI",
            MaishapaySMSCode.SOLDE_COMPTE_COURANT_CODE,
            MaishapaySMSCode.SOLDE_COMPTE_EPARGNE_CODE,
            MaishapaySMSCode.RETRAIT_CODE,
            MaishapaySMSCode.TRANS_CODE_CI,
            MaishapaySMSCode.TRANS_CODE_PP,
            MaishapaySMSCode.TRANSACTION_CODE_CI,
            MaishapaySMSCode.TRANSACTION_CODE_PP,
            MaishapaySMSCode.CONFIRM_DEPOT_CODE));

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
                //deleteFromSmsInbox(message);
            }

            return true;
        }

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
    private void smsMaishapayServerResponse(Message message, MaishapayResponse response) {
        if (response != null) {
            if(response.getResultat() == 1) {
                Log.e("Maishapay", response.getMessage());
            } else {
                Log.e("Maishapay", response.getMessage());
            }
        }
    }

    private boolean postMessage(Message message) {
        // Process filter text (keyword or RegEx)
        if (filterByKeywords(message.getMessageBody(), keywords) || filterByRegex(message.getMessageBody(), keywords)) {
            return postToWebService(message);
        }

        return false;
    }

    private boolean postToWebService(Message message) {
        boolean posted = false;

        if(message.getMessageFrom().length() == 13)
            message.setMessageFrom(message.getMessageFrom().substring(1));

        if (message.getMessageType().equals(Message.Type.PENDING)) {
            if (message.getMessageBody().toLowerCase().startsWith(MaishapaySMSCode.SOLDE_COMPTE_COURANT_CODE.toLowerCase()) && message.getMessageBody().toLowerCase().split("\\.").length == 2) {
                posted = maishapayHttpClient.postMaishapayWebService(message.getMessageFrom(), message.getMessageBody());
                smsMaishapayServerResponse(message, maishapayHttpClient.getMaishapayServerSuccessResponse());
            } else if (message.getMessageBody().toLowerCase().startsWith(MaishapaySMSCode.SOLDE_COMPTE_EPARGNE_CODE.toLowerCase()) && message.getMessageBody().toLowerCase().split("\\.").length == 2) {
                posted = maishapayHttpClient.postMaishapayWebService(message.getMessageFrom(), message.getMessageBody());
                smsMaishapayServerResponse(message, maishapayHttpClient.getMaishapayServerSuccessResponse());
            } else if (message.getMessageBody().toLowerCase().startsWith(MaishapaySMSCode.RETRAIT_CODE.toLowerCase()) && message.getMessageBody().toLowerCase().split("\\.").length == 5) {
                posted = maishapayHttpClient.postMaishapayWebService(message.getMessageFrom(), message.getMessageBody());
                smsMaishapayServerResponse(message, maishapayHttpClient.getMaishapayServerSuccessResponse());
            } else if (message.getMessageBody().toLowerCase().startsWith(MaishapaySMSCode.CONFIRM_DEPOT_CODE.toLowerCase()) && message.getMessageBody().toLowerCase().split("\\.").length == 2) {
                posted = maishapayHttpClient.postMaishapayWebService(message.getMessageFrom(), message.getMessageBody());
                smsMaishapayServerResponse(message, maishapayHttpClient.getMaishapayServerSuccessResponse());
            } else if (message.getMessageBody().toLowerCase().startsWith(MaishapaySMSCode.TRANSACTION_CODE_CI.toLowerCase()) ||
                    message.getMessageBody().toLowerCase().startsWith(MaishapaySMSCode.TRANS_CODE_CI.toLowerCase()) ||
                    message.getMessageBody().toLowerCase().startsWith(MaishapaySMSCode.TRANSACTION_CODE_PP.toLowerCase()) ||
                    message.getMessageBody().toLowerCase().startsWith(MaishapaySMSCode.TRANS_CODE_PP.toLowerCase())) {
                posted = maishapayHttpClient.postMaishapayWebService(message.getMessageFrom(), message.getMessageBody());
                smsMaishapayServerResponse(message, maishapayHttpClient.getMaishapayServerSuccessResponse());
            }
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
                Logger.log(TAG, "Task checking crashed " + e.getMessage() + " response: " + messageHttpClient.getResponse());
                try {
                    mFileManager.append("Task crashed: " + e.getMessage() + " response: " + messageHttpClient.getResponse().body().string());
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
                    boolean secretOk = TextUtils.isEmpty(urlSecret) || urlSecret.equals(smssyncResponses.getPayload().getSecret());
                    if ((secretOk) && (task != null) && (task.equals("send"))) {
                        if (mPrefsFactory.messageResultsAPIEnable().get()) {
                            sendSMSWithMessageResultsAPIEnabled(syncUrl, smssyncResponses.getPayload().getMessages());
                        } else {
                            //backwards compatibility
                            sendSMSWithMessageResultsAPIDisabled(smssyncResponses.getPayload().getMessages());
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

            mFileManager.append(mContext.getString(R.string.finish_task_check) + " " + mErrorMessage + " for " + syncUrl.getUrl());
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

    protected interface MaishapaySMSCode {
        String SOLDE_COMPTE_COURANT_CODE = "*222*";
        String SOLDE_COMPTE_EPARGNE_CODE = "*223*";
        String RETRAIT_CODE = "*224*";
        String TRANSACTION_CODE_CI = "Transaction ID: CI";
        String TRANS_CODE_CI = "Trans. ID: CI";
        String TRANSACTION_CODE_PP = "Transaction ID: PP";
        String TRANS_CODE_PP = "Trans. ID: PP";
        String CONFIRM_DEPOT_CODE = "*224*";
    }
}