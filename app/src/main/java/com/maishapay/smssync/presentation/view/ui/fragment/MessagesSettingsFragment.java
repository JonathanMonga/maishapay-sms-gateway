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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.maishapay.smssync.R;
import com.maishapay.smssync.presentation.view.log.AddLogView;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class MessagesSettingsFragment extends BasePreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener, AddLogView {

    public static final String MESSAGES_FRAGMENT_SETTINGS = "messages_settings_fragment";

    public static final String KEY_ENABLE_REPLY = "enable_reply_preference";

    public static final String KEY_ENABLE_REPLY_FRM_SERVER = "enable_reply_frm_server_preference";

    public static final String KEY_REPLY = "reply_preference";

    private SwitchPreferenceCompat mEnableReplyFrmServer;

    private SwitchPreferenceCompat mEnableReply;

    private EditTextPreference mReplyPref;

    public MessagesSettingsFragment() {
        // Do nothing
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String key) {
        addPreferencesFromResource(R.xml.messages_preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    private void initialize() {
        mEnableReply = (SwitchPreferenceCompat) getPreferenceScreen()
                .findPreference(KEY_ENABLE_REPLY);
        mEnableReplyFrmServer = (SwitchPreferenceCompat) getPreferenceScreen()
                .findPreference(KEY_ENABLE_REPLY_FRM_SERVER);
        mReplyPref = (EditTextPreference) getPreferenceScreen().findPreference(KEY_REPLY);
        savePreferences();
    }

    private void savePreferences() {
        // log reply changes.
        if (!mPrefs.reply().get().equals(mReplyPref.getText().toString())) {
            // Log old value and new value.
            mAddLogPresenter.addLog(getString(R.string.settings_changed,
                    mReplyPref.getDialogTitle().toString(),
                    mPrefs.reply().get(), mReplyPref.getText().toString()));
        }
        mPrefs.reply().set(mReplyPref.getText().toString());

        if (mPrefs.enableReplyFrmServer().get() != mEnableReplyFrmServer.isChecked()) {
            boolean checked = mEnableReplyFrmServer.isChecked() ? true : false;
            String check = getCheckedStatus(checked);

            String status = getCheckedStatus(mPrefs.enableReplyFrmServer().get());

            mAddLogPresenter.addLog(getString(R.string.settings_changed, mEnableReplyFrmServer.getTitle().toString(), status, check));
        }
        mPrefs.enableReplyFrmServer().set(mEnableReplyFrmServer.isChecked());
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        savePreferences();
    }
}
