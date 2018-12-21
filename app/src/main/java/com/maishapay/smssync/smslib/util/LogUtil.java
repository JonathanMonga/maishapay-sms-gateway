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

import android.util.Log;

import com.maishapay.smssync.BuildConfig;

/**
 * Utility for logging items
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class LogUtil {

    public static final boolean LOGGING_MODE = BuildConfig.DEBUG;

    private LogUtil() {
        // No instantiation
    }

    public static void logDebug(String tag, String message) {
        if (LOGGING_MODE) {
            Log.d(tag, message);
        }
    }

    public static void logInfo(String tag, String message) {
        if (LOGGING_MODE) {
            Log.i(tag, message);
        }
    }

    public static void logDebug(String tag, String message, Object... args) {
        if (LOGGING_MODE) {
            Log.d(tag, message);
        }
    }

    public static void logInfo(String tag, String format, Object... args) {
        if (LOGGING_MODE) {
            Log.i(tag, String.format(format, args));
        }
    }

    public static void logError(String tag, String message, Exception ex) {
        if (LOGGING_MODE) {
            Log.e(tag, message, ex);
        }
    }
}
