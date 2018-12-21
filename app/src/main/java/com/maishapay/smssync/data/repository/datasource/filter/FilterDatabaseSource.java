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

package com.maishapay.smssync.data.repository.datasource.filter;

import android.support.annotation.NonNull;

import com.maishapay.smssync.data.database.FilterDatabaseHelper;
import com.maishapay.smssync.data.entity.Filter;

import java.util.List;

import rx.Observable;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class FilterDatabaseSource implements FilterDataSource {

    final FilterDatabaseHelper mFilterDatabaseHelper;

    public FilterDatabaseSource(@NonNull FilterDatabaseHelper filterDatabaseHelper) {
        mFilterDatabaseHelper = filterDatabaseHelper;
    }

    @Override
    public Observable<Integer> deleteAllWhiteList() {
        return mFilterDatabaseHelper.deleteAllWhiteList();
    }

    @Override
    public Observable<Integer> deleteAllBlackList() {
        return mFilterDatabaseHelper.deleteAllBlackList();
    }

    @Override
    public Observable<List<Filter>> fetchByStatus(Filter.Status status) {
        return mFilterDatabaseHelper.fetchByStatus(status);
    }

    @Override
    public Observable<List<Filter>> getEntities() {
        return mFilterDatabaseHelper.getFilterList();
    }

    @Override
    public Observable<Filter> getEntity(Long id) {
        return mFilterDatabaseHelper.getFilter(id);
    }

    @Override
    public Observable<Long> addEntity(Filter filter) {
        return mFilterDatabaseHelper.put(filter);
    }

    @Override
    public Observable<Long> updateEntity(Filter filterEntity) {
        return mFilterDatabaseHelper.put(filterEntity);
    }

    @Override
    public Observable<Long> deleteEntity(Long id) {
        return mFilterDatabaseHelper.deleteById(id);
    }

    @Override
    public List<Filter> getFilters() {
        return mFilterDatabaseHelper.getFilters();
    }
}
