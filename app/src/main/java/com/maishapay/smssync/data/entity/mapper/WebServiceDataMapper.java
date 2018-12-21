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

import com.maishapay.smssync.domain.entity.SyncSchemeEntity;
import com.maishapay.smssync.domain.entity.WebServiceEntity;

import com.maishapay.smssync.data.entity.SyncScheme;
import com.maishapay.smssync.data.entity.SyncUrl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class WebServiceDataMapper {

    @Inject
    public WebServiceDataMapper() {
        // Do nothing
    }

    public WebServiceEntity map(SyncUrl syncUrl) {
        WebServiceEntity webServiceEntity = null;
        if (syncUrl != null) {
            webServiceEntity = new WebServiceEntity();
            webServiceEntity._id = syncUrl._id;
            webServiceEntity.setSecret(syncUrl.getSecret());
            webServiceEntity.setTitle(syncUrl.getTitle());
            webServiceEntity.setUrl(syncUrl.getUrl());
            webServiceEntity.setKeywords(syncUrl.getKeywords());
            webServiceEntity.setStatus(map(syncUrl.getStatus()));
            webServiceEntity.setKeywordStatus(map(syncUrl.getKeywordStatus()));
            webServiceEntity.setSyncScheme(
                    new SyncSchemeEntity(syncUrl.getSyncScheme().toJSONString()));
        }
        return webServiceEntity;
    }

    public SyncUrl map(WebServiceEntity webServiceEntity) {
        SyncUrl syncUrl = null;
        if (webServiceEntity != null) {
            syncUrl = new SyncUrl();
            syncUrl._id = webServiceEntity._id;
            syncUrl.setSecret(webServiceEntity.getSecret());
            syncUrl.setTitle(webServiceEntity.getTitle());
            syncUrl.setUrl(webServiceEntity.getUrl());
            syncUrl.setKeywords(webServiceEntity.getKeywords());
            syncUrl.setStatus(map(webServiceEntity.getStatus()));
            syncUrl
                    .setSyncScheme(new SyncScheme(webServiceEntity.getSyncScheme().toJSONString()));
            syncUrl.setKeywordStatus(map(webServiceEntity.getKeywordStatus()));
        }
        return syncUrl;
    }

    public List<WebServiceEntity> map(List<SyncUrl> syncUrls) {
        List<WebServiceEntity> webServiceEntities = new ArrayList<>();
        WebServiceEntity webServiceEntity;
        for (SyncUrl syncUrl : syncUrls) {
            webServiceEntity = map(syncUrl);
            if (webServiceEntity != null) {
                webServiceEntities.add(webServiceEntity);
            }
        }
        return webServiceEntities;
    }

    public SyncUrl.Status map(WebServiceEntity.Status status) {
        if (status != null) {
            return SyncUrl.Status.valueOf(status.name());
        }
        return SyncUrl.Status.DISABLED;
    }

    public WebServiceEntity.Status map(SyncUrl.Status status) {
        if (status != null) {
            return WebServiceEntity.Status.valueOf(status.name());
        }
        return WebServiceEntity.Status.DISABLED;
    }

    public SyncUrl.KeywordStatus map(WebServiceEntity.KeywordStatus keywordStatus) {
        if (keywordStatus != null) {
            return SyncUrl.KeywordStatus.valueOf(keywordStatus.name());
        }
        return SyncUrl.KeywordStatus.DISABLED;
    }

    public WebServiceEntity.KeywordStatus map(SyncUrl.KeywordStatus keywordStatus) {
        if (keywordStatus != null) {
            return WebServiceEntity.KeywordStatus.valueOf(keywordStatus.name());
        }
        return WebServiceEntity.KeywordStatus.DISABLED;
    }
}
