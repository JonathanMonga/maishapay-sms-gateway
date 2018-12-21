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

import java.util.Map;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public interface SessionManager<T extends Session> {

    /**
     * @return the active session, restoring saved session if available
     */
    T getActiveSession();

    /**
     * Sets the active session.
     */
    void setActiveSession(T session);

    /**
     * Clears the active session.
     */
    void clearActiveSession();

    /**
     * @return the session associated with the id.
     */
    T getSession(long id);

    /**
     * Sets the session to associate with the id. If there is no active session, this session also
     * becomes the active session.
     */
    void setSession(long id, T session);

    /**
     * Clears the session associated with the id.
     */
    void clearSession(long id);

    /**
     * @return the session map containing all managed sessions
     */
    Map<Long, T> getSessionMap();
}
