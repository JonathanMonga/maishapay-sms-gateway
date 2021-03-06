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

package com.maishapay.smssync.data.repository.datasource.webservice;


import android.support.annotation.NonNull;

import com.maishapay.smssync.data.database.WebServiceDatabaseHelper;
import com.maishapay.smssync.data.entity.SyncUrl;

import java.util.List;

import rx.Observable;

/**
 * Retrieves and adds a webService data to the database
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class WebServiceDatabaseDataSource implements WebServiceDataSource {

    private final WebServiceDatabaseHelper mWebServiceDatabaseHelper;

    /**
     * Default constructor
     *
     * @param webServiceDatabaseHelper The webService database helper
     */
    public WebServiceDatabaseDataSource(
            @NonNull WebServiceDatabaseHelper webServiceDatabaseHelper) {
        mWebServiceDatabaseHelper = webServiceDatabaseHelper;
    }

    @Override
    public Observable<List<SyncUrl>> getWebServiceList() {
        return mWebServiceDatabaseHelper.getWebServices();
    }

    @Override
    public Observable<SyncUrl> getWebService(Long webServiceId) {
        return mWebServiceDatabaseHelper.getWebService(webServiceId);
    }

    @Override
    public Observable<List<SyncUrl>> getByStatus(SyncUrl.Status status) {
        return mWebServiceDatabaseHelper.getByStatus(status);
    }

    @Override
    public List<SyncUrl> syncGetByStatus(SyncUrl.Status status) {
        return mWebServiceDatabaseHelper.get(status);
    }

    @Override
    public Observable<Long> addWebService(SyncUrl syncUrl) {
        return mWebServiceDatabaseHelper.put(syncUrl);
    }

    @Override
    public Observable<Long> updateWebService(SyncUrl syncUrl) {
        return mWebServiceDatabaseHelper.put(syncUrl);
    }

    @Override
    public Observable<Long> deleteWebService(Long webServiceId) {
        return mWebServiceDatabaseHelper.deleteWebService(webServiceId);
    }

    @Override
    public List<SyncUrl> get(SyncUrl.Status status) {
        return mWebServiceDatabaseHelper.get(status);
    }

    @Override
    public List<SyncUrl> listWebServices() {
        return mWebServiceDatabaseHelper.listWebServices();
    }

}
