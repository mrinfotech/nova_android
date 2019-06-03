package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by prasad on 3/13/2018.
 */

public class StoresDTO {

    String message;
    String status;

    @SerializedName("records")
    @Expose
    List<StoresDTOList> storesDTOList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StoresDTOList> getStoresDTOList() {
        return storesDTOList;
    }

    public void setStoresDTOList(List<StoresDTOList> storesDTOList) {
        this.storesDTOList = storesDTOList;
    }
}
