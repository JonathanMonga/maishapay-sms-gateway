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

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A factory class for creating the different datasource for {@link SyncUrl}
 *
 * @author Maishapay Team <online@maishapay.online>
 */
@Singleton
public class WebServiceDataSourceFactory {

    private final WebServiceDatabaseHelper mWebServiceDatabaseHelper;

    /**
     * Default constructor that constructs {@link WebServiceDataSourceFactory}
     *
     * @param deploymentDatabaseHelper The deployment database helper
     */
    @Inject
    WebServiceDataSourceFactory(@NonNull WebServiceDatabaseHelper deploymentDatabaseHelper) {
        mWebServiceDatabaseHelper = deploymentDatabaseHelper;
    }

    /**
     * Creates {@link WebServiceDatabaseDataSource}
     *
     * @return The deployment database source
     */
    public WebServiceDataSource createDatabaseDataSource() {
        return new WebServiceDatabaseDataSource(mWebServiceDatabaseHelper);
    }
}
