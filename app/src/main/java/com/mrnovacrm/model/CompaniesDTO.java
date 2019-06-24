package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 24-06-2019.
 */

public class CompaniesDTO {

    int status;
    String message;

    @SerializedName("records")
    @Expose
    List<RecordListDTO> recordList;


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

    public List<RecordListDTO> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordListDTO> recordList1) {
        this.recordList = recordList1;
    }

}
