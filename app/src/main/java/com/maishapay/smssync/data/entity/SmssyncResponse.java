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

package com.maishapay.smssync.data.entity;

import java.util.List;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class SmssyncResponse {

    private static final long serialVersionUID = -6696308336215002660L;

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "SmssyncResponse{" +
                "payload=" + payload +
                '}';
    }

    public class Payload {

        private List<Message> messages;

        private String task;

        private String secret;

        private String error;

        private boolean success;

        public List<Message> getMessages() {
            return messages;
        }

        public String getTask() {
            return task;
        }

        public String getSecret() {
            return secret;
        }

        public String getError() {
            return error;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "messages=" + messages +
                    ", task='" + task + '\'' +
                    ", secret='" + secret + '\'' +
                    ", error='" + error + '\'' +
                    ", success=" + success +
                    '}';
        }
    }
}
