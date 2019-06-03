package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 22-03-2018.
 */

public class OrdersListDTO {

    @SerializedName("order_key")
    @Expose
    String order_key;

    @SerializedName("parent_order")
    @Expose
    String parent_order;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("single_qty")
    @Expose
    String single_qty;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("sellerid")
    @Expose
    String sellerid;

    @SerializedName("seller_id")
    @Expose
    String seller_id;

    @SerializedName("itemid")
    @Expose
    String itemid;

    @SerializedName("orderid")
    @Expose
    String orderid;

    @SerializedName("mrp")
    @Expose
    String mrp;

    @SerializedName("qty")
    @Expose
    String qty;

    @SerializedName("packed_qty")
    @Expose
    String packed_qty;

    @SerializedName("rem_qty")
    @Expose
    String rem_qty;

    @SerializedName("service_charge")
    @Expose
    String service_charge;

    @SerializedName("created_on")
    @Expose
    String created_on;

    @SerializedName("itemname")
    @Expose
    String itemname;

    @SerializedName("brand")
    @Expose
    String brand;

    @SerializedName("categoryname")
    @Expose
    String categoryname;

    @SerializedName("pack_type")
    @Expose
    String pack_type;

    @SerializedName("dept")
    @Expose
    String dept;

    @SerializedName("ean")
    @Expose
    String ean;

    @SerializedName("seller")
    @Expose
    String seller;

    @SerializedName("order_id")
    @Expose
    String order_id;

    @SerializedName("order_value")
    @Expose
    String order_value;

    @SerializedName("orderedon")
    @Expose
    String orderedon;

    @SerializedName("order_date")
    @Expose
    String order_date;

    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("tax_string")
    @Expose
    String tax_string;

    @SerializedName("picked_qty")
    @Expose
    String picked_qty;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("item_id")
    @Expose
    String item_id;

    @SerializedName("margin")
    @Expose
    String margin;

    @SerializedName("discount")
    @Expose
    String discount;

    @SerializedName("sellingprice")
    @Expose
    String sellingprice;

    @SerializedName("total_price")
    @Expose
    String total_price;

    @SerializedName("sp")
    @Expose
    String sp;

    @SerializedName("sp_amount")
    @Expose
    String sp_amount;

    @SerializedName("total_cost")
    @Expose
    String total_cost;

    @SerializedName("picked_time")
    @Expose
    String picked_time;


    @SerializedName("pb_id")
    @Expose
    String pb_id;


    @SerializedName("bag_name")
    @Expose
    String bag_name;

    @SerializedName("barcode")
    @Expose
    String barcode;

    @SerializedName("cost")
    @Expose
    String cost;

    @SerializedName("barcode_img")
    @Expose
    String barcode_img;

    @SerializedName("action_status")
    @Expose
    String action_status;

    @SerializedName("invoice_id")
    @Expose
    String invoice_id;

    @SerializedName("is_placed")
    @Expose
    String is_placed;


    @SerializedName("balance_qty")
    @Expose
    String balance_qty;

    @SerializedName("balance_descr")
    @Expose
    String balance_descr;

    @SerializedName("delivered_qty")
    @Expose
    String delivered_qty;

    @SerializedName("count")
    @Expose
    String count;


    @SerializedName("payment_type")
    @Expose
    String payment_type;


    @SerializedName("credit_date")
    @Expose
    String credit_date;


    @SerializedName("dealer_id")
    @Expose
    String dealer_id;


    @SerializedName("dealer_name")
    @Expose
    String dealer_name;


    @SerializedName("dealer_code")
    @Expose
    String dealer_code;


    @SerializedName("dealer_contact")
    @Expose
    String dealer_contact;

    @SerializedName("takenby_name")
    @Expose
    String takenby_name;

    @SerializedName("takenby_branch")
    @Expose
    String takenby_branch;

    @SerializedName("takenby_contact")
    @Expose
    String takenby_contact;


    @SerializedName("reference_no")
    @Expose
    String reference_no;

    @SerializedName("transaction_no")
    @Expose
    String transaction_no;

    @SerializedName("transaction_date")
    @Expose
    String transaction_date;

    @SerializedName("batch_no")
    @Expose
    String batch_no;

    @SerializedName("mfg_date")
    @Expose
    String mfg_date;

    @SerializedName("exp_date")
    @Expose
    String exp_date;

 @SerializedName("remarks")
    @Expose
    String remarks;



    @SerializedName("images")
    @Expose
    List<String> imagesList;

    @SerializedName("items")
    @Expose
    List<DelItemsDTO> delItemsDTOList;

    public String getBalance_qty() {
        return balance_qty;
    }

    public void setBalance_qty(String balance_qty) {
        this.balance_qty = balance_qty;
    }

    public String getPacked_qty() {
        return packed_qty;
    }

    public void setPacked_qty(String packed_qty) {
        this.packed_qty = packed_qty;
    }



    public String getRem_qty() {
        return rem_qty;
    }

    public void setRem_qty(String rem_qty) {
        this.rem_qty = rem_qty;
    }



    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getPicked_qty() {
        return picked_qty;
    }

    public void setPicked_qty(String picked_qty) {
        this.picked_qty = picked_qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }



    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPb_id() {
        return pb_id;
    }

    public void setPb_id(String pb_id) {
        this.pb_id = pb_id;
    }

    public String getBag_name() {
        return bag_name;
    }

    public void setBag_name(String bag_name) {
        this.bag_name = bag_name;
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




    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrderedon() {
        return orderedon;
    }

    public void setOrderedon(String orderedon) {
        this.orderedon = orderedon;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }


    public String getPicked_time() {
        return picked_time;
    }

    public void setPicked_time(String picked_time) {
        this.picked_time = picked_time;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcode_img() {
        return barcode_img;
    }

    public void setBarcode_img(String barcode_img) {
        this.barcode_img = barcode_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_value() {
        return order_value;
    }

    public void setOrder_value(String order_value) {
        this.order_value = order_value;
    }

    public List<DelItemsDTO> getDelItemsDTOList() {
        return delItemsDTOList;
    }

    public void setDelItemsDTOList(List<DelItemsDTO> delItemsDTOList) {
        this.delItemsDTOList = delItemsDTOList;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAction_status() {
        return action_status;
    }

    public void setAction_status(String action_status) {
        this.action_status = action_status;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getIs_placed() {
        return is_placed;
    }

    public void setIs_placed(String is_placed) {
        this.is_placed = is_placed;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getDealer_code() {
        return dealer_code;
    }

    public void setDealer_code(String dealer_code) {
        this.dealer_code = dealer_code;
    }

    public String getDealer_contact() {
        return dealer_contact;
    }

    public void setDealer_contact(String dealer_contact) {
        this.dealer_contact = dealer_contact;
    }

    public String getTakenby_name() {
        return takenby_name;
    }

    public void setTakenby_name(String takenby_name) {
        this.takenby_name = takenby_name;
    }

    public String getTakenby_branch() {
        return takenby_branch;
    }

    public void setTakenby_branch(String takenby_branch) {
        this.takenby_branch = takenby_branch;
    }

    public String getTakenby_contact() {
        return takenby_contact;
    }

    public void setTakenby_contact(String takenby_contact) {
        this.takenby_contact = takenby_contact;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getCredit_date() {
        return credit_date;
    }

    public void setCredit_date(String credit_date) {
        this.credit_date = credit_date;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }


    @SerializedName("branch_price_id")
    @Expose
    String branch_price_id;

    @SerializedName("reason")
    @Expose
    String reason;


    @SerializedName("balanced_qty")
    @Expose
    String balanced_qty;

    @SerializedName("rejection_point")
    @Expose
    String rejection_point;


    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getSp_amount() {
        return sp_amount;
    }

    public void setSp_amount(String sp_amount) {
        this.sp_amount = sp_amount;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getBranch_price_id() {
        return branch_price_id;
    }

    public void setBranch_price_id(String branch_price_id) {
        this.branch_price_id = branch_price_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBalanced_qty() {
        return balanced_qty;
    }

    public void setBalanced_qty(String balanced_qty) {
        this.balanced_qty = balanced_qty;
    }

    public String getBalance_descr() {
        return balance_descr;
    }

    public void setBalance_descr(String balance_descr) {
        this.balance_descr = balance_descr;
    }

    public String getRejection_point() {
        return rejection_point;
    }

    public void setRejection_point(String rejection_point) {
        this.rejection_point = rejection_point;
    }

    public String getDelivered_qty() {
        return delivered_qty;
    }

    public void setDelivered_qty(String delivered_qty) {
        this.delivered_qty = delivered_qty;
    }

    public String getOrder_key() {
        return order_key;
    }

    public void setOrder_key(String order_key) {
        this.order_key = order_key;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getTax_string() {
        return tax_string;
    }

    public void setTax_string(String tax_string) {
        this.tax_string = tax_string;
    }

    public String getSingle_qty() {
        return single_qty;
    }

    public void setSingle_qty(String single_qty) {
        this.single_qty = single_qty;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getParent_order() {
        return parent_order;
    }

    public void setParent_order(String parent_order) {
        this.parent_order = parent_order;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getMfg_date() {
        return mfg_date;
    }

    public void setMfg_date(String mfg_date) {
        this.mfg_date = mfg_date;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
