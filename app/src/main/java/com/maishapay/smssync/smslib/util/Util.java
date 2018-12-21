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

package com.maishapay.smssync.smslib.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class Util {

    private static final int KITKAT = 19;

    private Util() {
        // No instance
    }

    /**
     * Gets the current users phone number
     *
     * @param context is the context of the activity or service
     * @return a string of the phone number on the device
     */
    public static String getMyPhoneNumber(Context context) {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }

    /**
     * Check if the device runs Android 4.4 (KitKat) or higher.
     */
    public static boolean isKitKatOrHigher() {
        return Build.VERSION.SDK_INT >= KITKAT;
    }
}
