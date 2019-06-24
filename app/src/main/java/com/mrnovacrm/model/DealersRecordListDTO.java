package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 24-06-2019.
 */

public class DealersRecordListDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;


    public String getId() {
        return id;
    }

    public void setId(String id1) {
        this.id = id1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name1) {
        this.name = name1;
    }


}
