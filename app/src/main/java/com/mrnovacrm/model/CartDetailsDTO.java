package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by prasad on 3/21/2018.
 */

public class CartDetailsDTO {


    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("company_id")
    @Expose
    String company_id;

    @SerializedName("company")
    @Expose
    String company;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @SerializedName("user_id")
    @Expose

    String user_id;

    @SerializedName("discount")
    @Expose
    String discount;

    @SerializedName("item_id")
    @Expose
    String item_id;

    @SerializedName("sellerid")
    @Expose
    String sellerid;

    @SerializedName("product_count")
    @Expose
    String product_count;

    @SerializedName("modifiedon")
    @Expose
    String modifiedon;

    @SerializedName("seller")
    @Expose
    String seller;

    @SerializedName("itemname")
    @Expose
    String itemname;

    @SerializedName("brand")
    @Expose
    String brand;

    @SerializedName("pay")
    @Expose
    String pay;

    @SerializedName("mrp")
    @Expose
    String mrp;

    @SerializedName("sellingprice")
    @Expose
    String sellingprice;

    @SerializedName("bal_qty")
    @Expose
    String bal_qty;

    @SerializedName("company_mrp")
    @Expose
    String company_mrp;

    @SerializedName("caseprice")
    @Expose
    String caseprice;

    @SerializedName("total_price")
    @Expose
    String total_price;

    @SerializedName("images")
    @Expose
    List<String> imagesList;

    @SerializedName("service_percent")
    @Expose
    String service_percent;


    @SerializedName("pack_type")
    @Expose
    String pack_type;

    @SerializedName("pack_qty")
    @Expose
    String pack_qty;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public String getModifiedon() {
        return modifiedon;
    }

    public void setModifiedon(String modifiedon) {
        this.modifiedon = modifiedon;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSellingprice() {
        return sellingprice;
    }

    public void setSellingprice(String sellingprice) {
        this.sellingprice = sellingprice;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public String getBal_qty() {
        return bal_qty;
    }

    public void setBal_qty(String bal_qty) {
        this.bal_qty = bal_qty;
    }

    public String getService_percent() {
        return service_percent;
    }

    public void setService_percent(String service_percent) {
        this.service_percent = service_percent;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getPack_qty() {
        return pack_qty;
    }

    public void setPack_qty(String pack_qty) {
        this.pack_qty = pack_qty;
    }

    public String getCaseprice() {
        return caseprice;
    }

    public void setCaseprice(String caseprice) {
        this.caseprice = caseprice;
    }

    public String getCompany_mrp() {
        return company_mrp;
    }

    public void setCompany_mrp(String company_mrp) {
        this.company_mrp = company_mrp;
    }
}
