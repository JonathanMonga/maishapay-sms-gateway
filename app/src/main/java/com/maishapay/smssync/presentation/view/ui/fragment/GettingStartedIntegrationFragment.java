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

import com.addhen.android.raiburari.presentation.ui.fragment.BaseFragment;
import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.view.ui.activity.ListWebServiceActivity;
import com.maishapay.smssync.presentation.view.ui.activity.MainActivity;

import com.maishapay.smssync.R;

import butterknife.OnClick;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class GettingStartedIntegrationFragment extends BaseFragment {

    private static GettingStartedIntegrationFragment mIntegrationFragment;

    public GettingStartedIntegrationFragment() {
        super(R.layout.fragment_getting_started_add_integration, 0);
    }

    public static GettingStartedIntegrationFragment newInstance() {
        if (mIntegrationFragment == null) {
            mIntegrationFragment = new GettingStartedIntegrationFragment();
        }
        return mIntegrationFragment;
    }

    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.twitter)
    void onTwitterClicked() {
        if (App.getTwitterInstance().getSessionManager().getActiveSession() == null) {
            App.getTwitterInstance().login(getActivity());
            return;
        }
        // Show profile
        ((MainActivity) getActivity()).replaceFragment(R.id.fragment_main_content,
                TwitterProfileFragment.newInstance(), "twitter_profile");
    }

    @OnClick(R.id.custom_web_service)
    void onCustomWebServiceClicked() {
        getActivity().startActivity(ListWebServiceActivity.getIntent(getActivity()));
    }
}
