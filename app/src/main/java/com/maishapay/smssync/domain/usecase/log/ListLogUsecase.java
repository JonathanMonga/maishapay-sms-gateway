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

package com.maishapay.smssync.domain.usecase.log;

import android.support.annotation.NonNull;

import com.addhen.android.raiburari.domain.executor.PostExecutionThread;
import com.addhen.android.raiburari.domain.executor.ThreadExecutor;
import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.maishapay.smssync.domain.entity.LogEntity;
import com.maishapay.smssync.domain.repository.LogRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class ListLogUsecase extends Usecase {

    private final LogRepository mLogRepository;

    @Inject
    public ListLogUsecase(@NonNull LogRepository logRepository,
                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mLogRepository = logRepository;
    }

    @Override
    protected Observable<List<LogEntity>> buildUseCaseObservable() {
        return mLogRepository.getLogs();
    }
}
