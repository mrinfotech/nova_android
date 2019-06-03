package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prasad on 6/7/2018.
 */

public class BarcodesDTO {



    @SerializedName("pb_id")
    @Expose
    String pb_id;


    @SerializedName("bag_name")
    @Expose
    String bag_name;

    @SerializedName("barcode")
    @Expose
    String barcode;

    @SerializedName("pack_type")
    @Expose
    String pack_type;

    @SerializedName("barcode_img")
    @Expose
    String barcode_img;

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

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getBarcode_img() {
        return barcode_img;
    }

    public void setBarcode_img(String barcode_img) {
        this.barcode_img = barcode_img;
    }
}
