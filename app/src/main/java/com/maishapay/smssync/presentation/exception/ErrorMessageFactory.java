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

package com.maishapay.smssync.presentation.exception;

import android.content.Context;

import com.maishapay.smssync.BuildConfig;
import com.maishapay.smssync.R;
import com.maishapay.smssync.data.exception.FilterNotFoundException;
import com.maishapay.smssync.data.exception.MessageNotFoundException;

/**
 * Creates the various app exceptions
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        // Do nothing
    }

    /**
     * Creates a String representing an error message.
     *
     * @param context   Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);
        if (exception instanceof FilterNotFoundException) {
            message = context.getString(R.string.exception_message_filter_not_found);
        } else if (exception instanceof MessageNotFoundException) {
            message = context.getString(R.string.exception_message_filter_not_found);
        }

        // Only print stacktrace when running a debug build for debugging purposes
        if (BuildConfig.DEBUG) {
            exception.printStackTrace();
        }
        return message;
    }
}
