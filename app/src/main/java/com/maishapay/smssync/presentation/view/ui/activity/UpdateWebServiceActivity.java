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
import com.maishapay.smssync.presentation.view.ui.fragment.UpdateWebServiceFragment;

/**
 * Renders {@link UpdateWebServiceFragment}
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class UpdateWebServiceActivity extends BaseActivity
        implements HasComponent<WebServiceComponent>,
        UpdateWebServiceFragment.UpdateWebServiceListener {

    private static final String INTENT_EXTRA_PARAM_WEB_SERVICE_MODEL
            = "com.maishapay.smssync.INTENT_PARAM_WEB_SERVICE_MODEL";

    private static final String INTENT_STATE_PARAM_WEB_SERVICE
            = "com.maishapay.smssync.STATE_PARAM_WEB_SERVICE";

    private static final String FRAG_TAG = "update_webService";

    private WebServiceModel mWebServiceModel;

    private WebServiceComponent mUpdateWebServiceComponent;

    private UpdateWebServiceFragment mUpdateWebServiceFragment;

    /**
     * Default constructor
     */
    public UpdateWebServiceActivity() {
        super(R.layout.activity_update_web_service, 0);
    }

    /**
     * Provides {@link Intent} launching this activity
     *
     * @param context         The calling context
     * @param webServiceModel The webService model
     * @return The intent to be launched
     */
    public static Intent getIntent(final Context context, WebServiceModel webServiceModel) {
        Intent intent = new Intent(context, UpdateWebServiceActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_WEB_SERVICE_MODEL, webServiceModel);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector();
        if (savedInstanceState == null) {
            mWebServiceModel = getIntent().getParcelableExtra(INTENT_EXTRA_PARAM_WEB_SERVICE_MODEL);
        } else {
            mWebServiceModel = savedInstanceState.getParcelable(INTENT_STATE_PARAM_WEB_SERVICE);
        }
        mUpdateWebServiceFragment = (UpdateWebServiceFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG);
        if (mUpdateWebServiceFragment == null) {
            mUpdateWebServiceFragment = UpdateWebServiceFragment.newInstance(mWebServiceModel);
            replaceFragment(R.id.update_fragment_container, mUpdateWebServiceFragment, FRAG_TAG);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putParcelable(INTENT_EXTRA_PARAM_WEB_SERVICE_MODEL, mWebServiceModel);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mUpdateWebServiceFragment.showWebService(mWebServiceModel);
    }

    private void injector() {
        getAppComponent().inject(this);
        mUpdateWebServiceComponent = DaggerWebServiceComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public WebServiceComponent getComponent() {
        return mUpdateWebServiceComponent;
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
    public void onUpdateNavigateOrReloadList() {
        //mUpdateWebServiceComponent.launcher().launchListWebService();
        finish();
    }

    @Override
    public void onCancelUpdate() {
        finish();
    }
}
