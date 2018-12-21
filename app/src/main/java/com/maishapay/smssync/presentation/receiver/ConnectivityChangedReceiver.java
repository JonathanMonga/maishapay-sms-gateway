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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.presenter.AlertPresenter;
import com.maishapay.smssync.presentation.service.CheckTaskService;
import com.maishapay.smssync.presentation.service.ServiceConstants;
import com.maishapay.smssync.presentation.service.SyncPendingMessagesService;
import com.maishapay.smssync.presentation.task.SyncType;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.util.Utility;

/**
 * This Receiver class is designed to listen for changes in connectivity. When we receive
 * connectivity the relevant Service classes will automatically push pending messages and check for
 * tasks. This class will restart the service that pushes pending messages to the configured URL
 * and check for tasks from the configured URL.
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class ConnectivityChangedReceiver extends BroadcastReceiver {

    private AlertPresenter mAlertPresenter;

    @Override
    public void onReceive(Context context, Intent intent) {
        mAlertPresenter = App.getAppComponent().alertPresenter();
        if (Utility.isConnected(context) && App.getAppComponent().prefsFactory().serviceEnabled()
                .get()) {
            Intent syncPendingMessagesServiceIntent = new Intent(context,
                    SyncPendingMessagesService.class);
            syncPendingMessagesServiceIntent.putExtra(ServiceConstants.MESSAGE_UUID, "");
            syncPendingMessagesServiceIntent.putExtra(SyncType.EXTRA, SyncType.MANUAL.name());
            context.startService(syncPendingMessagesServiceIntent);
            CheckTaskService.sendWakefulWork(context, CheckTaskService.class);

            if (mAlertPresenter.lostConnectionThread != null
                    && mAlertPresenter.lostConnectionThread.isAlive()) {
                mAlertPresenter.lostConnectionThread.interrupt();
            }

            App.getAppComponent().fileManager()
                    .append(context.getString(R.string.active_data_connection));
        } else if (!Utility.isConnected(context) && App.getAppComponent().prefsFactory()
                .serviceEnabled().get()) {
            if (mAlertPresenter.lostConnectionThread == null
                    || !mAlertPresenter.lostConnectionThread.isAlive()) {
                mAlertPresenter.lostConnectionThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(AlertPresenter.MAX_DISCONNECT_TIME);
                        } catch (InterruptedException e) {
                            return;
                        }
                        mAlertPresenter.dataConnectionLost();
                    }
                });
                mAlertPresenter.lostConnectionThread.start();
            }
            App.getAppComponent().fileManager()
                    .append(context.getString(R.string.no_data_connection));

        }
    }
}
