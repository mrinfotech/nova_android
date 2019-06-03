package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 22-02-2018.
 */

public class Login {

    String status;
    String message;
    String userId;
    String primaryid;
    String userName;
    String role;
    String mobile;
    String address;
    String pincode;
    String dp;
    String latitude;
    String longitude;
    String address_id;
    String total_records;
    String total_processed;
    String otp;
    String branch_id;
    String branch_name;
    String locale;
    String short_form;
    String route_id;
    String branch;
    String company;
    String branch_contact;
    String branch_count;
    String role_count;


    @SerializedName("notifications")
    @Expose
    List<NotificatonDTO> notificatonDTOList;


    @SerializedName("details")
    @Expose
    List<EmpDetailsDTO> empDetailsDTOS;

    @SerializedName("licenses")
    @Expose
    List<EmpDetailsDTO> licensesdetais;

    @SerializedName("contact")
    @Expose
    List<ContactsModelDTO> contactsModelDTOList;


    @SerializedName("companies")
    @Expose
    List<ContactsModelDTO> companiesDTOList;

    @SerializedName("roles")
    @Expose
    List<RolesModelDTO> rolesDTOLis;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrimaryid() {
        return primaryid;
    }

    public void setPrimaryid(String primaryid) {
        this.primaryid = primaryid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
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

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    public String getTotal_processed() {
        return total_processed;
    }

    public void setTotal_processed(String total_processed) {
        this.total_processed = total_processed;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getShort_form() {
        return short_form;
    }

    public void setShort_form(String short_form) {
        this.short_form = short_form;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBranch_contact() {
        return branch_contact;
    }

    public void setBranch_contact(String branch_contact) {
        this.branch_contact = branch_contact;
    }

    public List<EmpDetailsDTO> getEmpDetailsDTOS() {
        return empDetailsDTOS;
    }

    public void setEmpDetailsDTOS(List<EmpDetailsDTO> empDetailsDTOS) {
        this.empDetailsDTOS = empDetailsDTOS;
    }

    public List<ContactsModelDTO> getContactsModelDTOList() {
        return contactsModelDTOList;
    }

    public void setContactsModelDTOList(List<ContactsModelDTO> contactsModelDTOList) {
        this.contactsModelDTOList = contactsModelDTOList;
    }

    public List<EmpDetailsDTO> getLicensesdetais() {
        return licensesdetais;
    }

    public void setLicensesdetais(List<EmpDetailsDTO> licensesdetais) {
        this.licensesdetais = licensesdetais;
    }

    public List<ContactsModelDTO> getCompaniesDTOList() {
        return companiesDTOList;
    }

    public void setCompaniesDTOList(List<ContactsModelDTO> companiesDTOList) {
        this.companiesDTOList = companiesDTOList;
    }

    public List<RolesModelDTO> getRolesDTOLis() {
        return rolesDTOLis;
    }

    public void setRolesDTOLis(List<RolesModelDTO> rolesDTOLis) {
        this.rolesDTOLis = rolesDTOLis;
    }

    public String getBranch_count() {
        return branch_count;
    }

    public void setBranch_count(String branch_count) {
        this.branch_count = branch_count;
    }

    public String getRole_count() {
        return role_count;
    }

    public void setRole_count(String role_count) {
        this.role_count = role_count;
    }

    public List<NotificatonDTO> getNotificatonDTOList() {
        return notificatonDTOList;
    }

    public void setNotificatonDTOList(List<NotificatonDTO> notificatonDTOList) {
        this.notificatonDTOList = notificatonDTOList;
    }
}
