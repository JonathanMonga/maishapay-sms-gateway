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

package com.maishapay.smssync.domain.usecase.filter;

import com.addhen.android.raiburari.domain.executor.PostExecutionThread;
import com.addhen.android.raiburari.domain.executor.ThreadExecutor;
import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.maishapay.smssync.domain.repository.FilterRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class DeleteFilterUsecase extends Usecase {

    private final FilterRepository mFilterRepository;

    private Long mFilterId;

    @Inject
    protected DeleteFilterUsecase(FilterRepository filterRepository, ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mFilterRepository = filterRepository;
    }

    public void setFilterId(Long filterId) {
        mFilterId = filterId;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (mFilterId == null) {
            throw new RuntimeException("FilterId is null. You must call setFilterId(...)");
        }
        return mFilterRepository.deleteEntity(mFilterId);
    }
}
