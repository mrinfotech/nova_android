package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DelItemsDTO {

    @SerializedName("pb_id")
    @Expose
    String pb_id;

    @SerializedName("bag_name")
    @Expose
    String bag_name;

    @SerializedName("barcode")
    @Expose
    String barcode;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("barcode_img")
    @Expose
    String barcode_img;



    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("order_item_id")
    @Expose
    String order_item_id;

    @SerializedName("pack_type")
    @Expose
    String pack_type;

    @SerializedName("packed_qty")
    @Expose
    String packed_qty;


    @SerializedName("balance_qty")
    @Expose
    String balance_qty;


    @SerializedName("itemname")
    @Expose
    String itemname;




    public String getPb_id() {
        return pb_id;
    }

    public void setPb_id(String pb_id) {
        this.pb_id = pb_id;
    }

    public String getBag_name() {
        return bag_name;
    }

    public void setBag_name(String bag_name) {
        this.bag_name = bag_name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBarcode_img() {
        return barcode_img;
    }

    public void setBarcode_img(String barcode_img) {
        this.barcode_img = barcode_img;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(String order_item_id) {
        this.order_item_id = order_item_id;
    }

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getPacked_qty() {
        return packed_qty;
    }

    public void setPacked_qty(String packed_qty) {
        this.packed_qty = packed_qty;
    }

    public String getBalance_qty() {
        return balance_qty;
    }

    public void setBalance_qty(String balance_qty) {
        this.balance_qty = balance_qty;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
