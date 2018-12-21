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
import com.maishapay.smssync.presentation.presenter.AlertPresenter;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.message.PostMessage;
import com.maishapay.smssync.data.util.Utility;

import javax.inject.Inject;

/**
 * @author Henry Addo
 */
public class CheckTaskService extends BaseWakefulIntentService {

    private final static String CLASS_TAG = CheckTaskService.class
            .getSimpleName();

    @Inject
    PostMessage mProcessMessage;

    @Inject
    MaishapayMessage mMaishapayMessage;

    @Inject
    PrefsFactory mPrefsFactory;

    @Inject
    FileManager mFileManager;

    @Inject
    AlertPresenter mAlertPresenter;

    public CheckTaskService() {
        super(CLASS_TAG);
    }

    public void onCreate() {
        super.onCreate();
        getComponent().inject(this);
    }

    protected void executeTask(Intent intent) {
        log("checkTaskService: check if a task has been enabled.");
        if (Utility.isConnected(this)) {
            if (mPrefsFactory.serviceEnabled().get() && mPrefsFactory.enableTaskCheck().get()) {
                //mProcessMessage = getAppComponent().processMessage();

                mMaishapayMessage = getAppComponent().processMaishapayMessage();
                mMaishapayMessage.performTask();
            }
            return;
        }
        mFileManager.append(getString(R.string.no_data_connection));
    }
}
