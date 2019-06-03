package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 28-04-2018.
 */

public class DeliveryRecordsDTO {

    @SerializedName("orders")
    @Expose
    String orders;

    @SerializedName("droute_id")
    @Expose
    String droute_id;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("route_order")
    @Expose
    String route_order;


    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("address")
    @Expose
    String address;

    @SerializedName("latitude")
    @Expose
    String latitude;

    @SerializedName("longitude")
    @Expose
    String longitude;


    @SerializedName("mobile")
    @Expose
    String mobile;

    @SerializedName("pkey")
    @Expose
    String pkey;



    @SerializedName("order_id")
    @Expose
    String order_id;

    @SerializedName("order_value")
    @Expose
    String order_value;

    @SerializedName("route_define")
    @Expose
    String route_define;

    @SerializedName("total_orders")
    @Expose
    String total_orders;



    @SerializedName("orders_list")
    @Expose
    List<DeliveryOrdersListDTO> deliveryOrdersListDTO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<DeliveryOrdersListDTO> getDeliveryOrdersListDTO() {
        return deliveryOrdersListDTO;
    }

    public void setDeliveryOrdersListDTO(List<DeliveryOrdersListDTO> deliveryOrdersListDTO) {
        this.deliveryOrdersListDTO = deliveryOrdersListDTO;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoute_order() {
        return route_order;
    }

    public void setRoute_order(String route_order) {
        this.route_order = route_order;
    }

    public String getDroute_id() {
        return droute_id;
    }

    public void setDroute_id(String droute_id) {
        this.droute_id = droute_id;
    }
    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
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

    public String getRoute_define() {
        return route_define;
    }

    public void setRoute_define(String route_define) {
        this.route_define = route_define;
    }

    public String getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(String total_orders) {
        this.total_orders = total_orders;
    }
}
