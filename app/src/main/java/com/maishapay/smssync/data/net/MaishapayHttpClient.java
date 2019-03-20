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

package com.maishapay.smssync.data.net;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.maishapay.smssync.BuildConfig;
import com.maishapay.smssync.R;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.entity.MaishapayResponse;
import com.maishapay.smssync.data.entity.SyncScheme;
import com.maishapay.smssync.domain.entity.HttpNameValuePair;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@Singleton
public class MaishapayHttpClient extends BaseHttpClient {

    private String mServerError;

    private String mClientError;

    private MaishapayResponse mMaishapayResponse;

    private FileManager mFileManager;

    @Inject
    public MaishapayHttpClient(Context context, FileManager fileManager) {
        super(context);
        mFileManager = fileManager;
    }

    /**
     * Post sms to the configured sync URL
     *
     * @return boolean
     */
    public boolean postMaishapayWebService(String number, String message) {
        initMaishapayWebService(number, message);

        final Gson gson = new Gson();
        try {
            execute();
            Response response = getResponse();
            int statusCode = response.code();
            if (statusCode != 200 && statusCode != 201) {
                setServerError("bad http return code", statusCode);
                return false;
            }

            MaishapayResponse maishapayResponse;
            try {
                maishapayResponse = gson.fromJson(response.body().string(), MaishapayResponse.class);
                setMaishapayServerSuccessResponse(maishapayResponse);
                return true;
            } catch (JsonSyntaxException e) {
                log("Request failed", e);
                setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.TEST_END_POINT);
            }
        } catch (Exception e) {
            log("Request failed", e);
            setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.TEST_END_POINT);
        }

        return false;
    }

    private void initMaishapayWebService(String number, String message) {
        setUrl(BuildConfig.END_POINT);

        SyncScheme.SyncMethod method = SyncScheme.SyncMethod.POST;
        SyncScheme.SyncDataFormat format = SyncScheme.SyncDataFormat.URLEncoded;

        // Clear set params before adding new one to clear the previous one
        getParams().clear();
        setHeader("Content-Type", BuildConfig.CONTENT_TYPE_PARAM);

        Log.e("MAISHAPAY", number);
        Log.e("MAISHAPAY", message);

        addParam(BuildConfig.ENTITY_PARAM, BuildConfig.MESSAGE_REQUEST_PARAM);
        addParam(BuildConfig.TELEPHONE_PARAM, number);
        addParam(BuildConfig.MESSAGE_BODY_REQUEST_PARAM, message);

        try {
            setHttpEntity(format);
        } catch (Exception e) {
            log("Failed to set request body", e);
            setClientError("Failed to format request body " + e.getMessage());
        }

        try {
            switch (method) {
                case POST:
                    setMethod(HttpMethod.POST);
                    break;
                case PUT:
                    setMethod(HttpMethod.PUT);
                    break;
                default:
                    log("Invalid server method");
                    setClientError("Failed to set request method.");
            }
        } catch (Exception e) {
            log("failed to set request method.", e);
            setClientError("Failed to set request method. sync url \n" + BuildConfig.TEST_END_POINT);
        }

    }

    /**
     * Get HTTP Entity populated with data in a format specified by the current sync scheme
     */
    private void setHttpEntity(SyncScheme.SyncDataFormat format) throws Exception {
        RequestBody body;
        switch (format) {
            case URLEncoded:
                log("setHttpEntity format URLEncoded");
                FormBody.Builder builder = new FormBody.Builder();
                List<HttpNameValuePair> params = getParams();
                for (HttpNameValuePair pair : params) {
                    builder.add(pair.getName(), pair.getValue());
                }
                mFileManager.append(mContext.getString(R.string.http_entity_format, "URLEncoded"));
                body = builder.build();
                body.toString();
                break;
            default:
                mFileManager.append(mContext.getString(R.string.invalid_data_format));
                throw new Exception("Invalid data format");
        }
        log("RequestBody is " + body.toString());
        setRequestBody(body);

    }

    public String getClientError() {
        return mClientError;
    }

    public void setClientError(String error) {
        log("Client error " + error);
        Resources res = mContext.getResources();
        mClientError = String.format(Locale.getDefault(), "%s",
                res.getString(R.string.sending_failed_custom_error, error));
        mFileManager.append(mClientError);
    }

    public String getServerError() {
        return mServerError;
    }

    public void setServerError(String error, int statusCode) {
        log("Server error " + error);
        Resources res = mContext.getResources();
        mServerError = String
                .format(res.getString(R.string.sending_failed_custom_error, error),
                        res.getString(R.string.sending_failed_http_code, statusCode));
        mFileManager.append(mServerError);
    }

    public MaishapayResponse getMaishapayServerSuccessResponse() {
        return this.mMaishapayResponse;
    }

    public void setMaishapayServerSuccessResponse(MaishapayResponse maishapayResponse) {
        this.mMaishapayResponse = maishapayResponse;
    }
}
