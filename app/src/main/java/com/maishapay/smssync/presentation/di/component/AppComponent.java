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

import com.addhen.android.raiburari.presentation.di.component.ApplicationComponent;
import com.addhen.android.raiburari.presentation.di.module.ApplicationModule;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.message.MaishapayMessage;
import com.maishapay.smssync.data.message.PostMessage;
import com.maishapay.smssync.data.message.ProcessMessageResult;
import com.maishapay.smssync.data.message.TweetMessage;
import com.maishapay.smssync.data.net.AppHttpClient;
import com.maishapay.smssync.data.twitter.TwitterClient;
import com.maishapay.smssync.domain.repository.FilterRepository;
import com.maishapay.smssync.domain.repository.LogRepository;
import com.maishapay.smssync.domain.repository.MessageRepository;
import com.maishapay.smssync.domain.repository.WebServiceRepository;
import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.di.module.AppModule;
import com.maishapay.smssync.presentation.di.module.NoAnalyticsAppModule;
import com.maishapay.smssync.presentation.presenter.AlertPresenter;
import com.maishapay.smssync.presentation.presenter.DebugPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Singleton
@Component(modules = {AppModule.class, NoAnalyticsAppModule.class})
public interface AppComponent extends ApplicationComponent {

    FilterRepository filterRepository();

    MessageRepository messageRepository();

    WebServiceRepository webServiceRepository();

    LogRepository logRepository();

    FileManager fileManager();

    PrefsFactory prefsFactory();

    AppHttpClient appHttpClient();

    TwitterClient twitterClient();

    PostMessage processMessage();

    MaishapayMessage processMaishapayMessage();

    ProcessMessageResult processMessageResult();

    TweetMessage tweetMessage();

    DebugPresenter debugPresenter();

    AlertPresenter alertPresenter();

    final class Initializer {

        private Initializer() {
        } // No instances.

        public static AppComponent init(App app) {
            return DaggerAppComponent.builder()
                    .applicationModule(new ApplicationModule(app))
                    .appModule(new AppModule(app))
                    .build();
        }
    }
}