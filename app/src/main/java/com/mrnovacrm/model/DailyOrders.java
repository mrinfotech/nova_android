package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 28-03-2018.
 */

public class DailyOrders {

    String status;

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    String message;
    String total_cost;

    public List<DailyOrdersList> getDailyOrdersList() {
        return dailyOrdersList;
    }

    public void setDailyOrdersList(List<DailyOrdersList> dailyOrdersList) {
        this.dailyOrdersList = dailyOrdersList;
    }

    @SerializedName("records")
    @Expose
    List<DailyOrdersList> dailyOrdersList;

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

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    String total_records;
}
