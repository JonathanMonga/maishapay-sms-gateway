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
import com.maishapay.smssync.domain.entity.MessageEntity;

import java.util.List;

import rx.Observable;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public interface MessageRepository extends Repository<MessageEntity> {

    Observable<Integer> deleteByUuid(String uuid);

    Observable<Integer> deleteAll();

    Observable<List<MessageEntity>> fetchByType(MessageEntity.Type type);

    Observable<List<MessageEntity>> fetchByStatus(MessageEntity.Status status);

    Observable<List<MessageEntity>> fetchPending();

    Observable<Boolean> publishMessage(MessageEntity messageEntity);

    Observable<Boolean> publishMessages();

    Observable<List<MessageEntity>> importMessage();

    MessageEntity syncFetchByUuid(String uuid);

    List<MessageEntity> syncFetchPending();
}
