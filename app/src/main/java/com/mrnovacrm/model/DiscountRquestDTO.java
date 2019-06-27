package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 24-06-2019.
 */

public class DiscountRquestDTO {

    int status;
    String message;

    @SerializedName("items_count")
    @Expose
    String itemsCount;

    @SerializedName("records")
    @Expose
    List<DiscountRequestRecordsDTO> recordList;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(String itemsCount1) {
        this.itemsCount = itemsCount1;
    }

    public List<DiscountRequestRecordsDTO> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<DiscountRequestRecordsDTO> recordList1) {
        this.recordList = recordList1;
    }

}
