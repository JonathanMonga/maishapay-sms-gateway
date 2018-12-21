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
import com.maishapay.smssync.presentation.view.ui.fragment.AddWebServiceFragment;

/**
 * Renders {@link AddWebServiceFragment}
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class AddWebServiceActivity extends BaseActivity implements HasComponent<WebServiceComponent> {

    private static final String FRAG_TAG = "add_webService";

    private WebServiceComponent mAddWebServiceComponent;

    private AddWebServiceFragment mAddWebServiceFragment;

    /**
     * Default constructor
     */
    public AddWebServiceActivity() {
        super(R.layout.activity_add_web_service, 0);
    }

    /**
     * Provides {@link Intent} launching this activity
     *
     * @param context The calling context
     * @return The intent to be launched
     */
    public static Intent getIntent(final Context context) {
        return new Intent(context, AddWebServiceActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector();
        mAddWebServiceFragment = (AddWebServiceFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        if (mAddWebServiceFragment == null) {
            mAddWebServiceFragment = AddWebServiceFragment.newInstance();
            replaceFragment(R.id.add_fragment_container, mAddWebServiceFragment, FRAG_TAG);
        }
    }

    private void injector() {
        getAppComponent().inject(this);
        mAddWebServiceComponent = DaggerWebServiceComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public WebServiceComponent getComponent() {
        return mAddWebServiceComponent;
    }

    /**
     * Gets the Main Application component for dependency injection.
     *
     * @return {@link com.addhen.android.raiburari.presentation.di.component.ApplicationComponent}
     */
    public AppComponent getAppComponent() {
        return App.getAppComponent();
    }
}
