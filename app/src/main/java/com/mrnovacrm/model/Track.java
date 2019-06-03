package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 26-03-2018.
 */

public class Track {

    String status;
    String message;
    String is_transfered;

    public List<TrackOrderStatus> getTrackOrderStatus() {
        return trackOrderStatus;
    }

    public void setTrackOrderStatus(List<TrackOrderStatus> trackOrderStatus) {
        this.trackOrderStatus = trackOrderStatus;
    }

    @SerializedName("track")
    @Expose
    List<TrackOrderStatus> trackOrderStatus;


    @SerializedName("details")
    @Expose
    List<TransportDetailsDTO> transportDetailsDTOS;

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

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    String order_status;

    public List<TransportDetailsDTO> getTransportDetailsDTOS() {
        return transportDetailsDTOS;
    }

    public void setTransportDetailsDTOS(List<TransportDetailsDTO> transportDetailsDTOS) {
        this.transportDetailsDTOS = transportDetailsDTOS;
    }

    public String getIs_transfered() {
        return is_transfered;
    }

    public void setIs_transfered(String is_transfered) {
        this.is_transfered = is_transfered;
    }
}
