/*
 *  Copyright (c) 2018 - 2019 Maishapay
 * All rights reserved
 * Contact: contact@maishapay.online
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
 * Maishapay developers at contact@maishapay.online.
 */

package com.maishapay.smssync.domain.usecase.filter;

import com.addhen.android.raiburari.domain.executor.PostExecutionThread;
import com.addhen.android.raiburari.domain.executor.ThreadExecutor;

import com.maishapay.smssync.domain.entity.DomainEntityFixture;
import com.maishapay.smssync.domain.entity.FilterEntity;
import com.maishapay.smssync.domain.repository.FilterRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Maishapay Team <contact@maishapay.online>
 */
public class DeleteFilterUsecaseTest {

    private DeleteFilterUsecase mDeleteFilterUsecase;

    @Mock
    private ThreadExecutor mMockThreadExecutor;

    @Mock
    private PostExecutionThread mMockPostExecutionThread;

    @Mock
    private FilterRepository mMockFilterRepository;

    @Mock
    private FilterEntity mFilterEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mDeleteFilterUsecase = new DeleteFilterUsecase(mMockFilterRepository, mMockThreadExecutor,
                mMockPostExecutionThread);
    }

    @Test
    public void shouldSuccessfullyDeleteAFilter() {
        mDeleteFilterUsecase.setFilterId(DomainEntityFixture.ID);
        mDeleteFilterUsecase.buildUseCaseObservable();
        verify(mMockFilterRepository).deleteEntity(DomainEntityFixture.ID);

        verifyNoMoreInteractions(mMockFilterRepository);
        verifyZeroInteractions(mMockPostExecutionThread);
        verifyZeroInteractions(mMockThreadExecutor);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeException() {
        assertNotNull(mDeleteFilterUsecase);
        mDeleteFilterUsecase.setFilterId(null);
        mDeleteFilterUsecase.execute(null);
    }
}
