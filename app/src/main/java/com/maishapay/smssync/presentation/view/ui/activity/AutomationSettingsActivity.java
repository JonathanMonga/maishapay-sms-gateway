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

package com.maishapay.smssync.presentation.view.ui.activity;

import android.os.Bundle;

import com.maishapay.smssync.R;

/**
 * Automation settings
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AutomationSettingsActivity extends BasePreferenceActivity {

    public static final String AUTO_SYNC_TIMES = "auto_sync_times";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.automation_preferences);
        setToolbarTitle(R.string.automation);
    }
}
