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

package com.maishapay.smssync.presentation;

import android.support.v7.app.AppCompatDelegate;

import com.addhen.android.raiburari.presentation.BaseApplication;
import com.maishapay.smssync.data.twitter.TwitterBuilder;
import com.maishapay.smssync.data.twitter.TwitterClient;
import com.maishapay.smssync.presentation.di.component.AppComponent;

import timber.log.Timber;

/**
 * @author Maishapay Team <online@maishapay.online>
 */

public class App extends BaseApplication {

    private static AppComponent mAppComponent;

    private static TwitterClient mTwitter;

    private static App mApp;

    public static synchronized TwitterClient getTwitterInstance() {
        if (mTwitter == null) {
            mTwitter = new TwitterBuilder(mApp, "", "").build();
        }
        return mTwitter;
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    /**
     * Return the application tracker
     */
    public static AppTracker getInstance() {
        return TrackerResolver.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        mApp = this;
        Timber.plant(new FirebaseCrashTree());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private void initializeInjector() {
        mAppComponent = AppComponent.Initializer.init(this);
    }
}
