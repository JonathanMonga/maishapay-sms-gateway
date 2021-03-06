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

package com.maishapay.smssync.presentation.view.ui.navigation;

import android.app.Activity;
import android.content.Intent;

import com.maishapay.smssync.presentation.model.WebServiceModel;
import com.maishapay.smssync.presentation.view.ui.activity.AddKeywordsActivity;
import com.maishapay.smssync.presentation.view.ui.activity.AddPhoneNumberActivity;
import com.maishapay.smssync.presentation.view.ui.activity.AddTwitterKeywordsActivity;
import com.maishapay.smssync.presentation.view.ui.activity.AddWebServiceActivity;
import com.maishapay.smssync.presentation.view.ui.activity.GettingStartedActivity;
import com.maishapay.smssync.presentation.view.ui.activity.ListWebServiceActivity;
import com.maishapay.smssync.presentation.view.ui.activity.SettingsActivity;
import com.maishapay.smssync.presentation.view.ui.activity.TwitterProfileActivity;
import com.maishapay.smssync.presentation.view.ui.activity.UpdateWebServiceActivity;

import javax.inject.Inject;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class Launcher {

    private final Activity mActivity;

    @Inject
    public Launcher(Activity activity) {
        mActivity = activity;
    }

    public void launchSettings() {
        Intent intent = SettingsActivity.getIntent(mActivity);
        mActivity.startActivity(intent);
    }

    public void launchGettingStarted() {
        mActivity.startActivity(GettingStartedActivity.getIntent(mActivity));
    }

    public void launchListWebServices() {
        mActivity.startActivity(ListWebServiceActivity.getIntent(mActivity));
    }

    public void launchAddWebServices() {
        mActivity.startActivity(AddWebServiceActivity.getIntent(mActivity));
    }

    public void launchUpdateWebServices(WebServiceModel webServiceModel) {
        mActivity.startActivity(UpdateWebServiceActivity.getIntent(mActivity, webServiceModel));
    }

    /**
     * Launches the barcode reader
     */
    public void launchTwitterProfile() {
        mActivity.startActivity(TwitterProfileActivity.getIntent(mActivity));
    }

    /**
     * Launches activity for adding a new phone number
     */
    public void launchAddPhoneNumber() {
        mActivity.startActivity(AddPhoneNumberActivity.getIntent(mActivity));
    }

    /**
     * Launches activity for adding a new keyword
     */
    public void launchAddKeyword(WebServiceModel webServiceModel) {
        mActivity.startActivity(AddKeywordsActivity.getIntent(mActivity, webServiceModel));
    }

    /**
     * Launches activity for adding a new keyword for twitter service
     */
    public void launchAddTwitterKeyword() {
        mActivity.startActivity(AddTwitterKeywordsActivity.getIntent(mActivity));
    }
}
