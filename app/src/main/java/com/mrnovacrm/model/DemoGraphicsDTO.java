package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DemoGraphicsDTO {

    String status;
    String message;

    @SerializedName("records")
    @Expose
    List<RecordsDTO> demograRecordsDTOS;

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

    public List<RecordsDTO> getDemograRecordsDTOS() {
        return demograRecordsDTOS;
    }

    public void setDemograRecordsDTOS(List<RecordsDTO> demograRecordsDTOS) {
        this.demograRecordsDTOS = demograRecordsDTOS;
    }
}
