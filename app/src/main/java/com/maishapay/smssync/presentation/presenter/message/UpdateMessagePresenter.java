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
import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.maishapay.smssync.domain.usecase.message.UpdateMessageUsecase;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.MessageModel;
import com.maishapay.smssync.presentation.model.mapper.MessageModelDataMapper;
import com.maishapay.smssync.presentation.view.message.UpdateMessageView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class UpdateMessagePresenter implements Presenter {

    private final UpdateMessageUsecase mUpdateMessageUsecase;

    private final MessageModelDataMapper mMessageModelDataMapper;

    private UpdateMessageView mUpdateMessageView;

    /**
     * Default use case.
     *
     * @param updateMessageUsecase   The update deployment use case
     * @param messageModelDataMapper The deployment model data mapper
     */
    @Inject
    public UpdateMessagePresenter(
            @Named("messageUpdate") UpdateMessageUsecase updateMessageUsecase,
            MessageModelDataMapper messageModelDataMapper) {
        mUpdateMessageUsecase = updateMessageUsecase;
        mMessageModelDataMapper = messageModelDataMapper;
    }

    public void setUpdateMessageView(@NonNull UpdateMessageView updateMessageView) {
        mUpdateMessageView = updateMessageView;
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
        mUpdateMessageUsecase.unsubscribe();
    }

    /**
     * Updates {@link MessageModel}
     *
     * @param messageModel The deployment model to be updated
     */
    public void updateMessage(MessageModel messageModel) {
        mUpdateMessageUsecase.setMessageEntity(
                mMessageModelDataMapper.map(messageModel));
        mUpdateMessageUsecase.execute(new DefaultSubscriber<Long>() {
            @Override
            public void onCompleted() {
                // Do nothing
                mUpdateMessageView.showLoading();
            }

            @Override
            public void onError(Throwable e) {
                // Do nothing
                mUpdateMessageView.hideLoading();
                showErrorMessage(new DefaultErrorHandler((Exception) e));
                mUpdateMessageView.showRetry();
            }

            @Override
            public void onNext(Long row) {
                // Do nothing
                mUpdateMessageView.onMessageUpdated();
            }
        });
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mUpdateMessageView.getAppContext(),
                errorHandler.getException());
        mUpdateMessageView.showError(errorMessage);
    }

}