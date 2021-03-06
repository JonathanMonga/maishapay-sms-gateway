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

package com.maishapay.smssync.presentation.view.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.addhen.android.raiburari.presentation.di.HasComponent;
import com.addhen.android.raiburari.presentation.ui.activity.BaseActivity;
import com.maishapay.smssync.R;
import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.di.component.AppComponent;
import com.maishapay.smssync.presentation.di.component.DaggerWebServiceComponent;
import com.maishapay.smssync.presentation.di.component.WebServiceComponent;
import com.maishapay.smssync.presentation.model.WebServiceModel;
import com.maishapay.smssync.presentation.view.ui.fragment.ListWebServiceFragment;
import com.maishapay.smssync.presentation.view.ui.navigation.Launcher;

import javax.inject.Inject;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class ListWebServiceActivity extends BaseActivity
        implements HasComponent<WebServiceComponent>,
        ListWebServiceFragment.WebServiceListListener {

    private static final String FRAG_TAG = "list_webService";

    @Inject
    Launcher mLauncher;

    private ListWebServiceFragment mListWebServiceFragment;

    private WebServiceComponent mListWebServiceComponent;


    /**
     * Default constructor
     */
    public ListWebServiceActivity() {
        super(R.layout.activity_list_web_service, 0);
    }

    /**
     * Provides {@link Intent} launching this activity
     *
     * @param context The calling context
     * @return The intent to be launched
     */
    public static Intent getIntent(final Context context) {
        return new Intent(context, ListWebServiceActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector();
        mListWebServiceFragment = (ListWebServiceFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG);
        if (mListWebServiceFragment == null) {
            mListWebServiceFragment = ListWebServiceFragment.newInstance();
            replaceFragment(R.id.add_fragment_container, mListWebServiceFragment);
        }
    }

    private void injector() {
        getAppComponent().inject(this);
        mListWebServiceComponent = DaggerWebServiceComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public WebServiceComponent getComponent() {
        return mListWebServiceComponent;
    }

    /**
     * Gets the Main Application component for dependency injection.
     *
     * @return {@link com.addhen.android.raiburari.presentation.di.component.ApplicationComponent}
     */
    public AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }

    @Override
    public void onWebServiceClicked(WebServiceModel webServiceModel) {
        mListWebServiceComponent.launcher().launchUpdateWebServices(webServiceModel);
    }
}