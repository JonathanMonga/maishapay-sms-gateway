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

package com.maishapay.smssync.data.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maishapay.smssync.presentation.view.ui.activity.OauthActivity;

import javax.inject.Inject;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class TwitterClient {

    static final String PREF_KEY_ACTIVE_TWITTER_SESSION = "active_twittersession";

    private static final String PREF_KEY_TWITTER_SESSION = "twittersession";

    private static SessionManager<TwitterSession> mTwitterSessionManager;

    private TwitterAuthConfig mAuthConfig;

    private Context mContext;

    private TwitterFactory mTwitterFactory;

    @Inject
    public TwitterClient(Context context, TwitterAuthConfig config) {
        mContext = context;
        mAuthConfig = config;
        mTwitterSessionManager = new PersistedSessionManager<>(
                PreferenceManager.getDefaultSharedPreferences(mContext),
                new TwitterSession.Serializer(), PREF_KEY_ACTIVE_TWITTER_SESSION,
                PREF_KEY_TWITTER_SESSION);
        initTwitterFactory();
    }

    private void initTwitterFactory() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(mAuthConfig.consumerKey);
        builder.setOAuthConsumerSecret(mAuthConfig.consumerSecret);
        mTwitterFactory = new TwitterFactory(builder.build());

    }

    /**
     */
    public SessionManager<TwitterSession> getSessionManager() {
        return mTwitterSessionManager;
    }

    public TwitterAuthConfig getAuthConfig() {
        return mAuthConfig;
    }

    /**
     * Performs a user login
     */
    public void login(Activity activity) {
        Intent intent = new Intent(activity, OauthActivity.class);
        intent.putExtra(OauthActivity.INTENT_EXTRA_TWITTER_CONFIG, getAuthConfig());
        activity.startActivity(intent);
    }

    /**
     * Logs out the user, clearing user session. This will not make a network request to invalidate
     * the session.
     */
    public void logout() {
        final SessionManager<TwitterSession> sessionManager = getSessionManager();
        if (sessionManager != null) {
            sessionManager.clearActiveSession();
        }
    }

    @Nullable
    public Status tweet(@NonNull String update) {
        if (mTwitterSessionManager != null && mTwitterSessionManager.getActiveSession() != null) {
            TwitterAuthToken authToken = mTwitterSessionManager.getActiveSession().getAuthToken();
            AccessToken accessToken = new AccessToken(authToken.token, authToken.secret);
            Twitter twitter = mTwitterFactory.getInstance(accessToken);
            StatusUpdate latestStatus = new StatusUpdate(update);
            try {
                return twitter.updateStatus(latestStatus);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
