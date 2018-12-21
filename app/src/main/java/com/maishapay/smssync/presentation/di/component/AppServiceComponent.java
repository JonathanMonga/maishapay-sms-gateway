/*
 * Copyright (c) 2010 - 2015 Ushahidi Inc
 * All rights reserved
 * Contact: team@ushahidi.com
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
 * Ushahidi developers at team@ushahidi.com.
 */

package com.maishapay.smssync.presentation.di.component;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */

import com.maishapay.smssync.presentation.di.module.ServiceModule;
import com.maishapay.smssync.presentation.di.qualifier.ServiceScope;
import com.maishapay.smssync.presentation.presenter.message.DeleteMessagePresenter;
import com.maishapay.smssync.presentation.presenter.message.PublishMessagePresenter;
import com.maishapay.smssync.presentation.presenter.message.UpdateMessagePresenter;
import com.maishapay.smssync.presentation.service.AutoSyncScheduledService;
import com.maishapay.smssync.presentation.service.BaseWakefulIntentService;
import com.maishapay.smssync.presentation.service.CheckTaskService;
import com.maishapay.smssync.presentation.service.DeleteMessageService;
import com.maishapay.smssync.presentation.service.MessageResultsService;
import com.maishapay.smssync.presentation.service.SmsReceiverService;
import com.maishapay.smssync.presentation.service.SyncPendingMessagesService;
import com.maishapay.smssync.presentation.service.UpdateMessageService;

import dagger.Component;

@ServiceScope
@Component(dependencies = {AppComponent.class}, modules = {ServiceModule.class})
public interface AppServiceComponent {

    void inject(SmsReceiverService smsReceiverService);

    void inject(BaseWakefulIntentService baseWakefulIntentService);

    void inject(CheckTaskService baseWakefulIntentService);

    void inject(DeleteMessageService deleteMessageService);

    void inject(MessageResultsService messageResultsService);

    void inject(UpdateMessageService updateMessageService);

    void inject(SyncPendingMessagesService syncPendingMessagesService);

    void inject(AutoSyncScheduledService autoSyncScheduledService);

    UpdateMessagePresenter updateMessagePresenter();

    DeleteMessagePresenter deleteMessagePresenter();

    PublishMessagePresenter publishMessagePresenter();
}
