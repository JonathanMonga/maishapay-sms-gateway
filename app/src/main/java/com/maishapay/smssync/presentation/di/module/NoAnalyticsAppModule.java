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

package com.maishapay.smssync.presentation.di.module;

import com.maishapay.smssync.data.repository.FilterDataRepository;
import com.maishapay.smssync.data.repository.LogDataRepository;
import com.maishapay.smssync.data.repository.MessageDataRepository;
import com.maishapay.smssync.data.repository.WebServiceDataRepository;
import com.maishapay.smssync.domain.repository.FilterRepository;
import com.maishapay.smssync.domain.repository.LogRepository;
import com.maishapay.smssync.domain.repository.MessageRepository;
import com.maishapay.smssync.domain.repository.WebServiceRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@Module
public class NoAnalyticsAppModule {

    @Provides
    @Singleton
    MessageRepository provideMessageRepository(
            MessageDataRepository messageDataRepository) {
        return messageDataRepository;
    }

    @Provides
    @Singleton
    WebServiceRepository providesInternalWebServiceRepository(
            WebServiceDataRepository webServiceRepository) {
        return webServiceRepository;
    }

    @Provides
    @Singleton
    FilterRepository provideFilterRepository(
            FilterDataRepository filterDataRepository) {
        return filterDataRepository;
    }

    @Provides
    @Singleton
    LogRepository provideLogRepository(LogDataRepository logDataRepository) {
        return logDataRepository;
    }
}
