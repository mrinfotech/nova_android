package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prasad on 5/23/2018.
 */

public class BTDataModelDTO {

    @SerializedName("bt_id")
    @Expose
    String bt_id;

    @SerializedName("bt_qty")
    @Expose
    String bt_qty;

    @SerializedName("bt_price")
    @Expose
    String bt_price;

    @SerializedName("save")
    @Expose
    String save;



    public String getBt_id() {
        return bt_id;
    }

    public void setBt_id(String bt_id) {
        this.bt_id = bt_id;
    }

    public String getBt_qty() {
        return bt_qty;
    }

    public void setBt_qty(String bt_qty) {
        this.bt_qty = bt_qty;
    }

    public String getBt_price() {
        return bt_price;
    }

    public void setBt_price(String bt_price) {
        this.bt_price = bt_price;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
