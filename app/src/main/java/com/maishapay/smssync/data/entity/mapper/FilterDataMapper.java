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

package com.maishapay.smssync.data.entity.mapper;

import com.maishapay.smssync.domain.entity.FilterEntity;

import com.maishapay.smssync.data.entity.Filter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Henry Addo
 */
public class FilterDataMapper {

    @Inject
    public FilterDataMapper() {
        // Do nothing
    }

    /**
     * Maps {@link com.maishapay.smssync.data.entity.Filter} to {@link FilterEntity}
     *
     * @param filter The {@link Filter} to be
     *               mapped
     * @return The {@link Filter} entity
     */
    public FilterEntity map(Filter filter) {
        FilterEntity filterEntity = null;

        if (filter != null) {
            filterEntity = new FilterEntity();
            filterEntity.setId(filter.getId());
            filterEntity.setPhoneNumber(filter.getPhoneNumber());
            filterEntity.setStatus(FilterEntity.Status.valueOf(filter.getStatus().name()));
        }

        return filterEntity;
    }

    public Filter map(FilterEntity filterEntity) {
        Filter filter = null;

        if (filterEntity != null) {
            filter = new Filter();
            filter.setId(filterEntity.getId());
            filter.setPhoneNumber(filterEntity.getPhoneNumber());
            filter.setStatus(Filter.Status.valueOf(filterEntity.getStatus().name()));
        }
        return filter;
    }

    /**
     * Maps a list {@link Filter} into a list of {@link FilterEntity}.
     *
     * @param filterList List to be mapped.
     * @return {@link Filter}
     */
    public List<FilterEntity> map(List<Filter> filterList) {
        List<FilterEntity> filterEntityList = new ArrayList<>();
        FilterEntity filterEntity;
        for (Filter filter : filterList) {
            filterEntity = map(filter);
            if (filterEntity != null) {
                filterEntityList.add(filterEntity);
            }
        }
        return filterEntityList;
    }

    public Filter.Status map(FilterEntity.Status status) {
        return Filter.Status.valueOf(status.name());
    }
}