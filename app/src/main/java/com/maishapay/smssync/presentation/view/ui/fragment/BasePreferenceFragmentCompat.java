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

package com.maishapay.smssync.presentation.view.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.Toast;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.presentation.di.component.SettingsComponent;
import com.maishapay.smssync.presentation.presenter.AddLogPresenter;
import com.maishapay.smssync.presentation.service.ServiceControl;
import com.maishapay.smssync.presentation.view.log.AddLogView;
import com.maishapay.smssync.presentation.view.ui.activity.SettingsActivity;

import javax.inject.Inject;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public abstract class BasePreferenceFragmentCompat extends PreferenceFragmentCompat implements
        AddLogView {

    @Inject
    PrefsFactory mPrefs;

    @Inject
    ServiceControl mServiceControl;

    @Inject
    AddLogPresenter mAddLogPresenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        getComponent(SettingsComponent.class).inject(this);
        mAddLogPresenter.setView(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the default white background in the view so as to avoid transparency
        view.setBackgroundColor(
                ContextCompat.getColor(getContext(), R.color.background_material_light));

    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((SettingsActivity) getActivity()).getComponent());
    }

    /**
     * A convenient method to return boolean values to a more meaningful format
     *
     * @param status The boolean value
     * @return The meaningful format
     */
    protected String getCheckedStatus(boolean status) {
        if (status) {
            return getString(R.string.enabled);
        }
        return getString(R.string.disabled);
    }

    @Override
    public void onAdded(Long row) {
        // Do nothing
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getAppContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getAppContext() {
        return getContext();
    }
}
