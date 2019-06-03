package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 29-03-2018.
 */

public class SellersOrdersListDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("dis_point")
    @Expose
    String dis_point;

    @SerializedName("rej_point")
    @Expose
    String rej_point;

    @SerializedName("qty")
    @Expose
    String qty;

    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("seller")
    @Expose
    String seller;

    @SerializedName("itemname")
    @Expose
    String itemname;

    @SerializedName("brand")
    @Expose
    String brand;

    @SerializedName("picked_qty")
    @Expose
    String picked_qty;

    @SerializedName("invoice_item_id")
    @Expose
    String invoice_item_id;

    @SerializedName("is_processed")
    @Expose
    String is_processed;

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRej_point() {
        return rej_point;
    }

    public void setRej_point(String rej_point) {
        this.rej_point = rej_point;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    @SerializedName("unit_name")

    @Expose
    String unit_name;

    public String getPicked_qty() {
        return picked_qty;
    }

    public void setPicked_qty(String picked_qty) {
        this.picked_qty = picked_qty;
    }

    public String getInvoice_item_id() {
        return invoice_item_id;
    }

    public void setInvoice_item_id(String invoice_item_id) {
        this.invoice_item_id = invoice_item_id;
    }

    public String getIs_processed() {
        return is_processed;
    }

    public void setIs_processed(String is_processed) {
        this.is_processed = is_processed;
    }

    public String getDis_point() {
        return dis_point;
    }

    public void setDis_point(String dis_point) {
        this.dis_point = dis_point;
    }
}
