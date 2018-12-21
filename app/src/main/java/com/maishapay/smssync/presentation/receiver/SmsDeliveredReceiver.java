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

package com.maishapay.smssync.presentation.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.model.MessageModel;
import com.maishapay.smssync.presentation.service.ServiceConstants;
import com.maishapay.smssync.presentation.service.UpdateMessageService;
import com.maishapay.smssync.smslib.sms.ProcessSms;

/**
 * @author Henry Addo
 */
public class SmsDeliveredReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int result = getResultCode();
        MessageModel message = intent.getParcelableExtra(ProcessSms.DELIVERED_SMS_BUNDLE);
        FileManager fileManager = App.getAppComponent().fileManager();
        String resultMessage = "";
        switch (result) {
            case Activity.RESULT_OK:
                resultMessage = context.getResources().getString(R.string.sms_delivered);
                Toast.makeText(context, context.getResources().getString(R.string.sms_delivered),
                        Toast.LENGTH_LONG);
                fileManager.append(context.getResources().getString(
                        R.string.sms_delivered));
                break;
            case Activity.RESULT_CANCELED:
                resultMessage = context.getResources().getString(R.string.sms_not_delivered);
                Toast.makeText(context,
                        context.getResources().getString(R.string.sms_not_delivered),
                        Toast.LENGTH_LONG);
                fileManager.append(
                        context.getResources().getString(R.string.sms_not_delivered));
                break;
        }

        if (message != null) {
            message.setDeliveryResultMessage(resultMessage);
            message.setDeliveryResultCode(result);
            message.setMessageType(MessageModel.Type.TASK);
            message.setStatus(MessageModel.Status.SENT);

            // Update this in a service to guarantee it will run
            Intent updateService = new Intent(context, UpdateMessageService.class);
            updateService.putExtra(ServiceConstants.UPDATE_MESSAGE, message);
            context.startService(updateService);
        }
    }
}
