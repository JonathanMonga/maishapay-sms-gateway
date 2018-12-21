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

import com.addhen.android.raiburari.domain.exception.DefaultErrorHandler;
import com.addhen.android.raiburari.domain.exception.ErrorHandler;
import com.addhen.android.raiburari.domain.usecase.DefaultSubscriber;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.maishapay.smssync.domain.usecase.filter.AddFilterUsecase;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.FilterModel;
import com.maishapay.smssync.presentation.model.mapper.FilterModelDataMapper;
import com.maishapay.smssync.presentation.view.filter.AddFilterView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@ActivityScope
public class AddFilterPresenter implements Presenter {

    private final AddFilterUsecase mAddFilterUsecase;

    private final FilterModelDataMapper mFilterModelDataMapper;

    private AddFilterView mAddFilterView;

    @Inject
    public AddFilterPresenter(@Named("filterAdd") AddFilterUsecase addFilterUsecase,
                              FilterModelDataMapper filterModelDataMapper) {
        mAddFilterUsecase = addFilterUsecase;
        mFilterModelDataMapper = filterModelDataMapper;
    }

    public void setView(AddFilterView addFilterView) {
        mAddFilterView = addFilterView;
    }

    @Override
    public void resume() {
        // Do nothing
    }

    @Override
    public void pause() {
        // Do nothing
    }

    @Override
    public void destroy() {
        mAddFilterUsecase.unsubscribe();
    }


    public void addFilter(FilterModel filterModel) {
        mAddFilterUsecase.setFilter(mFilterModelDataMapper.map(filterModel));
        mAddFilterUsecase.execute(new DefaultSubscriber<Long>() {
            @Override
            public void onCompleted() {
                // Do nothing
            }

            @Override
            public void onNext(Long row) {
                mAddFilterView.onAdded(row);
            }

            @Override
            public void onError(Throwable e) {
                showErrorMessage(new DefaultErrorHandler((Exception) e));
            }
        });
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mAddFilterView.getAppContext(),
                errorHandler.getException());
        mAddFilterView.showError(errorMessage);
    }
}