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

import com.addhen.android.raiburari.data.entity.DataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class Filter extends DataEntity {

    private String phoneNumber;

    private Status status;

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * The status of the filtered phone number.
     */
    public enum Status {
        WHITELIST, BLACKLIST
    }

    public static List<Filter> getFilterList() {
        List<Filter> filters = new ArrayList<>();

        Filter filter1 = new Filter();
        filter1.setPhoneNumber("436");
        filter1.setStatus(Status.WHITELIST);
        filters.add(filter1);

        return filters;
    }
}
