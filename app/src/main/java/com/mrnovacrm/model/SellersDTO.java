package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by prasad on 3/29/2018.
 */

public class SellersDTO {

    String status;
    String message;
    String total_records;

    @SerializedName("records")
    @Expose
    List<SellersRecordsDTOList> sellersRecordsDTOLists;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    public List<SellersRecordsDTOList> getSellersRecordsDTOLists() {
        return sellersRecordsDTOLists;
    }

    public void setSellersRecordsDTOLists(List<SellersRecordsDTOList> sellersRecordsDTOLists) {
        this.sellersRecordsDTOLists = sellersRecordsDTOLists;
    }
}
