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

package com.maishapay.smssync.data.twitter;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public abstract class AuthToken implements AuthHeaders {

    /**
     * Unit time or epoch time when the token was created (always in UTC). The
     * time may be 0 if the token is deserialized from data missing the field.
     */
    protected final long createdAt;

    public AuthToken() {
        createdAt = System.currentTimeMillis();
    }

    protected AuthToken(long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Determines whether a token is known to have expired.
     *
     * @return true if the token is known to have expired, otherwise false to indicate the token
     * may or may not be considered expired by the server.
     */
    public abstract boolean isExpired();
}
