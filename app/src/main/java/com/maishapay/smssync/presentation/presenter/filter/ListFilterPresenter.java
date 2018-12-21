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

package com.maishapay.smssync.presentation.presenter.filter;

import android.support.annotation.NonNull;

import com.addhen.android.raiburari.domain.exception.DefaultErrorHandler;
import com.addhen.android.raiburari.domain.exception.ErrorHandler;
import com.addhen.android.raiburari.domain.usecase.DefaultSubscriber;
import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.maishapay.smssync.domain.entity.FilterEntity;
import com.maishapay.smssync.domain.entity.WebServiceEntity;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.mapper.FilterModelDataMapper;
import com.maishapay.smssync.presentation.model.mapper.WebServiceModelDataMapper;
import com.maishapay.smssync.presentation.view.filter.ListFilterView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@ActivityScope
public class ListFilterPresenter implements Presenter {

    private final Usecase mListFiltersUsecase;

    private final Usecase mGetActiveWebServiceUsecase;

    private final FilterModelDataMapper mFilterModelDataMapper;

    private final WebServiceModelDataMapper mWebServiceModelDataMapper;

    private ListFilterView mListFilterView;

    @Inject
    public ListFilterPresenter(@Named("filterList") Usecase listUsecase,
                               @Named("getActiveWebService") Usecase getActiveWebServiceUsecase,
                               FilterModelDataMapper filterModelDataMapper,
                               WebServiceModelDataMapper webServiceModelDataMapper) {
        mListFiltersUsecase = listUsecase;
        mGetActiveWebServiceUsecase = getActiveWebServiceUsecase;
        mWebServiceModelDataMapper = webServiceModelDataMapper;
        mFilterModelDataMapper = filterModelDataMapper;
    }

    @Override
    public void resume() {
        loadActiveWebService();
        loadFilters();
    }

    @Override
    public void pause() {
        // Do nothing
    }

    @Override
    public void destroy() {
        mListFiltersUsecase.unsubscribe();
        mGetActiveWebServiceUsecase.unsubscribe();
    }

    public void setView(@NonNull ListFilterView listFilterView) {
        mListFilterView = listFilterView;
    }

    private void loadActiveWebService() {
        mGetActiveWebServiceUsecase.execute(new DefaultSubscriber<List<WebServiceEntity>>() {
            @Override
            public void onNext(List<WebServiceEntity> webServiceList) {
                mListFilterView
                        .showCustomWebService(mWebServiceModelDataMapper.map(webServiceList));
            }

            @Override
            public void onError(Throwable e) {
                showErrorMessage(new DefaultErrorHandler((Exception) e));
            }
        });
    }

    public void loadFilters() {
        mListFiltersUsecase.execute(new DefaultSubscriber<List<FilterEntity>>() {
            @Override
            public void onNext(List<FilterEntity> filterList) {
                mListFilterView.showFilters(mFilterModelDataMapper.map(filterList));
            }

            @Override
            public void onError(Throwable e) {
                showErrorMessage(new DefaultErrorHandler((Exception) e));
            }
        });
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mListFilterView.getAppContext(),
                errorHandler.getException());
        mListFilterView.showError(errorMessage);
    }
}
