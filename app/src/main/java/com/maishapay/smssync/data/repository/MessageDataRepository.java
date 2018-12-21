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

package com.maishapay.smssync.data.repository;

import com.maishapay.smssync.data.entity.Message;
import com.maishapay.smssync.data.entity.mapper.MessageDataMapper;
import com.maishapay.smssync.data.message.MaishapayMessage;
import com.maishapay.smssync.data.message.PostMessage;
import com.maishapay.smssync.data.repository.datasource.message.MessageDataSource;
import com.maishapay.smssync.data.repository.datasource.message.MessageDataSourceFactory;
import com.maishapay.smssync.domain.entity.MessageEntity;
import com.maishapay.smssync.domain.repository.MessageRepository;
import com.maishapay.smssync.presentation.model.MessageModel;
import com.maishapay.smssync.smslib.sms.ProcessSms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@Singleton
public class MessageDataRepository implements MessageRepository {

    private final MessageDataMapper mMessageDataMapper;

    private final MessageDataSourceFactory mMessageDataSourceFactory;

    private MessageDataSource mMessageDataSource;

    private PostMessage mPostMessage;

    private MaishapayMessage mMaishapayMessage;

    @Inject
    public MessageDataRepository(MessageDataMapper messageDataMapper,
                                 MessageDataSourceFactory messageDataSourceFactory,
                                 PostMessage postMessage,
                                 MaishapayMessage maishapayMessage) {
        mMessageDataMapper = messageDataMapper;
        mMessageDataSourceFactory = messageDataSourceFactory;
        mPostMessage = postMessage;
        mMaishapayMessage = maishapayMessage;
    }

    @Override
    public Observable<Integer> deleteByUuid(String uuid) {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.deleteByUuid(uuid);
    }

    @Override
    public Observable<Integer> deleteAll() {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.deleteAll();
    }

    @Override
    public Observable<List<MessageEntity>> fetchByType(MessageEntity.Type type) {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.fetchMessageByType(mMessageDataMapper.map(type))
                .map((messageList -> mMessageDataMapper.map(messageList)));
    }

    @Override
    public Observable<List<MessageEntity>> fetchByStatus(MessageEntity.Status status) {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.fetchMessageByStatus(mMessageDataMapper.map(status))
                .map((messageList -> mMessageDataMapper.map(messageList)));
    }

    @Override
    public Observable<List<MessageEntity>> fetchPending() {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.fetchPending()
                .map((messageList -> mMessageDataMapper.map(messageList)));
    }

    @Override
    public Observable<Boolean> publishMessage(MessageEntity messageEntities) {
        return Observable.defer(() -> {
            boolean status = true;
            List<Message> messages = Arrays.asList(mMessageDataMapper.map(messageEntities));

            //mTweetMessage.tweetMessages(messages);
            //status = mPostMessage.postMessage(messages);

            status = mMaishapayMessage.postMessage(messages);
            return Observable.just(status);
        });
    }

    @Override
    public Observable<Boolean> publishMessages() {
        return Observable.defer(() -> {
            mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
            List<Message> messages = mMessageDataSource.syncFetchPending();
            boolean status;
            //mTweetMessage.tweetMessages(messages);
            //status = mPostMessage.postMessage(messages);

            status = mMaishapayMessage.postMessage(messages);
            return Observable.just(status);
        });
    }

    @Override
    public Observable<List<MessageEntity>> importMessage() {
        return Observable.defer(() -> {
            ProcessSms processSms = mPostMessage.getProcessSms();
            List<MessageModel> smsMessages = processSms.importMessages();
            List<Message> messages = new ArrayList<>();
            mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
            for (MessageModel smsMessage : smsMessages) {
                messages.add(mPostMessage.map(smsMessage));
            }
            mMessageDataSource.putMessages(messages);
            return Observable.just(syncFetchPending());
        });
    }

    @Override
    public MessageEntity syncFetchByUuid(String uuid) {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataMapper.map(mMessageDataSource.fetchPendingByUuid(uuid));
    }


    @Override
    public List<MessageEntity> syncFetchPending() {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataMapper.map(mMessageDataSource.syncFetchPending());
    }

    @Override
    public Observable<List<MessageEntity>> getEntities() {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.getMessages()
                .map((messageList -> mMessageDataMapper.map(messageList)));
    }

    @Override
    public Observable<MessageEntity> getEntity(Long id) {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.getMessage(id).map(message -> mMessageDataMapper.map(message));
    }

    @Override
    public Observable<Long> addEntity(MessageEntity messageEntity) {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.put(mMessageDataMapper.map(messageEntity));
    }

    @Override
    public Observable<Long> updateEntity(MessageEntity messageEntity) {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.put(mMessageDataMapper.map(messageEntity));
    }

    @Override
    public Observable<Long> deleteEntity(Long id) {
        mMessageDataSource = mMessageDataSourceFactory.createMessageDatabaseSource();
        return mMessageDataSource.deleteEntity(id);
    }
}
