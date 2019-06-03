package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BranchesDTO {

    String status;
    String message;

    @SerializedName("records")
    @Expose
    List<BranchDetailsDTO> branchDetailsDTOS;

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

    public List<BranchDetailsDTO> getBranchDetailsDTOS() {
        return branchDetailsDTOS;
    }

    public void setBranchDetailsDTOS(List<BranchDetailsDTO> branchDetailsDTOS) {
        this.branchDetailsDTOS = branchDetailsDTOS;
    }
}
