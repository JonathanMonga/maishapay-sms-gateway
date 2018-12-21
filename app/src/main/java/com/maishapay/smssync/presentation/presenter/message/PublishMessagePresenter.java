/*
 * Copyright (c) 2010 - 2017 Ushahidi Inc
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
import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.domain.usecase.message.PublishMessageUsecase;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.MessageModel;
import com.maishapay.smssync.presentation.model.mapper.MessageModelDataMapper;
import com.maishapay.smssync.presentation.view.message.PublishMessageView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class PublishMessagePresenter implements Presenter {

    private final PublishMessageUsecase mPublishMessageUsecase;

    private final MessageModelDataMapper mMessageModelDataMapper;

    private PublishMessageView mPublishMessageView;

    private PrefsFactory mPrefsFactory;

    @Inject
    public PublishMessagePresenter(
            @Named("messagePublish") PublishMessageUsecase publishMessageUsecase,
            MessageModelDataMapper messageModelDataMapper,
            PrefsFactory prefsFactory) {
        mPublishMessageUsecase = publishMessageUsecase;
        mMessageModelDataMapper = messageModelDataMapper;
        mPrefsFactory = prefsFactory;
    }

    public void setView(@NonNull PublishMessageView publishMessageView) {
        mPublishMessageView = publishMessageView;
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
        mPublishMessageUsecase.unsubscribe();
    }

    public void publishMessage(MessageModel messageModels) {
        if (!mPrefsFactory.serviceEnabled().get()) {
            mPublishMessageView.showEnableServiceMessage(
                    mPublishMessageView.getAppContext().getString(R.string.smssync_not_enabled));
            return;
        }
        mPublishMessageUsecase.setMessageEntity(mMessageModelDataMapper.map(messageModels));
        mPublishMessageUsecase.execute(new PublishMessageSubscriber());
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mPublishMessageView.getAppContext(),
                errorHandler.getException());
        mPublishMessageView.showError(errorMessage);
    }

    private class PublishMessageSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onStart() {
            mPublishMessageView.hideRetry();
            mPublishMessageView.showLoading();
        }

        @Override
        public void onCompleted() {
            mPublishMessageView.hideLoading();
        }

        @Override
        public void onNext(Boolean status) {
            mPublishMessageView.hideLoading();
            mPublishMessageView.successfullyPublished(status);
        }

        @Override
        public void onError(Throwable e) {
            mPublishMessageView.hideLoading();
            showErrorMessage(new DefaultErrorHandler((Exception) e));
            mPublishMessageView.showRetry();
        }
    }
}
