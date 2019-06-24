package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 24-06-2019.
 */

public class EmployeesDiscountsDTO {

    int status;
    String message;

    @SerializedName("records")
    @Expose
    List<EmployeesRecordListDTO> recordList;


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

    public List<EmployeesRecordListDTO> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<EmployeesRecordListDTO> recordList1) {
        this.recordList = recordList1;
    }

}
