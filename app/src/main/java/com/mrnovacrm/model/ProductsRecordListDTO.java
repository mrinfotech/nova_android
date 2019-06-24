package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harish on 24-06-2019.
 */

public class ProductsRecordListDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("itemname")
    @Expose
    String itemName;


    public String getId() {
        return id;
    }

    public void setId(String id1) {
        this.id = id1;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName1) {
        this.itemName = itemName1;
    }

}