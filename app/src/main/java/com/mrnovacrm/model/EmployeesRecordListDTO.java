package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 24-06-2019.
 */

public class EmployeesRecordListDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("uniq_id")
    @Expose
    String uniqId;


    public String getId() {
        return id;
    }

    public void setId(String id1) {
        this.id = id1;
    }


    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId1) {
        this.uniqId = uniqId1;
    }



}
