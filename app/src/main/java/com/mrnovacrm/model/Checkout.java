package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 22-03-2018.
 */

public class Checkout {

    String status;
    String message;
    String address;

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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    String order_id;
    String order_key;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getOrder_key() {
        return order_key;
    }

    public void setOrder_key(String order_key) {
        this.order_key = order_key;
    }

    @SerializedName("orders")
    @Expose
    List<OrdersListDTO> ordersListDTOList;

    public List<OrdersListDTO> getOrdersListDTOList() {
        return ordersListDTOList;
    }

    public void setOrdersListDTOList(List<OrdersListDTO> ordersListDTOList) {
        this.ordersListDTOList = ordersListDTOList;
    }
}
