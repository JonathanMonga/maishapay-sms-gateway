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
import android.support.annotation.Nullable;

import com.addhen.android.raiburari.presentation.di.HasComponent;
import com.addhen.android.raiburari.presentation.ui.activity.BaseActivity;
import com.maishapay.smssync.R;
import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.di.component.AppComponent;
import com.maishapay.smssync.presentation.di.component.DaggerWebServiceComponent;
import com.maishapay.smssync.presentation.di.component.WebServiceComponent;
import com.maishapay.smssync.presentation.model.WebServiceModel;
import com.maishapay.smssync.presentation.view.ui.fragment.AddKeywordFragment;

/**
 * Renders {@link AddKeywordFragment}
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class AddKeywordsActivity extends BaseActivity implements HasComponent<WebServiceComponent> {

    private static final String FRAG_TAG = "add_keyword";

    private static final String INTENT_EXTRA_WEBSERVICE_MODEL
            = "com.maishapay.smssync.INTENT_PARAM_ARTIST_ID";

    private static final String BUNDLE_STATE_PARAM_WEBSERVICE_MODEL
            = "com.maishapay.smssync.BUNDLE_STATE_PARAM_WEBSERVICE_MODEL";

    private AddKeywordFragment mAddKeywordFragment;

    private WebServiceModel mWebServiceModel;

    private WebServiceComponent mUpdateWebServiceComponent;

    /**
     * Default constructor
     */
    public AddKeywordsActivity() {
        super(R.layout.activity_add_keyword, 0);
    }

    /**
     * Provides {@link Intent} launching this activity
     *
     * @param context The calling context
     * @return The intent to be launched
     */
    public static Intent getIntent(final Context context, WebServiceModel webServiceModel) {
        Intent intent = new Intent(context, AddKeywordsActivity.class);
        intent.putExtra(INTENT_EXTRA_WEBSERVICE_MODEL, webServiceModel);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector();
        setupFragment(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(BUNDLE_STATE_PARAM_WEBSERVICE_MODEL, mWebServiceModel);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void injector() {
        getAppComponent().inject(this);
        mUpdateWebServiceComponent = DaggerWebServiceComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private void setupFragment(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mWebServiceModel = savedInstanceState
                    .getParcelable(BUNDLE_STATE_PARAM_WEBSERVICE_MODEL);
        } else {
            mWebServiceModel = (WebServiceModel) getIntent().getParcelableExtra(
                    INTENT_EXTRA_WEBSERVICE_MODEL);
        }
        mAddKeywordFragment = (AddKeywordFragment) getSupportFragmentManager().findFragmentByTag(
                FRAG_TAG);
        if (mAddKeywordFragment == null) {
            mAddKeywordFragment = AddKeywordFragment.newInstance(mWebServiceModel);
            replaceFragment(R.id.add_keyword_fragment_container, mAddKeywordFragment, FRAG_TAG);
        }
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
}
