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

package com.maishapay.smssync.domain.usecase.message;

import android.support.annotation.NonNull;

import com.addhen.android.raiburari.domain.executor.PostExecutionThread;
import com.addhen.android.raiburari.domain.executor.ThreadExecutor;
import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.maishapay.smssync.domain.entity.MessageEntity;
import com.maishapay.smssync.domain.repository.MessageRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Publishes {@link com.maishapay.smssync.domain.entity.MessageEntity} to a configured {@link
 * com.maishapay.smssync.domain.entity.WebServiceEntity}
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class PublishMessageUsecase extends Usecase {

    private final MessageRepository mMessageRepository;

    public MessageEntity mMessageEntity;

    @Inject
    protected PublishMessageUsecase(@NonNull MessageRepository messageRepository,
                                    @NonNull ThreadExecutor threadExecutor,
                                    @NonNull PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mMessageRepository = messageRepository;
    }

    public void setMessageEntity(MessageEntity messageEntity) {
        mMessageEntity = messageEntity;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (mMessageEntity == null) {
            throw new RuntimeException(
                    "MessageEntity is null. You must call setMessageEntity(...)");
        }
        return mMessageRepository.publishMessage(mMessageEntity);
    }

}
