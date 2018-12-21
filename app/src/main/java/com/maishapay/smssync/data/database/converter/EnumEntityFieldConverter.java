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

package com.maishapay.smssync.data.database.converter;

/**
 * @author Henry Addo
 */

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Locale;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class EnumEntityFieldConverter<E extends Enum> implements FieldConverter<E> {

    private final Class<E> mEnumClass;

    public EnumEntityFieldConverter(Class<E> enumClass) {
        this.mEnumClass = enumClass;
    }

    @Override
    public E fromCursorValue(Cursor cursor, int columnIndex) {
        return (E) Enum.valueOf(mEnumClass, cursor.getString(columnIndex).toUpperCase(
                Locale.getDefault()));
    }

    @Override
    public void toContentValue(E value, String key, ContentValues values) {
        values.put(key, value.toString());
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.TEXT;
    }
}
