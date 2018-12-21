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

package com.maishapay.smssync.presentation.view.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.addhen.android.raiburari.presentation.ui.fragment.BaseFragment;
import com.maishapay.smssync.presentation.di.component.WebServiceComponent;
import com.maishapay.smssync.presentation.model.WebServiceModel;
import com.maishapay.smssync.presentation.presenter.webservice.UpdateWebServiceKeywordsPresenter;
import com.maishapay.smssync.presentation.view.ui.widget.KeywordView;
import com.maishapay.smssync.presentation.view.webservice.UpdateWebServiceKeywordsView;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class AddKeywordFragment extends BaseFragment implements UpdateWebServiceKeywordsView {

    private static final String ARGUMENT_KEY_WEBSERVICE_MODE
            = "com.maishapay.smssync.ARGUMENT_WEBSERVICE_MODEL";

    @BindView(R.id.filter_keyword_integration_title)
    AppCompatTextView mWebServiceTitleTextView;

    @BindView(R.id.keywords_container)
    KeywordView mKeywordsView;

    @Inject
    UpdateWebServiceKeywordsPresenter mUpdateWebServiceKeywordsPresenter;

    private List<String> mKeywords;

    private WebServiceModel mWebServiceModel;

    /**
     * BaseFragment
     */
    public AddKeywordFragment() {
        super(R.layout.fragment_add_keywords, 0);
    }

    public static AddKeywordFragment newInstance(@NonNull WebServiceModel webServiceModel) {
        AddKeywordFragment addKeywordFragment = new AddKeywordFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_KEY_WEBSERVICE_MODE, webServiceModel);
        addKeywordFragment.setArguments(bundle);
        return addKeywordFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebServiceModel = getArguments().getParcelable(ARGUMENT_KEY_WEBSERVICE_MODE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponent(WebServiceComponent.class).inject(this);
        initialize();
    }

    @OnClick(R.id.add_keyword_btn)
    void onAddKeywordClicked() {
        showDialog();
    }

    private void initialize() {
        mUpdateWebServiceKeywordsPresenter.setView(this);
        mWebServiceTitleTextView.setText(mWebServiceModel.getTitle());
        initKeywords();
    }

    private void initKeywords() {
        if (mWebServiceModel.getKeywords() != null && !TextUtils
                .isEmpty(mWebServiceModel.getKeywords())) {
            mKeywords = new ArrayList(Arrays.asList(mWebServiceModel.getKeywords().split(",")));
            setKeywords();
            mKeywordsView.setOnTagSelectListener((view, tag, position) -> {
                mKeywordsView.removeTag(view, position);
                mKeywords.remove(tag.getTag());
                final String keyword = TextUtils.join(",", mKeywords);
                mWebServiceModel.setKeywords(keyword);
                mUpdateWebServiceKeywordsPresenter.updateWebService(mWebServiceModel);
            });
        }
    }

    private void setKeywords() {
        for (int i = 0; i < mKeywords.size(); i++) {
            KeywordView.Tag tag = new KeywordView.Tag(i, mKeywords.get(i),
                    com.maishapay.smssync.presentation.util.Utility.keywordColor(),
                    com.maishapay.smssync.presentation.util.Utility.keywordIcon());
            mKeywordsView.add(tag);
        }
        mKeywordsView.setTags();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_keyword, null);
        EditText keywordEditText = ButterKnife.findById(view, R.id.add_keyword_text);
        builder.setView(view).setPositiveButton(R.string.add,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (Utility.isEmpty(mKeywords)) {
                            mKeywords = new ArrayList<String>();
                            mKeywords.add(keywordEditText.getText().toString());
                        } else {
                            mKeywords.add(keywordEditText.getText().toString());
                        }
                        mWebServiceModel.setKeywords(TextUtils.join(",", mKeywords));
                        mUpdateWebServiceKeywordsPresenter.updateWebService(mWebServiceModel);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onWebServiceSuccessfullyUpdated(Long row) {
        setKeywords();
    }

    @Override
    public void showError(String message) {
        showSnackbar(getView(), message);
    }

    @Override
    public Context getAppContext() {
        return getContext().getApplicationContext();
    }
}
