package com.maishapay.smssync.data.entity;

import com.google.gson.annotations.SerializedName;

public class Balance {

    @SerializedName("fc")
    private String francCongolais;
    @SerializedName("usd")
    private String dollard;

    public String getFrancCongolais() {
        return francCongolais;
    }

    public void setFrancCongolais(String francCongolais) {
        this.francCongolais = francCongolais;
    }

    public String getDollard() {
        return dollard;
    }

    public void setDollard(String dollard) {
        this.dollard = dollard;
    }
}
