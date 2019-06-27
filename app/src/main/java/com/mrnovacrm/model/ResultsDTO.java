package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prasad on 6/7/2018.
 */

public class ResultsDTO {



    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("items_count")
    @Expose
    String itemsCount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status1) {
        this.status = status1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message1) {
        this.message = message1;
    }

    public String getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(String itemsCount1) {
        this.itemsCount = itemsCount1;
    }

}