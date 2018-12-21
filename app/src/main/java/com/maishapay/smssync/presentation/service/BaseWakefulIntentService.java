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

package com.maishapay.smssync.presentation.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.addhen.android.raiburari.presentation.di.HasComponent;
import com.addhen.android.raiburari.presentation.di.component.ApplicationComponent;
import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.di.component.AppComponent;
import com.maishapay.smssync.presentation.di.component.AppServiceComponent;
import com.maishapay.smssync.presentation.di.component.DaggerAppServiceComponent;
import com.maishapay.smssync.presentation.di.module.ServiceModule;
import com.maishapay.smssync.presentation.receiver.ConnectivityChangedReceiver;
import com.maishapay.smssync.presentation.util.Utility;

import com.maishapay.smssync.data.util.Logger;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseWakefulIntentService extends WakefulIntentService implements
        HasComponent<AppServiceComponent> {

    protected AppServiceComponent mAppServiceComponent;

    public BaseWakefulIntentService(String name) {
        super(name);
    }

    /*
     * Subclasses must implement this method so it executes any tasks
     * implemented in it.
     */
    protected abstract void executeTask(Intent intent);

    @Override
    public void doWakefulWork(final Intent intent) {
        log("onHandleIntent(): running service");

        boolean isConnected = Utility.isConnected(this);

        // check if we have internet
        if (!isConnected) {
            // Enable the Connectivity Changed Receiver to listen for
            // connection to a network so we can execute pending messages.
            PackageManager pm = getPackageManager();
            ComponentName connectivityReceiver = new ComponentName(this,
                    ConnectivityChangedReceiver.class);
            pm.setComponentEnabledSetting(connectivityReceiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        } else {
            // Execute the task
            executeTask(intent);
        }
    }

    private void injector() {
        mAppServiceComponent = DaggerAppServiceComponent.builder()
                .appComponent(getAppComponent())
                .serviceModule(new ServiceModule(this))
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        injector();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void log(String message) {
        Logger.log(getClass().getName(), message);
    }

    protected void log(String format, Object... args) {
        Logger.log(getClass().getName(), format, args);
    }

    protected void log(String message, Exception ex) {
        Logger.log(getClass().getName(), message, ex);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((App) getApplication()).getApplicationComponent();
    }

    @Override
    public AppServiceComponent getComponent() {
        return mAppServiceComponent;
    }

    protected AppComponent getAppComponent() {
        return App.getAppComponent();
    }
}
