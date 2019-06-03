package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 29-03-2018.
 */

public class EmployeeListDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("status")
    @Expose
    String status;

    public String getId() {
        return id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    @SerializedName("role_name")
    @Expose
    String role_name;
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("emp_id")
    @Expose
    String emp_id;

    @SerializedName("address")
    @Expose
    String address;

      @SerializedName("first_name")
    @Expose
    String first_name;

      @SerializedName("last_name")
    @Expose
    String last_name;

      @SerializedName("branch")
    @Expose
    String branch;

      @SerializedName("mobile")
    @Expose
    String mobile;

      @SerializedName("email")
    @Expose
    String email;

      @SerializedName("dob")
    @Expose
    String dob;

    @SerializedName("company_name")
    @Expose
    String company_name;

    @SerializedName("branch_name")
    @Expose
    String branch_name;

    @SerializedName("wallet")
    @Expose
    String wallet;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }
}
