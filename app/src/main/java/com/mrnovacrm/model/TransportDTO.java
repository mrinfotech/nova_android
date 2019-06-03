package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransportDTO {

    String status;
    String message;

    @SerializedName("transports")
    @Expose
    List<TransportList> transportDTOS;

    @SerializedName("records")
    @Expose
    List<GradesListDTO> gradesDTOS;

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

    public List<TransportList> getTransportDTOS() {
        return transportDTOS;
    }

    public void setTransportDTOS(List<TransportList> transportDTOS) {
        this.transportDTOS = transportDTOS;
    }

    public List<GradesListDTO> getGradesDTOS() {
        return gradesDTOS;
    }

    public void setGradesDTOS(List<GradesListDTO> gradesDTOS) {
        this.gradesDTOS = gradesDTOS;
    }
}
