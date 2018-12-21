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

package com.maishapay.smssync.data.exception;

import com.maishapay.smssync.data.entity.SyncUrl;

/**
 * Exception thrown by {@link com.maishapay.smssync.data.database.WebServiceDatabaseHelper} when a
 * {@link SyncUrl} can't be found from the database.
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class WebServiceNotFoundException extends Exception {

    /**
     * Default exception
     */
    public WebServiceNotFoundException() {
        super();
    }

    /**
     * Initialize the exception with a custom message
     *
     * @param message The message be shown when the exception is thrown
     */
    public WebServiceNotFoundException(final String message) {
        super(message);
    }

    /**
     * Initialize the exception with a custom message and the cause of the exception
     *
     * @param message The message to be shown when the exception is thrown
     * @param cause   The cause of the exception
     */
    public WebServiceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Initialize the exception with a the cause of the exception
     *
     * @param cause The cause of the exception
     */
    public WebServiceNotFoundException(final Throwable cause) {
        super(cause);
    }
}
