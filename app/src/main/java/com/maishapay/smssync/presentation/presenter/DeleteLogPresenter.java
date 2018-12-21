/*
 * Copyright (c) 2010 - 2015 Ushahidi Inc
 * All rights reserved
 * Contact: team@ushahidi.com
 * Website: http://www.ushahidi.com
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Ushahidi developers at team@ushahidi.com.
 */

package com.maishapay.smssync.presentation.presenter;

import android.support.annotation.NonNull;

import com.addhen.android.raiburari.domain.exception.DefaultErrorHandler;
import com.addhen.android.raiburari.domain.exception.ErrorHandler;
import com.addhen.android.raiburari.domain.usecase.DefaultSubscriber;
import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.mapper.LogModelDataMapper;
import com.maishapay.smssync.presentation.view.log.DeleteLogView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@ActivityScope
public class DeleteLogPresenter implements Presenter {

    private final Usecase mDeleteLogUsecase;

    private final LogModelDataMapper mLogModelDataMapper;

    private DeleteLogView mDeleteLogView;

    @Inject
    public DeleteLogPresenter(@Named("logDelete") Usecase deleteLogUsecase,
                              LogModelDataMapper logModelDataMapper) {
        mDeleteLogUsecase = deleteLogUsecase;
        mLogModelDataMapper = logModelDataMapper;
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
        mDeleteLogUsecase.unsubscribe();
    }

    public void setView(@NonNull DeleteLogView deleteLogView) {
        mDeleteLogView = deleteLogView;
    }

    public void deleteLogs() {
        mDeleteLogUsecase.execute(new DefaultSubscriber<Long>() {

            @Override
            public void onNext(Long row) {
                mDeleteLogView.onDeleted(row);
            }

            @Override
            public void onError(Throwable e) {
                showErrorMessage(new DefaultErrorHandler((Exception) e));
            }
        });
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mDeleteLogView.getAppContext(),
                errorHandler.getException());
        mDeleteLogView.showError(errorMessage);
    }
}