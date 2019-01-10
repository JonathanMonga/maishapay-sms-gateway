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

import com.google.gson.annotations.SerializedName;

/**
 * A user response
 */
public class ConfirmRetraitResponse {

    @SerializedName("resultat")
    private int resultat;
    @SerializedName("numero_agent")
    private String numero_agent;
    @SerializedName("nom_agent")
    private String nom_agent;
    @SerializedName("prenom_agent")
    private String prenom_agent;
    @SerializedName("numero_expediteur")
    private String numero_expediteur;
    @SerializedName("nom_expediteur")
    private String nom_expediteur;
    @SerializedName("prenom_expediteur")
    private String prenom_expediteur;
    @SerializedName("montant")
    private String montant;
    @SerializedName("monnaie")
    private String monnaie;
    @SerializedName("message")
    private String message;

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }

    public String getNumero_agent() {
        return numero_agent;
    }

    public void setNumero_agent(String numero_agent) {
        this.numero_agent = numero_agent;
    }

    public String getNom_agent() {
        return nom_agent;
    }

    public void setNom_agent(String nom_agent) {
        this.nom_agent = nom_agent;
    }

    public String getPrenom_agent() {
        return prenom_agent;
    }

    public void setPrenom_agent(String prenom_agent) {
        this.prenom_agent = prenom_agent;
    }

    public String getNumero_expediteur() {
        return numero_expediteur;
    }

    public void setNumero_expediteur(String numero_expediteur) {
        this.numero_expediteur = numero_expediteur;
    }

    public String getNom_expediteur() {
        return nom_expediteur;
    }

    public void setNom_expediteur(String nom_expediteur) {
        this.nom_expediteur = nom_expediteur;
    }

    public String getPrenom_expediteur() {
        return prenom_expediteur;
    }

    public void setPrenom_expediteur(String prenom_expediteur) {
        this.prenom_expediteur = prenom_expediteur;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
