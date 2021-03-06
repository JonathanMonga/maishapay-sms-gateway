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

package com.maishapay.smssync.presentation.presenter;

import android.content.Context;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.entity.Message;
import com.maishapay.smssync.data.message.PostMessage;
import com.maishapay.smssync.data.net.AppHttpClient;
import com.maishapay.smssync.data.net.BaseHttpClient;
import com.maishapay.smssync.domain.entity.WebServiceEntity;
import com.maishapay.smssync.domain.repository.WebServiceRepository;
import com.maishapay.smssync.presentation.model.MessageModel;
import com.maishapay.smssync.presentation.util.Utility;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class AlertPresenter {

    public final static int MAX_DISCONNECT_TIME = 15000;
    private static final String TASK_PARAM = "Task";
    private static final String MESSAGE_PARAM = "message";
    public Thread lostConnectionThread;
    private PrefsFactory mPrefsFactory;

    private AppHttpClient mAppHttpClient;

    private PostMessage mProcessMessage;

    private WebServiceRepository mWebServiceRepository;

    private FileManager mFileManager;

    private Context mContext;

    @Inject
    public AlertPresenter(Context context, PrefsFactory prefsFactory, AppHttpClient appHttpClient,
                          PostMessage processMessage, WebServiceRepository webServiceRepository,
                          FileManager fileManager) {
        mContext = context;
        mPrefsFactory = prefsFactory;
        mAppHttpClient = appHttpClient;
        mProcessMessage = processMessage;
        mWebServiceRepository = webServiceRepository;
        mFileManager = fileManager;
    }

    /**
     * If battery level drops to low post alert to server send alert text to stored phone number
     */
    public void lowBatteryLevelRequest(int batteryLevel) {
        List<WebServiceEntity> webServiceEntities = mWebServiceRepository.syncGetByStatus(WebServiceEntity.Status.ENABLED);
        if (!Utility.isEmpty(webServiceEntities)) {
            for (WebServiceEntity webServiceEntity : webServiceEntities) {
                mAppHttpClient.setUrl(webServiceEntity.getUrl());
                mAppHttpClient.addParam(TASK_PARAM, "alert");
                mAppHttpClient.addParam(MESSAGE_PARAM, mContext.getResources()
                        .getString(R.string.battery_level_message, batteryLevel));
                try {
                    mAppHttpClient.setMethod(BaseHttpClient.HttpMethod.POST);
                    mAppHttpClient.execute();
                } catch (Exception e) {
                    mFileManager.append(e.getMessage());
                } finally {
                    if ((mAppHttpClient.getResponse()) != null && (200 == mAppHttpClient.getResponse().code())) {
                        mFileManager.append(
                                mContext.getResources()
                                        .getString(R.string.successful_alert_to_server));
                    }
                }
            }

            if (!mPrefsFactory.alertPhoneNumber().get().matches("")) {
                sendSms(mContext.getResources().getString(R.string.battery_level_message,
                        batteryLevel));
            }
        }
    }

    /**
     * If data connection is lost for extended time (either WiFi, or GSM) send alert SMS to stored
     * phone number
     */

    public void dataConnectionLost() {
        if (!mPrefsFactory.alertPhoneNumber().get().matches("")) {
            sendSms(mContext.getResources().getString(R.string.lost_connection_message));
        }
    }

    private void sendSms(String body) {
        final Long timeMills = System.currentTimeMillis();
        Message message = new Message();
        message.setMessageBody(body);
        message.setMessageDate(new Date(timeMills));
        message.setMessageFrom(mPrefsFactory.alertPhoneNumber().get());
        message.setMessageUuid(mProcessMessage.getProcessSms().getUuid());
        message.setMessageType(Message.Type.ALERT);
        MessageModel smsMessage = mProcessMessage.map(message);
        mProcessMessage.getProcessSms().sendSms(smsMessage, false);
    }
}
