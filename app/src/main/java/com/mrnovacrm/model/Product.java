package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by prasad on 3/17/2018.
 */

public class Product {

    String status;
    String message;
    String items_count;


    public String getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    String total_rows;
    String total_units;
    String total_savings;
    String sub_pay;

    public String getTotal_units() {
        return total_units;
    }

    public void setTotal_units(String total_units) {
        this.total_units = total_units;
    }

    public String getTotal_savings() {
        return total_savings;
    }

    public void setTotal_savings(String total_savings) {
        this.total_savings = total_savings;
    }

    public String getTotal_sur_charge() {
        return total_sur_charge;
    }

    public void setTotal_sur_charge(String total_sur_charge) {
        this.total_sur_charge = total_sur_charge;
    }

    public String getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(String total_pay) {
        this.total_pay = total_pay;
    }

    String total_sur_charge;
    String total_pay;


    String hidden_total_pay;
    String balance;


    @SerializedName("items")
    @Expose
    List<ProductDetails> productDetails;

    @SerializedName("records")
    @Expose
    List<CartDetailsDTO> cartDetailsDTOList;


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

    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }

    public String getItems_count() {
        return items_count;
    }

    public void setItems_count(String items_count) {
        this.items_count = items_count;
    }

    public List<CartDetailsDTO> getCartDetailsDTOList() {
        return cartDetailsDTOList;
    }

    public void setCartDetailsDTOList(List<CartDetailsDTO> cartDetailsDTOList) {
        this.cartDetailsDTOList = cartDetailsDTOList;
    }

    public String getSub_pay() {
        return sub_pay;
    }

    public void setSub_pay(String sub_pay) {
        this.sub_pay = sub_pay;
    }

    public String getHidden_total_pay() {
        return hidden_total_pay;
    }

    public void setHidden_total_pay(String hidden_total_pay) {
        this.hidden_total_pay = hidden_total_pay;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
