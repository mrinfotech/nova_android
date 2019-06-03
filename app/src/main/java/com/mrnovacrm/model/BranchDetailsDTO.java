package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchDetailsDTO {


    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("mobile")
    @Expose
    String mobile;

    @SerializedName("address")
    @Expose
    String address;

    @SerializedName("district_id")
    @Expose
    String district_id;

    @SerializedName("country_id")
    @Expose
    String country_id;

    @SerializedName("state_id")
    @Expose
    String state_id;

    @SerializedName("latitude")
    @Expose
    String latitude;

    @SerializedName("longitude")
    @Expose
    String longitude;

    @SerializedName("gst")
    @Expose
    String gst;

    @SerializedName("pic")
    @Expose
    String pic;

    @SerializedName("country_nm")
    @Expose
    String country_nm;

    @SerializedName("state_nm")
    @Expose
    String state_nm;

    @SerializedName("district_nm")
    @Expose
    String district_nm;

    @SerializedName("color")
    @Expose
    String color;


    @SerializedName("company")
    @Expose
    String company;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
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

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCountry_nm() {
        return country_nm;
    }

    public void setCountry_nm(String country_nm) {
        this.country_nm = country_nm;
    }

    public String getState_nm() {
        return state_nm;
    }

    public void setState_nm(String state_nm) {
        this.state_nm = state_nm;
    }

    public String getDistrict_nm() {
        return district_nm;
    }

    public void setDistrict_nm(String district_nm) {
        this.district_nm = district_nm;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
