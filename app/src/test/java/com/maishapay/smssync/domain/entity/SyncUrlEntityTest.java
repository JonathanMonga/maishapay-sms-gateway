/*
 *  Copyright (c) 2018 - 2019 Maishapay
 * All rights reserved
 * Contact: contact@maishapay.online
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
 * Maishapay developers at contact@maishapay.online.
 */

package com.maishapay.smssync.domain.entity;

import com.maishapay.smssync.BaseRobolectricTestCase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Maishapay Team <contact@maishapay.online>
 */
public class SyncUrlEntityTest extends BaseRobolectricTestCase {

    private WebServiceEntity mWebServiceEntity;

    @Before
    public void setUp() {
        mWebServiceEntity = DomainEntityFixture.getWebServiceEntity();
    }

    @Test
    public void shouldSetWebServiceEntity() {
        assertNotNull(mWebServiceEntity);
        assertEquals(DomainEntityFixture.ID, mWebServiceEntity.getId());
        assertEquals(DomainEntityFixture.getWebServiceEntity().getKeywords(),
                mWebServiceEntity.getKeywords());
        assertEquals(DomainEntityFixture.getWebServiceEntity().getKeywordStatus(),
                mWebServiceEntity.getKeywordStatus());
        assertEquals(DomainEntityFixture.getWebServiceEntity().getStatus(),
                mWebServiceEntity.getStatus());
        assertEquals(DomainEntityFixture.getWebServiceEntity().getSecret(),
                mWebServiceEntity.getSecret());
        assertNotNull(mWebServiceEntity.getSyncScheme());
        assertEquals(DomainEntityFixture.getWebServiceEntity().getTitle(),
                mWebServiceEntity.getTitle());
        assertEquals(DomainEntityFixture.getWebServiceEntity().getUrl(),
                mWebServiceEntity.getUrl());
        assertNotNull(mWebServiceEntity.getSyncScheme());
    }
}
