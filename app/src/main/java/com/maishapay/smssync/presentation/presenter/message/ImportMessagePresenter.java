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

package com.maishapay.smssync.presentation.presenter.message;

import android.support.annotation.NonNull;

import com.addhen.android.raiburari.domain.exception.DefaultErrorHandler;
import com.addhen.android.raiburari.domain.exception.ErrorHandler;
import com.addhen.android.raiburari.domain.usecase.DefaultSubscriber;
import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.maishapay.smssync.domain.entity.MessageEntity;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.mapper.MessageModelDataMapper;
import com.maishapay.smssync.presentation.view.message.ImportMessageView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@ActivityScope
public class ImportMessagePresenter implements Presenter {

    private final Usecase mImportMessageUsecase;

    private final MessageModelDataMapper mMessageModelDataMapper;

    private ImportMessageView mImportMessageView;

    @Inject
    public ImportMessagePresenter(@Named("messageImport") Usecase importMessageUsecase,
                                  MessageModelDataMapper messageModelDataMapper) {
        mImportMessageUsecase = importMessageUsecase;
        mMessageModelDataMapper = messageModelDataMapper;
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
        mImportMessageUsecase.unsubscribe();
    }

    public void setView(@NonNull ImportMessageView importMessageView) {
        mImportMessageView = importMessageView;
    }

    public void importMessages() {
        mImportMessageView.hideRetry();
        mImportMessageView.showLoading();
        mImportMessageUsecase.execute(new DefaultSubscriber<List<MessageEntity>>() {
            @Override
            public void onCompleted() {
                mImportMessageView.hideLoading();
            }

            @Override
            public void onNext(List<MessageEntity> messageList) {
                mImportMessageView.hideLoading();
                mImportMessageView.showMessages(mMessageModelDataMapper.map(messageList));
            }

            @Override
            public void onError(Throwable e) {
                mImportMessageView.hideLoading();
                showErrorMessage(new DefaultErrorHandler((Exception) e));
                mImportMessageView.showRetry();
            }
        });
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mImportMessageView.getAppContext(),
                errorHandler.getException());
        mImportMessageView.showError(errorMessage);
    }
}
