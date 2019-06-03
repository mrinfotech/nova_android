package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 28-03-2018.
 */

public class DailyOrdersList {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrderedon() {
        return orderedon;
    }

    public void setOrderedon(String orderedon) {
        this.orderedon = orderedon;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getOrder_value() {
        return order_value;
    }

    public void setOrder_value(String order_value) {
        this.order_value = order_value;
    }

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("order_id")
    @Expose
    String order_id;

    @SerializedName("orderedon")
    @Expose
    String orderedon;

    @SerializedName("store")
    @Expose
    String store;

    @SerializedName("invoice_id")
    @Expose
    String invoice_id;

    @SerializedName("packed_date")
    @Expose
    String packed_date;

    @SerializedName("order_date")
    @Expose
    String order_date;

    @SerializedName("sellet_invoice_pk")
    @Expose
    String sellet_invoice_pk;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStore_status() {
        return store_status;
    }

    public void setStore_status(String store_status) {
        this.store_status = store_status;
    }

    @SerializedName("order_value")
    @Expose
    String order_value;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("address")
    @Expose
    String address;

    @SerializedName("store_status")
    @Expose
    String store_status;

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

    public String getSellet_invoice_pk() {
        return sellet_invoice_pk;
    }

    public void setSellet_invoice_pk(String sellet_invoice_pk) {
        this.sellet_invoice_pk = sellet_invoice_pk;
    }

    public String getPacked_date() {
        return packed_date;
    }

    public void setPacked_date(String packed_date) {
        this.packed_date = packed_date;
    }
}
