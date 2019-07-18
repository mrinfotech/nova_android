package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankAccountRecordsDTO {

    @SerializedName("custId")
    @Expose
    String custId;

    @SerializedName("userId")
    @Expose
    String userId;

    @SerializedName("accNum")
    @Expose
    String accNum;


    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }
}
