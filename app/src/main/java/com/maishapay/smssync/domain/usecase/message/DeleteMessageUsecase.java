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
import com.maishapay.smssync.domain.repository.MessageRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Delete message use case
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class DeleteMessageUsecase extends Usecase {

    private final MessageRepository mMessageRepository;

    private String mMessageUuid;

    /**
     * Default constructor
     *
     * @param messageRepository   The deployment repository
     * @param threadExecutor      The thread executor
     * @param postExecutionThread The post execution thread
     */
    @Inject
    protected DeleteMessageUsecase(@NonNull MessageRepository messageRepository,
                                   @NonNull ThreadExecutor threadExecutor,
                                   @NonNull PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mMessageRepository = messageRepository;
    }

    /**
     * Sets message Id
     *
     * @param messageUuid The Id of the message
     */
    public void setMessageUuid(String messageUuid) {
        mMessageUuid = messageUuid;
    }

    @Override
    protected Observable<Integer> buildUseCaseObservable() {
        if (mMessageUuid == null) {
            throw new RuntimeException("MessageId is null. You must call setMessageUuid(...)");
        }
        return mMessageRepository.deleteByUuid(mMessageUuid);
    }
}
