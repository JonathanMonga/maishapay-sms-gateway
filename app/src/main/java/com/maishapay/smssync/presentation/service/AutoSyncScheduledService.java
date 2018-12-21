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

package com.maishapay.smssync.presentation.service;

import android.content.Intent;

import com.maishapay.smssync.data.message.MaishapayMessage;
import com.maishapay.smssync.data.message.PostMessage;
import com.maishapay.smssync.data.message.TweetMessage;

import javax.inject.Inject;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class AutoSyncScheduledService extends BaseWakefulIntentService {

    private static String CLASS_TAG = AutoSyncScheduledService.class.getSimpleName();
    @Inject
    PostMessage mProcessMessage;
    @Inject
    TweetMessage mTweetMessage;
    @Inject
    MaishapayMessage mMaishapayMessage;
    // holds the status of the sync and sends it to the pending messages
    // activity to update the ui
    private Intent statusIntent;

    public AutoSyncScheduledService() {
        super(CLASS_TAG);
        statusIntent = new Intent(ServiceConstants.AUTO_SYNC_ACTION);
    }

    public void onCreate() {
        super.onCreate();
        getComponent().inject(this);
    }

    @Override
    protected void executeTask(Intent intent) {
        log(CLASS_TAG, "doWakefulWork() executing " + CLASS_TAG);

        //mTweetMessage.syncPendingMessages();
        //mProcessMessage.syncPendingMessages();

        mMaishapayMessage.syncPendingMessages();

        //statusIntent.putExtra("status", mProcessMessage.getErrorMessage());

        statusIntent.putExtra("status", mMaishapayMessage.getErrorMessage());
        sendBroadcast(statusIntent);
    }
}
