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

package com.maishapay.smssync.presentation.view.ui.activity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.addhen.android.raiburari.presentation.di.HasComponent;
import com.addhen.android.raiburari.presentation.ui.activity.BaseActivity;
import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.di.component.AppActivityComponent;
import com.maishapay.smssync.presentation.di.component.AppComponent;
import com.maishapay.smssync.presentation.di.component.DaggerAppActivityComponent;
import com.maishapay.smssync.presentation.di.component.DaggerFilterComponent;
import com.maishapay.smssync.presentation.di.component.DaggerIntegrationComponent;
import com.maishapay.smssync.presentation.di.component.DaggerLogComponent;
import com.maishapay.smssync.presentation.di.component.DaggerMessageComponent;
import com.maishapay.smssync.presentation.di.component.FilterComponent;
import com.maishapay.smssync.presentation.di.component.IntegrationComponent;
import com.maishapay.smssync.presentation.di.component.LogComponent;
import com.maishapay.smssync.presentation.di.component.MessageComponent;
import com.maishapay.smssync.presentation.receiver.AutoSyncScheduledReceiver;
import com.maishapay.smssync.presentation.receiver.SmsReceiver;
import com.maishapay.smssync.presentation.service.Scheduler;
import com.maishapay.smssync.presentation.service.ServiceConstants;
import com.maishapay.smssync.presentation.util.TimeFrequencyUtil;
import com.maishapay.smssync.presentation.util.Utility;
import com.maishapay.smssync.presentation.view.ui.fragment.FilterFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.IntegrationFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.LogFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.MessageFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.PublishedMessageFragment;

import butterknife.BindView;


/**
 * @author Henry Addo
 */
public class MainActivity extends BaseActivity implements HasComponent<AppActivityComponent> {

    public static final String INTEGRATION_TAG = "integration";

    public static final String LOGS_TAG = "logs";

    public static final String FILTERS_TAG = "filters";

    private static final String PUBLISHED_MESSAGES_TAG = "published_messages";

    private static final String BUNDLE_STATE_PARAM_CURRENT_MENU
            = "com.maishapay.smssync.presentation.view.ui.activity.BUNDLE_STATE_PARAM_CURRENT_MENU";

    private static final String BUNDLE_STATE_PARAM_CURRENT_MENU_TITLE
            = "com.maishapay.smssync.presentation.view.ui.activity.BUNDLE_STATE_PARAM_CURRENT_MENU_TITLE";

    private static final String INCOMING_FAG_TAG = "incoming_messages";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView((R.id.nav_view))
    NavigationView mNavigationView;

    private AppCompatTextView mAppCompatTextView;

    private AppActivityComponent mAppComponent;

    private MessageComponent mMessageComponent;

    private LogComponent mLogComponent;

    private FilterComponent mFilterComponent;

    private IntegrationComponent mIntegrationComponent;

    private ActionBarDrawerToggle mDrawerToggle;

    private int mCurrentMenu = R.id.nav_incoming_messages;

    private String mCurrentMenuTitle;

    private SearchView mSearchView = null;

    private MenuItem mSearchItem;

    private String mQuery = "";

    private MessageFragment mMessageFragment;

    public MainActivity() {
        super(R.layout.activity_main, R.menu.menu_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentMenu = savedInstanceState.getInt(BUNDLE_STATE_PARAM_CURRENT_MENU, mCurrentMenu);
            mCurrentMenuTitle = savedInstanceState.getString(BUNDLE_STATE_PARAM_CURRENT_MENU_TITLE);
        } else {
            mCurrentMenuTitle = getString(R.string.nav_menu_incoming);
        }
        injector();
        initViews();
        setupFragment(mNavigationView.getMenu().findItem(mCurrentMenu));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(BUNDLE_STATE_PARAM_CURRENT_MENU, mCurrentMenu);
        savedInstanceState.putString(BUNDLE_STATE_PARAM_CURRENT_MENU_TITLE, mCurrentMenuTitle);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        setToolbarTitle(mNavigationView.getMenu().findItem(mCurrentMenu));
    }


    public void onDestroy() {
        super.onDestroy();
        mMessageFragment = null;
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (mNavigationView != null) {
            View headerLayout = getLayoutInflater().inflate(R.layout.navdrawer_header, null);
            mNavigationView.addHeaderView(headerLayout);
            mAppCompatTextView = (AppCompatTextView) headerLayout
                    .findViewById(R.id.header_app_version);
            ViewGroup headerContainer = (ViewGroup) headerLayout.findViewById(
                    R.id.nav_header_container);
            headerContainer.setOnClickListener(v -> {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            });
            mAppCompatTextView.setText(getAppVersionName());
            setupDrawerContent();
        }
        setupFragment(mNavigationView.getMenu().findItem(mCurrentMenu));
        handleSearchIntent(getIntent());
    }

    private String getAppVersionName() {
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "v" + versionName;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleSearchIntent(intent);
    }

    private void handleSearchIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            query = query == null ? "" : query;
            mQuery = query;
            performQuery(query);
            if (mSearchView != null) {
                mSearchView.setQuery(query, false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mSearchItem = menu.findItem(R.id.menu_search_message);
        if (mSearchItem != null) {
            mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
            initSearchView();
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // Only show search icon for incoming messages fragment
        if (mNavigationView.getMenu().findItem(R.id.nav_incoming_messages).isChecked()) {
            mSearchItem.setVisible(true);
            mSearchView.setVisibility(View.VISIBLE);
        } else {
            mSearchItem.setVisible(false);
            mSearchView.setVisibility(View.GONE);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    private void initSearchView() {
        final SearchManager searchManager = (SearchManager) getSystemService(
                Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.clearFocus();
                performQuery(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                performQuery(s);
                return true;
            }
        });

        mSearchView.setOnCloseListener(() -> {
            reloadMessages();
            return false;
        });

        if (!TextUtils.isEmpty(mQuery)) {
            mSearchView.setQuery(mQuery, false);
        }
        SearchView.SearchAutoComplete searchAutoComplete
                = (SearchView.SearchAutoComplete) mSearchView
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
        searchAutoComplete.setTextSize(14);
    }

    private void reloadMessages() {
        if (mMessageFragment != null) {
            mMessageFragment.reloadMessages();
        }
    }

    private void performQuery(String query) {
        if (mMessageFragment != null) {
            mMessageFragment.requestQuery(query);
        }
    }

    private void injector() {
        mAppComponent = DaggerAppActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        // Launch getting started screen only when app is launch for the first time
        PrefsFactory prefsFactory = getAppComponent().prefsFactory();
        if (prefsFactory.isFirstTimeLaunched().get()) {
            prefsFactory.isFirstTimeLaunched().set(false);
            Utility.makeDefaultSmsApp(this);
            startSyncServices(prefsFactory);
            mAppComponent.launcher().launchGettingStarted();
        }

        mMessageComponent = DaggerMessageComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();

        mLogComponent = DaggerLogComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();

        mFilterComponent = DaggerFilterComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();

        mIntegrationComponent = DaggerIntegrationComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.settings:
                mAppComponent.launcher().launchSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent() {
        PrefsFactory prefs = getAppComponent().prefsFactory();
        MenuItem phoneName = mNavigationView.getMenu().findItem(R.id.nav_device_name);
        if (phoneName != null && !TextUtils.isEmpty(prefs.uniqueName().get())) {
            phoneName.setVisible(true);
            phoneName.setTitle(
                    Utility.capitalizeFirstLetter(prefs.uniqueName().get()) + " - " + prefs
                            .uniqueId().get());
        }
        mNavigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    setupFragment(menuItem);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }

    private void setToolbarTitle(MenuItem menuItem) {
        mCurrentMenu = menuItem.getItemId();
        mCurrentMenuTitle = menuItem.getTitle().toString();
        mToolbar.setTitle(mCurrentMenuTitle);
        onNavigationItemSelected(menuItem);
    }

    private void onNavigationItemSelected(final MenuItem menuItem) {
        final int groupId = menuItem.getGroupId();
        mNavigationView.getMenu()
                .setGroupCheckable(R.id.group_messages, (groupId == R.id.group_messages), true);
        menuItem.setChecked(true);
    }

    public void launchIntegration() {
        final MenuItem menuItem = mNavigationView.getMenu().findItem(R.id.nav_integration);
        menuItem.setChecked(true);
        setToolbarTitle(menuItem);
        replaceFragment(R.id.fragment_main_content, IntegrationFragment.newInstance(),
                INTEGRATION_TAG);
    }

    private void setupFragment(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_incoming_messages:
                mMessageFragment = MessageFragment.newInstance();
                replaceFragment(R.id.fragment_main_content, mMessageFragment,
                        INCOMING_FAG_TAG);
                setToolbarTitle(menuItem);
                break;
            case R.id.nav_published_messages:
                replaceFragment(R.id.fragment_main_content,
                        PublishedMessageFragment.newInstance(), PUBLISHED_MESSAGES_TAG);
                setToolbarTitle(menuItem);
                break;
            case R.id.nav_filters:
                replaceFragment(R.id.fragment_main_content,
                        FilterFragment.newInstance(), FILTERS_TAG);
                setToolbarTitle(menuItem);
                break;
            case R.id.nav_reports:
                replaceFragment(R.id.fragment_main_content, LogFragment.newInstance(), LOGS_TAG);
                setToolbarTitle(menuItem);
                break;
            case R.id.nav_integration:
                replaceFragment(R.id.fragment_main_content, IntegrationFragment.newInstance(),
                        INTEGRATION_TAG);
                setToolbarTitle(menuItem);
                break;
            case R.id.nav_settings:
                menuItem.setChecked(false);
                mAppComponent.launcher().launchSettings();
                break;
            default:
                break;
        }
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public AppActivityComponent getComponent() {
        return mAppComponent;
    }

    /**
     * Gets the Main Application component for dependency injection.
     *
     * @return {@link com.addhen.android.raiburari.presentation.di.component.ApplicationComponent}
     */
    public AppComponent getAppComponent() {
        return App.getAppComponent();
    }

    public MessageComponent getMessageComponent() {
        return mMessageComponent;
    }

    public LogComponent getLogComponent() {
        return mLogComponent;
    }

    public FilterComponent getFilterComponent() {
        return mFilterComponent;
    }

    public IntegrationComponent getIntegrationComponent() {
        return mIntegrationComponent;
    }

    public void startSyncServices(PrefsFactory prefsFactory) {
        getPackageManager().setComponentEnabledSetting(new ComponentName(this, SmsReceiver.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        // Then enable the services
        // Run auto sync service
        runAutoSyncService(prefsFactory);

        // Show notification
        Utility.showNotification(this);
        return;
    }

    /**
     * Runs the {@link com.maishapay.smssync.presentation.service.AutoSyncScheduledService}
     *
     * @return ScheduleServices
     */
    public void runAutoSyncService(PrefsFactory prefsFactory) {

        // Push any pending messages now that we have connectivity
        if (prefsFactory.enableAutoSync().get() && prefsFactory.serviceEnabled().get()) {

            // Start the scheduler for auto sync service
            final long interval = TimeFrequencyUtil.calculateInterval(prefsFactory.autoTime().get());
            final Intent intent = new Intent(this, AutoSyncScheduledReceiver.class);
            // Run the service
            runServices(prefsFactory, intent, ServiceConstants.AUTO_SYNC_SCHEDULED_SERVICE_REQUEST_CODE, interval);
        }
    }

    /**
     * Runs any enabled services. Making sure the device has internet connection before it attempts
     * to start any of the enabled services.
     *
     * @param intent      The intent to be started.
     * @param requestCode The private request code
     * @param interval    The interval in which to run the scheduled service.
     * @return void
     */
    public void runServices(PrefsFactory prefsFactory, Intent intent, int requestCode, long interval) {
        // load current setting
        // is smssync enabled
        if (prefsFactory.serviceEnabled().get()) {

            // show notification
            Utility.showNotification(this);
            // start pushing pending messages

            // do we have data network?
            if (isConnected()) {
                SchedulerInstance.INSTANCE.getScheduler(this, intent, requestCode).updateScheduler(interval);
            }
        }
    }

    public enum SchedulerInstance {
        INSTANCE;

        private Scheduler getScheduler(Context context, Intent intent, int requestCode) {
            return new Scheduler(context, intent, requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public boolean isConnected() {
        return Utility.isConnected(this);
    }
}
