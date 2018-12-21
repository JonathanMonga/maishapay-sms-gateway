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

package com.maishapay.smssync.presentation.view.ui.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.util.TimeFrequencyUtil;
import com.maishapay.smssync.presentation.view.ui.fragment.AutomationSettingsFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.TaskSettingsFragment;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.data.util.Logger;

/**
 * Created by Kamil Kalfas(kkalfas@soldevelo.com) on 19.05.14.
 * <p/>
 * Fields and methods are inherited from DialogPreference and TimePicker so DO NOT BE MISLED by
 * those names
 */
public class TimePreference extends DialogPreference {

    //fist picker field
    private int mLastHour = 0;

    //second picker field
    private int mLastMinute = 0;

    private PrefsFactory mPrefsFactory;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPrefsFactory = App.getAppComponent().prefsFactory();
        setPositiveButtonText(getContext().getString(R.string.ok));
        setNegativeButtonText(getContext().getString(R.string.cancel));
    }

    private static int getHour(String time) {
        String[] pieces = time.split(":");
        return (Integer.parseInt(pieces[0]));
    }

    private static int getMinute(String time) {
        String[] pieces = time.split(":");
        return (Integer.parseInt(pieces[1]));
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time;
        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString(loadTimeFrequency());
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }
        //This is needed for backward compatible with versions pre 2.6
        //It adjusts format of input string from "mm" to "hh:mm"
        if (!time.contains(":")) {
            int minutes = Integer.parseInt(time);
            time = Integer.toString((minutes / 60)) + ":" + Integer.toString((minutes % 60));
        }
        setLastHour(getHour(time));
        setLastMinute(getMinute(time));
    }

    public String getTimeValueAsString() {
        String h = String.valueOf(getLastHour());
        String m = String.valueOf(getLastMinute());
        String time = appendZeroAtBegin(h) + ":" + appendZeroAtBegin(m);
        return time;
    }

    private String appendZeroAtBegin(String time) {
        StringBuilder sb = new StringBuilder();
        if (time.length() == 1) {
            sb.append(0);
        }
        return sb.append(time).toString();
    }

    public void persistStringValue(String value) {
        persistString(value);
    }

    public void saveTimeFrequency() {
        final String timeFrequency = getTimeValueAsString();
        if (TaskSettingsFragment.TASK_CHECK_TIMES.equals(this.getKey())) {
            Logger.log("TimePreferences", "Save TASK time " + getTimeValueAsString());
            mPrefsFactory.taskCheckTime().set(getTimeValueAsString());
        } else if (AutomationSettingsFragment.AUTO_SYNC_TIMES.equals(this.getKey())) {
            Logger.log("TimePreferences", "Save AUTO time " + getTimeValueAsString());
            mPrefsFactory.autoTime().set(getTimeValueAsString());
        }
    }

    public int getLastHour() {
        return this.mLastHour;
    }

    public void setLastHour(int lastHour) {
        this.mLastHour = lastHour;
    }

    public int getLastMinute() {
        return this.mLastMinute;
    }

    public void setLastMinute(int lastMinute) {
        this.mLastMinute = lastMinute;
    }

    private String loadTimeFrequency() {
        // TODO: Figure out a way to load the saved frequency without knowing the keys
        String time = null;
        if (TaskSettingsFragment.TASK_CHECK_TIMES.equals(this.getKey())) {
            time = mPrefsFactory.taskCheckTime().get();
        } else if (AutomationSettingsFragment.AUTO_SYNC_TIMES.equals(this.getKey())) {
            time = mPrefsFactory.autoTime().get();
        } else {
            time = TimeFrequencyUtil.DEFAULT_TIME_FREQUENCY;
        }
        Logger.log("TimePreferences", "Loading saved time: " + time);
        return time;
    }
}
