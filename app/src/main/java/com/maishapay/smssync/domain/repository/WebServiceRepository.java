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

package com.maishapay.smssync.domain.repository;

import com.addhen.android.raiburari.domain.repository.Repository;
import com.maishapay.smssync.domain.entity.WebServiceEntity;

import java.util.List;

import rx.Observable;

/**
 * Repository for manipulating {@link com.maishapay.smssync.domain.entity.WebServiceEntity} data
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface WebServiceRepository extends Repository<WebServiceEntity> {

    /**
     * Get an {@link WebServiceEntity} by its status.
     *
     * @param status The web service status to be used for retrieving web service data.
     * @return The web service
     */
    Observable<List<WebServiceEntity>> getByStatus(WebServiceEntity.Status status);

    /**
     * Synchronously fetches a web service by its status
     *
     * @param status The status of the web service
     * @return A list of {@link WebServiceEntity}
     */
    List<WebServiceEntity> syncGetByStatus(WebServiceEntity.Status status);

    /**
     * Use to test web services. Does not return an Observable
     *
     * @param webServiceEntity The URL to test it's connection
     * @return The status of the connection test. True if it was successful, False otherwise.
     */
    Observable<Boolean> testWebService(WebServiceEntity webServiceEntity);
}
