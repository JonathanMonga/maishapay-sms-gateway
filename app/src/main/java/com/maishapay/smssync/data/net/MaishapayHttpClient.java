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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.maishapay.smssync.BuildConfig;
import com.maishapay.smssync.R;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.entity.ConfirmRetraitResponse;
import com.maishapay.smssync.data.entity.MobileMoneyResponse;
import com.maishapay.smssync.data.entity.RetraitResponse;
import com.maishapay.smssync.data.entity.SoldeEpargneResponse;
import com.maishapay.smssync.data.entity.SoldeResponse;
import com.maishapay.smssync.data.entity.SyncScheme;
import com.maishapay.smssync.data.util.Logger;
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

    private MobileMoneyResponse mMobileMoneyResponse;

    private SoldeResponse mSoldeResponse;

    private SoldeEpargneResponse mSoldeEpargneResponse;

    private RetraitResponse mRetraitResponse;

    private ConfirmRetraitResponse mConfirmRetraitResponse;

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
    public boolean postSmsToMobileMoneyWebService(String transactionId,
                                                  String from,
                                                  String sentTo,
                                                  String amount,
                                                  String currency,
                                                  String operatorName) {
        Logger.log(MaishapayHttpClient.class.getSimpleName(), "posting messages");
        initMobileMoneyRequest(transactionId, from, sentTo, amount, currency, operatorName);

        final Gson gson = new Gson();
        try {
            execute();
            Response response = getResponse();
            int statusCode = response.code();
            if (statusCode != 200 && statusCode != 201) {
                setServerError("bad http return code", statusCode);
                return false;
            }

            MobileMoneyResponse mobileMoneyResponse;

            try {
                mobileMoneyResponse = gson.fromJson(response.body().string(), MobileMoneyResponse.class);

                setMobileMoneyServerSuccessResp(mobileMoneyResponse);
                return true;
            } catch (JsonSyntaxException e) {
                log("Request failed", e);
                setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
            }
        } catch (Exception e) {
            log("Request failed", e);
            setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
        }

        return false;
    }

    /**
     * Post sms to the configured sync URL
     *
     * @return boolean
     */
    public boolean postSmsToSoldeWebService(String number, String pin) {
        Logger.log(MaishapayHttpClient.class.getSimpleName(), "posting messages");
        initSoldeRequest(number, pin);

        final Gson gson = new Gson();
        try {
            execute();
            Response response = getResponse();
            int statusCode = response.code();
            if (statusCode != 200 && statusCode != 201) {
                setServerError("bad http return code", statusCode);
                return false;
            }

            SoldeResponse soldeResponse;

            try {
                soldeResponse = gson.fromJson(response.body().string(), SoldeResponse.class);

                setSoldeServerSuccessResp(soldeResponse);
                return true;
            } catch (JsonSyntaxException e) {
                log("Request failed", e);
                setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
            }
        } catch (Exception e) {
            log("Request failed", e);
            setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
        }

        return false;
    }

    /**
     * Post sms to the configured sync URL
     *
     * @return boolean
     */
    public boolean postSmsToSoldeEpargneWebService(String number, String pin) {
        Logger.log(MaishapayHttpClient.class.getSimpleName(), "posting messages");
        initSoldeEpargneRequest(number, pin);

        final Gson gson = new Gson();
        try {
            execute();
            Response response = getResponse();
            int statusCode = response.code();
            if (statusCode != 200 && statusCode != 201) {
                setServerError("bad http return code", statusCode);
                return false;
            }

            SoldeEpargneResponse soldeEpargneResponse;

            try {
                soldeEpargneResponse = gson.fromJson(response.body().string(), SoldeEpargneResponse.class);

                setSoldeEpargneServerSuccessResp(soldeEpargneResponse);
                return true;
            } catch (JsonSyntaxException e) {
                log("Request failed", e);
                setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
            }
        } catch (Exception e) {
            log("Request failed", e);
            setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
        }

        return false;
    }

    /**
     * Post sms to the configured sync URL
     *
     * @return boolean
     */
    public boolean postSmsToRetraitWebService(String number, String agent, String montant, String monnaie, String pin) {
        Logger.log(MaishapayHttpClient.class.getSimpleName(), "posting messages");
        initRetraitRequest(number, agent, montant, monnaie, pin);

        final Gson gson = new Gson();
        try {
            execute();
            Response response = getResponse();
            int statusCode = response.code();
            if (statusCode != 200 && statusCode != 201) {
                setServerError("bad http return code", statusCode);
                return false;
            }

            RetraitResponse retraitResponse;

            try {
                retraitResponse = gson.fromJson(response.body().string(), RetraitResponse.class);

                setRetraitServerSuccessResp(retraitResponse);
                return true;
            } catch (JsonSyntaxException e) {
                log("Request failed", e);
                setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
            }
        } catch (Exception e) {
            log("Request failed", e);
            setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
        }

        return false;
    }

    /**
     * Post sms to the configured sync URL
     *
     * @return boolean
     */
    public boolean postSmsToConfirmRetraitWebService(String token, String agent, String expeditaire, String montant, String monnaie, String pin) {
        Logger.log(MaishapayHttpClient.class.getSimpleName(), "posting messages");
        initConfirmRetraitRequest(token, agent, expeditaire, montant, monnaie, pin);

        final Gson gson = new Gson();
        try {
            execute();
            Response response = getResponse();
            int statusCode = response.code();
            if (statusCode != 200 && statusCode != 201) {
                setServerError("bad http return code", statusCode);
                return false;
            }

            ConfirmRetraitResponse confirmRetraitResponse;

            try {
                confirmRetraitResponse = gson.fromJson(response.body().string(), ConfirmRetraitResponse.class);

                setConfirmRetraitServerSuccessResp(confirmRetraitResponse);
                return true;
            } catch (JsonSyntaxException e) {
                log("Request failed", e);
                setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
            }
        } catch (Exception e) {
            log("Request failed", e);
            setClientError("Request failed. " + e.getMessage() + "\n sync url " + BuildConfig.END_POINT);
        }

        return false;
    }

    private void initSoldeEpargneRequest(String number, String pin) {
        setUrl(BuildConfig.END_POINT);

        SyncScheme.SyncMethod method = SyncScheme.SyncMethod.POST;
        SyncScheme.SyncDataFormat format = SyncScheme.SyncDataFormat.URLEncoded;

        // Clear set params before adding new one to clear the previous one
        getParams().clear();
        setHeader("Content-Type", BuildConfig.CONTENT_TYPE_PARAM);

        addParam(BuildConfig.ENTITY_PARAM, BuildConfig.SOLDE_EPARGNE_PERSONNEL_REQUEST_PARAM);
        addParam(BuildConfig.TELEPHONE_PARAM, number);
        addParam(BuildConfig.PIN_PARAM, pin);

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
            setClientError("Failed to set request method. sync url \n" + BuildConfig.END_POINT);
        }

    }

    private void initSoldeRequest(String number, String pin) {
        setUrl(BuildConfig.END_POINT);

        SyncScheme.SyncMethod method = SyncScheme.SyncMethod.POST;
        SyncScheme.SyncDataFormat format = SyncScheme.SyncDataFormat.URLEncoded;

        // Clear set params before adding new one to clear the previous one
        getParams().clear();
        setHeader("Content-Type", BuildConfig.CONTENT_TYPE_PARAM);

        addParam(BuildConfig.ENTITY_PARAM, BuildConfig.SOLDE_REQUEST_PARAM);
        addParam(BuildConfig.TELEPHONE_PARAM, number);
        addParam(BuildConfig.PIN_PARAM, pin);

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
            setClientError("Failed to set request method. sync url \n" + BuildConfig.END_POINT);
        }

    }

    private void initRetraitRequest(String number, String agent, String montant, String monnaie, String pin) {
        setUrl(BuildConfig.END_POINT);

        SyncScheme.SyncMethod method = SyncScheme.SyncMethod.POST;
        SyncScheme.SyncDataFormat format = SyncScheme.SyncDataFormat.URLEncoded;

        // Clear set params before adding new one to clear the previous one
        getParams().clear();
        setHeader("Content-Type", BuildConfig.CONTENT_TYPE_PARAM);

        addParam(BuildConfig.ENTITY_PARAM, BuildConfig.RETRAIT_REQUEST_PARAM);
        addParam(BuildConfig.EXPEDITAIRE_PARAM, number);
        addParam(BuildConfig.DESTINATAIRE_PARAM, agent);
        addParam(BuildConfig.MONTANT_PARAM, montant);
        addParam(BuildConfig.MONNAIE_PARAM, monnaie);
        addParam(BuildConfig.PIN_PARAM, pin);

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
            setClientError("Failed to set request method. sync url \n" + BuildConfig.END_POINT);
        }

    }

    private void initConfirmRetraitRequest(String token, String agent, String expeditaire, String montant, String monnaie, String pin) {
        setUrl(BuildConfig.END_POINT);

        SyncScheme.SyncMethod method = SyncScheme.SyncMethod.POST;
        SyncScheme.SyncDataFormat format = SyncScheme.SyncDataFormat.URLEncoded;

        // Clear set params before adding new one to clear the previous one
        getParams().clear();
        setHeader("Content-Type", BuildConfig.CONTENT_TYPE_PARAM);

        addParam(BuildConfig.ENTITY_PARAM, BuildConfig.CONFIRM_RETRAIT_REQUEST_PARAM);
        addParam(BuildConfig.TOKEN_PARAM, token);
        addParam(BuildConfig.TELEPHONE_PARAM, agent);
        addParam(BuildConfig.DESTINATAIRE_PARAM, expeditaire);
        addParam(BuildConfig.MONTANT_PARAM, montant);
        addParam(BuildConfig.MONNAIE_PARAM, monnaie);
        addParam(BuildConfig.PIN_PARAM, pin);

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
            setClientError("Failed to set request method. sync url \n" + BuildConfig.END_POINT);
        }

    }

    private void initMobileMoneyRequest(String transactionId,
                                        String from,
                                        String sentTo,
                                        String amount,
                                        String currency,
                                        String operatorName) {
        setUrl(BuildConfig.END_POINT);

        SyncScheme.SyncMethod method = SyncScheme.SyncMethod.POST;
        SyncScheme.SyncDataFormat format = SyncScheme.SyncDataFormat.URLEncoded;
        // Clear set params before adding new one to clear the previous one
        getParams().clear();
        setHeader("Content-Type", BuildConfig.CONTENT_TYPE_PARAM);

        addParam(BuildConfig.ENTITY_PARAM, BuildConfig.SEND_REQUEST_PARAM);
        addParam(BuildConfig.TRANSACTION_ID_PARAM, transactionId);
        addParam(BuildConfig.FROM_PARAM, from);
        addParam(BuildConfig.SENT_TO_PARAM, sentTo);
        addParam(BuildConfig.MONTANT_PARAM, amount);
        addParam(BuildConfig.MONNAIE_PARAM, currency);
        addParam(BuildConfig.OPERATOR_NAME_PARAM, operatorName);

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
            setClientError("Failed to set request method. sync url \n" + BuildConfig.END_POINT);
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

    public MobileMoneyResponse getMobileMoneyServerSuccessResp() {
        return mMobileMoneyResponse;
    }

    public void setMobileMoneyServerSuccessResp(MobileMoneyResponse mobileMoneyResponse) {
        this.mMobileMoneyResponse = mobileMoneyResponse;
    }

    public SoldeResponse getSoldeServerSuccessResp() {
        return mSoldeResponse;
    }

    public void setSoldeServerSuccessResp(SoldeResponse soldeResponse) {
        this.mSoldeResponse = soldeResponse;
    }

    public SoldeEpargneResponse getSoldeEpargneServerSuccessResp() {
        return mSoldeEpargneResponse;
    }

    public RetraitResponse getRetraitServerSuccessResp() {
        return mRetraitResponse;
    }

    public ConfirmRetraitResponse getConfirmRetraitServerSuccessResp() {
        return mConfirmRetraitResponse;
    }

    public void setSoldeEpargneServerSuccessResp(SoldeEpargneResponse soldeResponse) {
        this.mSoldeEpargneResponse = soldeResponse;
    }

    public void setRetraitServerSuccessResp(RetraitResponse retraitServerSuccessResp) {
        this.mRetraitResponse = retraitServerSuccessResp;
    }

    public void setConfirmRetraitServerSuccessResp(ConfirmRetraitResponse confirmRetraitServerSuccessResp) {
        this.mConfirmRetraitResponse = confirmRetraitServerSuccessResp;
    }
}
