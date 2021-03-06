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

package com.maishapay.smssync.presentation.view.ui.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.addhen.android.raiburari.presentation.ui.adapter.BaseRecyclerViewAdapter;
import com.maishapay.smssync.R;
import com.maishapay.smssync.presentation.model.LogModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class LogAdapter extends BaseRecyclerViewAdapter<LogModel> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new Widgets(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_log_item, viewGroup, false));
    }

    @Override
    public int getAdapterItemCount() {
        return getItems().size();
    }

    @Override
    public void setItems(List<LogModel> items) {
        super.setItems(items);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LogModel logModel = getItem(position);
        // initialize view with content
        ((Widgets) holder).logMessage.setText(logModel.getMessage());
    }

    public class Widgets extends RecyclerView.ViewHolder {

        @BindView(R.id.log_message)
        AppCompatTextView logMessage;

        public Widgets(final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
