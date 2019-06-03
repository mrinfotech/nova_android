package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 26-03-2018.
 */

public class TrackOrderStatus {

    @SerializedName("order_status")
    @Expose
    String order_status;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getChanged_on() {
        return changed_on;
    }

    public void setChanged_on(String changed_on) {
        this.changed_on = changed_on;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @SerializedName("changed_on")
    @Expose
    String changed_on;

    @SerializedName("comments")
    @Expose
    String comments;

}
