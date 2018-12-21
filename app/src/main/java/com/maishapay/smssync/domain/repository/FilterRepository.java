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

package com.maishapay.smssync.domain.repository;

import com.addhen.android.raiburari.domain.repository.Repository;
import com.maishapay.smssync.domain.entity.FilterEntity;

import java.util.List;

import rx.Observable;

/**
 * Repository for manipulating {@link FilterEntity} data
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public interface FilterRepository extends Repository<FilterEntity> {

    Observable<Integer> deleteAllWhiteList();

    Observable<Integer> deleteAllBlackList();

    Observable<List<FilterEntity>> fetchByStatus(FilterEntity.Status status);
}