package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RolesModelDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("company_id")
    @Expose
    String company_id;

    @SerializedName("company")
    @Expose
    String company;

    @SerializedName("branch_id")
    @Expose
    String branch_id;

    @SerializedName("branch_name")
    @Expose
    String branch_name;

    @SerializedName("short_form")
    @Expose
    String short_form;

    @SerializedName("role_id")
    @Expose
    String role_id;

    @SerializedName("role_name")
    @Expose
    String role_name;

    @SerializedName("branch_contact")
    @Expose
    String branch_contact;

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

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getShort_form() {
        return short_form;
    }

    public void setShort_form(String short_form) {
        this.short_form = short_form;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getBranch_contact() {
        return branch_contact;
    }

    public void setBranch_contact(String branch_contact) {
        this.branch_contact = branch_contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
