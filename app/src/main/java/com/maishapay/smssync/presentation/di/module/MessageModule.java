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

import com.addhen.android.raiburari.domain.usecase.Usecase;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.maishapay.smssync.domain.usecase.message.DeleteMessageUsecase;
import com.maishapay.smssync.domain.usecase.message.ImportMessagesUsecase;
import com.maishapay.smssync.domain.usecase.message.ListMessageUsecase;
import com.maishapay.smssync.domain.usecase.message.ListPublishedMessageUsecase;
import com.maishapay.smssync.domain.usecase.message.PublishAllMessagesUsecase;
import com.maishapay.smssync.domain.usecase.message.PublishMessageUsecase;
import com.maishapay.smssync.domain.usecase.message.UpdateMessageUsecase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@Module
public class MessageModule {

    @Provides
    @ActivityScope
    @Named("messageList")
    Usecase provideListMessageUseCase(ListMessageUsecase listMessageUsecase) {
        return listMessageUsecase;
    }

    @Provides
    @ActivityScope
    @Named("messagePublishList")
    ListPublishedMessageUsecase provideListPublishedMessageUsecase(
            ListPublishedMessageUsecase listPublishedMessageUsecase) {
        return listPublishedMessageUsecase;
    }

    @Provides
    @ActivityScope
    @Named("messagePublish")
    PublishMessageUsecase providePublishedMessageUsecase(
            PublishMessageUsecase publishMessageUsecase) {
        return publishMessageUsecase;
    }

    @Provides
    @ActivityScope
    @Named("publishAllMessages")
    Usecase providePublishedMessagesUsecase(
            PublishAllMessagesUsecase publishMessagesUsecase) {
        return publishMessagesUsecase;
    }

    @Provides
    @ActivityScope
    @Named("messageDelete")
    DeleteMessageUsecase provideDeleteMessageUsecase(DeleteMessageUsecase deleteMessageUsecase) {
        return deleteMessageUsecase;
    }

    @Provides
    @ActivityScope
    @Named("messageImport")
    Usecase provideImportMessageUsecase(ImportMessagesUsecase importMessageUsecase) {
        return importMessageUsecase;
    }

    @Provides
    @ActivityScope
    @Named("messageUpdate")
    UpdateMessageUsecase provideUpdateMessageUsecase(UpdateMessageUsecase updateMessageUsecase) {
        return updateMessageUsecase;
    }
}
