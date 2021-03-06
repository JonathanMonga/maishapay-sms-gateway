/*
 * Copyright (c) 2010 - 2017 Ushahidi Inc
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

package com.maishapay.smssync.presentation;

import timber.log.Timber;

//import com.google.firebase.crash.FirebaseCrash;

/**
 * A {@link Timber.Tree} that logs and sends crash reports to firebase's crash
 * report console.
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class FirebaseCrashTree extends Timber.Tree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        /*
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            FirebaseCrash.log((priority == Log.DEBUG ? "[debug] " : "[verbose] ") + tag + ": "
                    + message);
            return;
        }
        FirebaseCrash.logcat(priority, tag, message);
        if (t == null) {
            return;
        }

        if (priority == Log.ERROR || priority == Log.WARN) {
            FirebaseCrash.report(t);
        }
        */
    }
}
