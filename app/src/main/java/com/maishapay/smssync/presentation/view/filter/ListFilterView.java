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

package com.maishapay.smssync.presentation.view.filter;

import com.addhen.android.raiburari.presentation.ui.view.UiView;
import com.maishapay.smssync.presentation.model.FilterModel;
import com.maishapay.smssync.presentation.model.WebServiceModel;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface ListFilterView extends UiView {

    void showFilters(List<FilterModel> filterModelList);

    void showCustomWebService(List<WebServiceModel> webServiceModels);
}
