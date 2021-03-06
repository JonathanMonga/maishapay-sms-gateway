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
import com.maishapay.smssync.presentation.di.module.LogModule;
import com.maishapay.smssync.presentation.presenter.DeleteLogPresenter;
import com.maishapay.smssync.presentation.presenter.ListLogPresenter;
import com.maishapay.smssync.presentation.view.ui.fragment.LogFragment;

import dagger.Component;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, LogModule.class})
public interface LogComponent extends AppActivityComponent {

    void inject(LogFragment logFragment);

    ListLogPresenter listLogPresenter();

    DeleteLogPresenter deleteLogPresenter();
}
