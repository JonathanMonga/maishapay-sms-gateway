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

package com.maishapay.smssync.presentation.task;

import android.content.Intent;

import com.maishapay.smssync.R;

/**
 * The synchronization types
 *
 * @author Henry Addo
 */
public enum SyncType {
    UNKNOWN(R.string.unknown),
    MANUAL(R.string.manual);

    public static final String EXTRA = "com.maishapay.smssync.SyncTypeAsString";

    public final int resId;

    SyncType(int resId) {
        this.resId = resId;
    }

    public static SyncType fromIntent(Intent intent) {
        if (intent.hasExtra(EXTRA)) {
            final String name = intent.getStringExtra(EXTRA);
            for (SyncType type : values()) {
                if (type.name().equals(name)) {
                    return type;
                }
            }
        }
        return UNKNOWN;
    }

    public boolean isBackground() {
        return this != MANUAL;
    }
}
