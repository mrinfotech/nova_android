package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 8/9/2017.
 */

public class ProductDetails {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("brand")
    @Expose
    String brand;

    @SerializedName("itemname")
    @Expose
    String itemname;

    @SerializedName("productid")
    @Expose
    String productid;

    @SerializedName("mrp")
    @Expose
    String mrp;

    @SerializedName("pay")
    @Expose
    String pay;

    @SerializedName("suppier_name")
    @Expose
    String suppier_name;

    @SerializedName("sellerid")
    @Expose
    String sellerid;

    @SerializedName("qty")
    @Expose
    String qty;

    @SerializedName("caseprice")
    @Expose
    String caseprice;

    @SerializedName("company_mrp")
    @Expose
    String company_mrp;

    public String getBal_qty() {
        return bal_qty;
    }

    public void setBal_qty(String bal_qty) {
        this.bal_qty = bal_qty;
    }

    @SerializedName("bal_qty")
    @Expose
    String bal_qty;

    @SerializedName("margin")
    @Expose
    String margin;

    @SerializedName("discount")
    @Expose
    String discount;

    @SerializedName("cart")
    @Expose
    String cart;


    @SerializedName("is_bt")
    @Expose
    String is_bt;

     @SerializedName("branch_price_id")
    @Expose
    String branch_price_id;


    @SerializedName("pack_qty")
    @Expose
    String pack_qty;


    @SerializedName("case_rate")
    @Expose
    String case_rate;

    @SerializedName("item_descr")
    @Expose
    String item_descr;



    @SerializedName("images")
    @Expose
    List<String> imagesList;


    @SerializedName("bt_data")
    @Expose
    List<BTDataModelDTO> btDataModelDTOList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getSuppier_name() {
        return suppier_name;
    }

    public void setSuppier_name(String suppier_name) {
        this.suppier_name = suppier_name;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public String getIs_bt() {
        return is_bt;
    }

    public void setIs_bt(String is_bt) {
        this.is_bt = is_bt;
    }

    public List<BTDataModelDTO> getBtDataModelDTOList() {
        return btDataModelDTOList;
    }

    public void setBtDataModelDTOList(List<BTDataModelDTO> btDataModelDTOList) {
        this.btDataModelDTOList = btDataModelDTOList;
    }

    public String getBranch_price_id() {
        return branch_price_id;
    }

    public void setBranch_price_id(String branch_price_id) {
        this.branch_price_id = branch_price_id;
    }

    public String getPack_qty() {
        return pack_qty;
    }

    public void setPack_qty(String pack_qty) {
        this.pack_qty = pack_qty;
    }

    public String getCase_rate() {
        return case_rate;
    }

    public void setCase_rate(String case_rate) {
        this.case_rate = case_rate;
    }

    public String getCompany_mrp() {
        return company_mrp;
    }

    public void setCompany_mrp(String company_mrp) {
        this.company_mrp = company_mrp;
    }

    public String getItem_descr() {
        return item_descr;
    }

    public void setItem_descr(String item_descr) {
        this.item_descr = item_descr;
    }

    public String getCaseprice() {
        return caseprice;
    }

    public void setCaseprice(String caseprice) {
        this.caseprice = caseprice;
    }
}
