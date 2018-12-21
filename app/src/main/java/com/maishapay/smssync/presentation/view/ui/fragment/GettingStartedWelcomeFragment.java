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

import android.os.Bundle;

import com.addhen.android.raiburari.presentation.ui.fragment.BaseFragment;

import com.maishapay.smssync.R;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class GettingStartedWelcomeFragment extends BaseFragment {

    private static GettingStartedWelcomeFragment mGettingStartedWelcomeFragment;

    public GettingStartedWelcomeFragment() {
        super(R.layout.fragment_getting_started_welcome, 0);
    }

    public static GettingStartedWelcomeFragment newInstance() {
        if (mGettingStartedWelcomeFragment == null) {
            mGettingStartedWelcomeFragment = new GettingStartedWelcomeFragment();
        }
        return mGettingStartedWelcomeFragment;
    }

    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setRetainInstance(true);
    }
}
