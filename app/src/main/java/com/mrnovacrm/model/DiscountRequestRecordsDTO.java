package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscountRequestRecordsDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("company")
    @Expose
    String company;

    @SerializedName("dealer")
    @Expose
    String dealer;


    @SerializedName("product")
    @Expose
    String product;


    @SerializedName("discount")
    @Expose
    String discount;


    @SerializedName("request_to")
    @Expose
    String requestTo;

    @SerializedName("request_by")
    @Expose
    String requestBy;

    @SerializedName("startDate")
    @Expose
    String startDate;

    @SerializedName("endDate")
    @Expose
    String endDate;

    @SerializedName("requested_on")
    @Expose
    String requestedOn;
    @SerializedName("approved_on")
    @Expose
    String approvedOn;


    @SerializedName("coupon")
    @Expose
    String coupon;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("reqId")
    @Expose
    String reqId;

    @SerializedName("companyName")
    @Expose
    String companyName;


    @SerializedName("itemname")
    @Expose
    String itemName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRequestTo() {
        return requestTo;
    }

    public void setRequestTo(String requestTo) {
        this.requestTo = requestTo;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
