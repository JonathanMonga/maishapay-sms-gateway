/*
 * Copyright 2018 Jonathan Monga.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maishapay.smssync.data.entity;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

/**
 * A user response
 */
public class SoldeEpargneResponse {

    @SerializedName("resultat")
    private int resultat;
    @SerializedName("transaction_id")
    private int transaction_id;
    @SerializedName("solde_compte_epargne")
    private Balance mBalance;
    @SerializedName("message")
    private String errorMessage;

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Balance getBalance() {
        return mBalance;
    }

    public void setBalance(Balance balance) {
        mBalance = balance;
    }

    @SuppressLint("DefaultLocale")
    public String getSuccessMessage() {
        return String.format("Maishapay trans ID : %d.\nSolde epargne CDF : %s FC.\nSolde epargne USD : %s $", getTransaction_id(), getBalance().getFrancCongolais(), getBalance().getDollard());
    }

    @SuppressLint("DefaultLocale")
    public String getErrorMessage() {
        return String.format("Maishapay trans ID : %d.\n%s", getTransaction_id(), errorMessage);
    }
}
