package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prasad on 3/29/2018.
 */

public class SellersRecordsDTOList {

    @SerializedName("invoice_id")
    @Expose
    String invoice_id;

    @SerializedName("order_date")
    @Expose
    String order_date;

    @SerializedName("is_processed")
    @Expose
    String is_processed;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("id")
    @Expose
    String id;

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getIs_processed() {
        return is_processed;
    }

    public void setIs_processed(String is_processed) {
        this.is_processed = is_processed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
