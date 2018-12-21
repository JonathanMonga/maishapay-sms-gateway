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

package com.maishapay.smssync.data.repository.datasource.log;

import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.entity.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@Singleton
public class LogFileDataSource implements LogDataSource {

    private final FileManager mFileManager;

    @Inject
    public LogFileDataSource(FileManager fileManager) {
        mFileManager = fileManager;
    }

    @Override
    public Observable<List<Log>> getLogs() {
        return mFileManager.getLogs();
    }

    @Override
    public Observable<Long> addLog(Log log) {
        return mFileManager.addLog(log);
    }

    @Override
    public Observable<Long> deleteLog() {
        return mFileManager.deleteLog();
    }

    @Override
    public Observable<Log> getLog() {
        return mFileManager.getLog();
    }
}
