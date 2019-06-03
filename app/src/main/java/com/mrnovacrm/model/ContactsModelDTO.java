package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactsModelDTO {


    @SerializedName("key_person")
    @Expose
    String key_person;

    @SerializedName("ofc_email")
    @Expose
    String ofc_email;

    @SerializedName("ofc_contact")
    @Expose
    String ofc_contact;

    @SerializedName("ofc_whatsapp")
    @Expose
    String ofc_whatsapp;

    @SerializedName("company_id")
    @Expose
    String company_id;

    @SerializedName("company")
    @Expose
    String company;

    public String getKey_person() {
        return key_person;
    }

    public void setKey_person(String key_person) {
        this.key_person = key_person;
    }

    public String getOfc_email() {
        return ofc_email;
    }

    public void setOfc_email(String ofc_email) {
        this.ofc_email = ofc_email;
    }

    public String getOfc_contact() {
        return ofc_contact;
    }

    public void setOfc_contact(String ofc_contact) {
        this.ofc_contact = ofc_contact;
    }

    public String getOfc_whatsapp() {
        return ofc_whatsapp;
    }

    public void setOfc_whatsapp(String ofc_whatsapp) {
        this.ofc_whatsapp = ofc_whatsapp;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
