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

package com.maishapay.smssync.data.repository;

import com.maishapay.smssync.data.entity.mapper.WebServiceDataMapper;
import com.maishapay.smssync.data.net.AppHttpClient;
import com.maishapay.smssync.data.repository.datasource.webservice.WebServiceDataSource;
import com.maishapay.smssync.data.repository.datasource.webservice.WebServiceDataSourceFactory;
import com.maishapay.smssync.domain.entity.WebServiceEntity;
import com.maishapay.smssync.domain.repository.WebServiceRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Implementation of {@link WebServiceRepository} for manipulating webservice data
 *
 * @author Maishapay Team <online@maishapay.online>
 */
@Singleton
public class WebServiceDataRepository implements WebServiceRepository {

    private final WebServiceDataSourceFactory mWebServiceDataStoreFactory;

    private final WebServiceDataMapper mWebServiceEntityDataMapper;

    private final AppHttpClient mAppHttpClient;

    /**
     * Constructs a {@link WebServiceRepository}.
     *
     * @param dataSourceFactory          A factory to construct different data source
     *                                   implementations.
     * @param webserviceEntityDataMapper {@link WebServiceDataMapper}.
     */
    @Inject
    public WebServiceDataRepository(WebServiceDataSourceFactory dataSourceFactory,
                                    WebServiceDataMapper webserviceEntityDataMapper, AppHttpClient appHttpClient) {
        mWebServiceDataStoreFactory = dataSourceFactory;
        mWebServiceEntityDataMapper = webserviceEntityDataMapper;
        mAppHttpClient = appHttpClient;
    }

    @Override
    public Observable<List<WebServiceEntity>> getByStatus(WebServiceEntity.Status status) {
        final WebServiceDataSource webserviceDataSource = mWebServiceDataStoreFactory
                .createDatabaseDataSource();
        return webserviceDataSource.getByStatus(mWebServiceEntityDataMapper.map(status))
                .map(mWebServiceEntityDataMapper::map);
    }

    @Override
    public List<WebServiceEntity> syncGetByStatus(WebServiceEntity.Status status) {
        final WebServiceDataSource webserviceDataSource = mWebServiceDataStoreFactory
                .createDatabaseDataSource();
        return mWebServiceEntityDataMapper.map(
                webserviceDataSource.syncGetByStatus(mWebServiceEntityDataMapper.map(status)));
    }

    @Override
    public Observable<Boolean> testWebService(WebServiceEntity webServiceEntity) {
        return mAppHttpClient.makeRequest(mWebServiceEntityDataMapper.map(webServiceEntity));
    }

    @Override
    public Observable<List<WebServiceEntity>> getEntities() {
        final WebServiceDataSource webserviceDataSource = mWebServiceDataStoreFactory
                .createDatabaseDataSource();
        return webserviceDataSource.getWebServiceList().map(mWebServiceEntityDataMapper::map
        );
    }

    @Override
    public Observable<WebServiceEntity> getEntity(Long id) {
        final WebServiceDataSource webserviceDataSource = mWebServiceDataStoreFactory
                .createDatabaseDataSource();
        return webserviceDataSource.getWebService(id)
                .map(mWebServiceEntityDataMapper::map);
    }

    @Override
    public Observable<Long> addEntity(WebServiceEntity webservice) {
        final WebServiceDataSource webserviceDataSource = mWebServiceDataStoreFactory
                .createDatabaseDataSource();
        return webserviceDataSource.addWebService(mWebServiceEntityDataMapper.map(webservice));
    }

    @Override
    public Observable<Long> updateEntity(WebServiceEntity webservice) {
        final WebServiceDataSource webserviceDataSource = mWebServiceDataStoreFactory
                .createDatabaseDataSource();
        return webserviceDataSource
                .updateWebService(mWebServiceEntityDataMapper.map(webservice));
    }

    @Override
    public Observable<Long> deleteEntity(Long webserviceId) {
        final WebServiceDataSource webserviceDataSource = mWebServiceDataStoreFactory
                .createDatabaseDataSource();
        return webserviceDataSource.deleteWebService(webserviceId);
    }
}
