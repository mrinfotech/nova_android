package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 28-04-2018.
 */

public class DeliveryOrdersListDTO {


    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("order_id")
    @Expose
    String order_id;

    @SerializedName("order_value")
    @Expose
    String order_value;

    @SerializedName("is_define")
    @Expose
    String is_define;

    @SerializedName("transport_id")
    @Expose
    String transport_id;

    @SerializedName("transport_name")
    @Expose
    String transport_name;

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

    public String getOrder_value() {
        return order_value;
    }

    public void setOrder_value(String order_value) {
        this.order_value = order_value;
    }

    public String getIs_define() {
        return is_define;
    }

    public void setIs_define(String is_define) {
        this.is_define = is_define;
    }

    public String getTransport_name() {
        return transport_name;
    }

    public void setTransport_name(String transport_name) {
        this.transport_name = transport_name;
    }

    public String getTransport_id() {
        return transport_id;
    }

    public void setTransport_id(String transport_id) {
        this.transport_id = transport_id;
    }
}
