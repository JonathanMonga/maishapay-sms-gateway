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

/**
 * @author Maishapay Team <online@maishapay.online>
 */

import com.addhen.android.raiburari.presentation.di.component.ApplicationComponent;
import com.addhen.android.raiburari.presentation.di.module.ActivityModule;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.maishapay.smssync.presentation.view.ui.activity.MainActivity;
import com.maishapay.smssync.presentation.view.ui.navigation.Launcher;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface AppActivityComponent {

    void inject(MainActivity mainActivity);

    Launcher launcher();
}
