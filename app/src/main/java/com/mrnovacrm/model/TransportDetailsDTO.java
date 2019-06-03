package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransportDetailsDTO {


    @SerializedName("from_route")
    @Expose
    String from_route;

    @SerializedName("to_route")
    @Expose
    String to_route;

    @SerializedName("paid")
    @Expose
    String paid;

    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("vechicle_number")
    @Expose
    String vechicle_number;

    @SerializedName("driver_number")
    @Expose
    String driver_number;

    @SerializedName("driver_name")
    @Expose
    String driver_name;

    @SerializedName("lr_no")
    @Expose
    String lr_no;

    @SerializedName("transport_name")
    @Expose
    String transport_name;

    @SerializedName("contact_no")
    @Expose
    String contact_no;

    @SerializedName("transport_type")
    @Expose
    String transport_type;

   @SerializedName("emp_name")
    @Expose
    String emp_name;

    @SerializedName("estimation_time")
    @Expose
    String estimation_time;

    @SerializedName("contact")
    @Expose
    String contact;

    @SerializedName("company_name")
    @Expose
    String company_name;

    public String getFrom_route() {
        return from_route;
    }

    public void setFrom_route(String from_route) {
        this.from_route = from_route;
    }

    public String getTo_route() {
        return to_route;
    }

    public void setTo_route(String to_route) {
        this.to_route = to_route;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVechicle_number() {
        return vechicle_number;
    }

    public void setVechicle_number(String vechicle_number) {
        this.vechicle_number = vechicle_number;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getLr_no() {
        return lr_no;
    }

    public void setLr_no(String lr_no) {
        this.lr_no = lr_no;
    }

    public String getTransport_name() {
        return transport_name;
    }

    public void setTransport_name(String transport_name) {
        this.transport_name = transport_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getTransport_type() {
        return transport_type;
    }

    public void setTransport_type(String transport_type) {
        this.transport_type = transport_type;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEstimation_time() {
        return estimation_time;
    }

    public void setEstimation_time(String estimation_time) {
        this.estimation_time = estimation_time;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
