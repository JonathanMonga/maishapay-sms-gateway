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

package com.maishapay.smssync.data.repository.datasource.message;

import com.maishapay.smssync.data.entity.Message;

import java.util.List;

import rx.Observable;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public interface MessageDataSource {

    Observable<Integer> deleteByUuid(String uuid);

    Observable<Integer> deleteAll();

    Observable<List<Message>> fetchMessageByType(Message.Type type);

    Observable<List<Message>> fetchMessageByStatus(Message.Status status);

    Observable<List<Message>> fetchPending();

    Observable<List<Message>> getMessages();

    Observable<Message> getMessage(Long id);

    Observable<Long> put(Message message);

    Observable<Long> deleteEntity(Long id);

    List<Message> fetchMessage(Message.Type type);

    Message fetchMessageByUuid(String uuid);

    void putMessage(Message message);

    void putMessages(List<Message> messages);

    Integer deleteWithUuid(String uuid);

    Message fetchByUuid(String uuid);

    Message fetchPendingByUuid(String uuid);

    List<Message> syncFetchPending();
}
