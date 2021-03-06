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

package com.maishapay.smssync.presentation.di.component;

import com.addhen.android.raiburari.presentation.di.module.ActivityModule;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.maishapay.smssync.presentation.di.module.WebServiceModule;
import com.maishapay.smssync.presentation.presenter.webservice.AddWebServicePresenter;
import com.maishapay.smssync.presentation.presenter.webservice.DeleteWebServicePresenter;
import com.maishapay.smssync.presentation.presenter.webservice.ListWebServicePresenter;
import com.maishapay.smssync.presentation.presenter.webservice.UpdateWebServiceKeywordsPresenter;
import com.maishapay.smssync.presentation.presenter.webservice.UpdateWebServicePresenter;
import com.maishapay.smssync.presentation.view.ui.activity.AddWebServiceActivity;
import com.maishapay.smssync.presentation.view.ui.activity.ListWebServiceActivity;
import com.maishapay.smssync.presentation.view.ui.activity.UpdateWebServiceActivity;
import com.maishapay.smssync.presentation.view.ui.fragment.AddKeywordFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.AddWebServiceFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.ListWebServiceFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.UpdateWebServiceFragment;

import dagger.Component;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, WebServiceModule.class})
public interface WebServiceComponent extends AppActivityComponent {

    /**
     * Injects {@link AddWebServiceActivity}
     *
     * @param addWebServiceActivity The webService activity
     */
    void inject(AddWebServiceActivity addWebServiceActivity);

    /**
     * Injects {@link AddWebServiceFragment}
     *
     * @param addWebServiceFragment The webService fragment
     */
    void inject(AddWebServiceFragment addWebServiceFragment);


    /**
     * Injects {@link ListWebServiceActivity}
     *
     * @param listWebServiceActivity The list webService activity
     */
    void inject(ListWebServiceActivity listWebServiceActivity);

    /**
     * Injects {@link UpdateWebServiceActivity}
     *
     * @param updateWebServiceActivity The update webService activity
     */
    void inject(UpdateWebServiceActivity updateWebServiceActivity);


    /**
     * Injects {@link ListWebServiceFragment}
     *
     * @param listWebServiceFragment The list webService fragment
     */
    void inject(ListWebServiceFragment listWebServiceFragment);

    /**
     * Injects {@link UpdateWebServiceFragment}
     *
     * @param updateWebServiceFragment The update webService fragment
     */
    void inject(UpdateWebServiceFragment updateWebServiceFragment);


    /**
     * Injects {@link AddKeywordFragment}
     *
     * @param addKeywordFragment The fragment for adding keyword
     */
    void inject(AddKeywordFragment addKeywordFragment);

    /**
     * Provides {@link UpdateWebServicePresenter} to the sub-graph
     *
     * @return The update webService presenter
     */
    UpdateWebServicePresenter updateWebServicePresenter();

    /**
     * Provides {@link UpdateWebServiceKeywordsPresenter} to the sub-graph
     *
     * @return The update webService presenter
     */
    UpdateWebServiceKeywordsPresenter updateWebServiceKeywordsPresenter();

    /**
     * Provides {@link ListWebServicePresenter} to the sub-graph
     *
     * @return The list webService presenter
     */
    ListWebServicePresenter listWebServicePresenter();

    /**
     * Provides {@link DeleteWebServicePresenter}
     *
     * @return The delete webService presenter
     */
    DeleteWebServicePresenter deleteWebServicePresenter();


    /**
     * Provides {@link AddWebServicePresenter} to sub-graph
     *
     * @return The add webService presenter
     */
    AddWebServicePresenter addWebServicePresenter();

}
