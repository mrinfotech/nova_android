package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 28-04-2018.
 */

public class DeliveryDTO {
    String status;
    String message;
    String is_define;
    String total_records;
    String status_count;
    String droute_id;

    @SerializedName("records")
    @Expose
    List<DeliveryRecordsDTO> deliveryDTO;


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

    public List<DeliveryRecordsDTO> getDeliveryDTO() {
        return deliveryDTO;
    }

    public void setDeliveryDTO(List<DeliveryRecordsDTO> deliveryDTO) {
        this.deliveryDTO = deliveryDTO;
    }

    public String getIs_define() {
        return is_define;
    }

    public void setIs_define(String is_define) {
        this.is_define = is_define;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    public String getStatus_count() {
        return status_count;
    }

    public void setStatus_count(String status_count) {
        this.status_count = status_count;
    }

    public String getDroute_id() {
        return droute_id;
    }

    public void setDroute_id(String droute_id) {
        this.droute_id = droute_id;
    }
}
