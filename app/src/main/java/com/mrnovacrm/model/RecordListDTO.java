package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 24-06-2019.
 */

public class RecordListDTO {

    @SerializedName("company_id")
    @Expose
    String companyId;

    @SerializedName("company")
    @Expose
    String company;

    @SerializedName("prefix")
    @Expose
    String prefix;



    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String company_id) {
        this.companyId = company_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company1) {
        this.company = company1;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix1) {
        this.prefix = prefix1;
    }



}
