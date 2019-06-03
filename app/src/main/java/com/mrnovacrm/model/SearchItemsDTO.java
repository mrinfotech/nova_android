package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchItemsDTO {

    String status;
    String message;

    @SerializedName("items")
    @Expose
    List<SearchItemsList> searchItemsLists;

    @SerializedName("records")
    @Expose
    List<SearchRecordsDTO> searchRecordsDTOS;

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

    public List<SearchRecordsDTO> getSearchRecordsDTOS() {
        return searchRecordsDTOS;
    }

    public void setSearchRecordsDTOS(List<SearchRecordsDTO> searchRecordsDTOS) {
        this.searchRecordsDTOS = searchRecordsDTOS;
    }

    public List<SearchItemsList> getSearchItemsLists() {
        return searchItemsLists;
    }

    public void setSearchItemsLists(List<SearchItemsList> searchItemsLists) {
        this.searchItemsLists = searchItemsLists;
    }


}
