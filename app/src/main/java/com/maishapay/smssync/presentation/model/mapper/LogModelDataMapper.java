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

package com.maishapay.smssync.presentation.model.mapper;

import com.maishapay.smssync.domain.entity.LogEntity;
import com.maishapay.smssync.presentation.model.LogModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class LogModelDataMapper {

    @Inject
    public LogModelDataMapper() {
        // Do nothing
    }

    public LogEntity map(LogModel log) {
        LogEntity logEntity = null;
        if (logEntity != null) {
            logEntity = new LogEntity();
            logEntity._id = log._id;
            logEntity.setMessage(log.getMessage());
        }
        return logEntity;
    }

    public LogModel map(LogEntity logEntity) {
        LogModel log = null;
        if (logEntity != null) {
            log = new LogModel();
            log._id = logEntity._id;
            log.setMessage(logEntity.getMessage());
        }
        return log;
    }

    public List<LogModel> map(List<LogEntity> logList) {
        List<LogModel> logModelList = new ArrayList<>();
        LogModel logModel;
        for (LogEntity log : logList) {
            logModel = map(log);
            if (logModel != null) {
                logModelList.add(logModel);
            }
        }
        return logModelList;
    }
}
