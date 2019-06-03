package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 22-03-2018.
 */

public class Order {

    String status;
    String message;
    String total_records;
    String store_name;
    String total_items;
    String store_address;

    String delivery_date;
    String delivery_time;
    String delivery_charges;
    String sub_total;
    String total_cost;

    String total_processed;
    String order_status;
    String order_id;
    String payment_type;
    String credit_date;

    String mobile;
    String reference_no;
    String takenby_name;
    String denied_reason;
    String denied_person;
    String fa_status;
    String admin_status;
    String created_by;
    String forward_by;
    String parent_order;
    String is_transfered;
    String remarks;

    public String getTotal_rows() {
        return total_rows;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

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

    String total_rows;
    String total_units;
    String total_savings;
    String total_sur_charge;
    String total_pay;

    public String getBag_count() {
        return bag_count;
    }

    public void setBag_count(String bag_count) {
        this.bag_count = bag_count;
    }

    public String getCase_count() {
        return case_count;
    }

    public void setCase_count(String case_count) {
        this.case_count = case_count;
    }

    public String getTin_count() {
        return tin_count;
    }

    public void setTin_count(String tin_count) {
        this.tin_count = tin_count;
    }

    String bag_count;
    String case_count;
    String tin_count;

    public String getOrder_value() {
        return order_value;
    }

    public void setOrder_value(String order_value) {
        this.order_value = order_value;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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



    public List<OrdersListDTO> getOrdersListDTOS() {
        return ordersListDTOS;
    }

    public void setOrdersListDTOS(List<OrdersListDTO> ordersListDTOS) {
        this.ordersListDTOS = ordersListDTOS;
    }

    @SerializedName("records")
    @Expose
    List<OrdersListDTO> ordersListDTOS;


    @SerializedName("barcodes")
    @Expose
    List<BarcodesDTO> barcodesListDTOS;

    public List<OrdersListDTO> getPickerordersListDTOS() {
        return pickerordersListDTOS;
    }

    public void setPickerordersListDTOS(List<OrdersListDTO> pickerordersListDTOS) {
        this.pickerordersListDTOS = pickerordersListDTOS;
    }

    @SerializedName("orders")
    @Expose
    List<OrdersListDTO> pickerordersListDTOS;

    @SerializedName("details")
    @Expose
    List<WalletCreditDetailsDTO> walletCreditDetailsDTOList;

    @SerializedName("lr_details")
    @Expose
    List<LRDetailsDTO> lrDetailsDTOList;

    @SerializedName("rejected_records")
    @Expose
    List<OrdersListDTO> rejectordersListDTOS;


    @SerializedName("statementdetails")
    @Expose
    List<StatementDetailsDTO> statementDetailsDTOS;


    String order_value;
    String processed;
    String rejected;
    String pending;

    String processed_qty;
    String rejected_qty;
    String pending_qty;

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getProcessed_qty() {
        return processed_qty;
    }

    public void setProcessed_qty(String processed_qty) {
        this.processed_qty = processed_qty;
    }

    public String getRejected_qty() {
        return rejected_qty;
    }

    public void setRejected_qty(String rejected_qty) {
        this.rejected_qty = rejected_qty;
    }

    public String getPending_qty() {
        return pending_qty;
    }

    public void setPending_qty(String pending_qty) {
        this.pending_qty = pending_qty;
    }

    public List<BarcodesDTO> getBarcodesListDTOS() {
        return barcodesListDTOS;
    }

    public void setBarcodesListDTOS(List<BarcodesDTO> barcodesListDTOS) {
        this.barcodesListDTOS = barcodesListDTOS;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getTotal_processed() {
        return total_processed;
    }

    public void setTotal_processed(String total_processed) {
        this.total_processed = total_processed;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public List<OrdersListDTO> getRejectordersListDTOS() {
        return rejectordersListDTOS;
    }

    public void setRejectordersListDTOS(List<OrdersListDTO> rejectordersListDTOS) {
        this.rejectordersListDTOS = rejectordersListDTOS;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getTakenby_name() {
        return takenby_name;
    }

    public void setTakenby_name(String takenby_name) {
        this.takenby_name = takenby_name;
    }

    public String getDenied_reason() {
        return denied_reason;
    }

    public void setDenied_reason(String denied_reason) {
        this.denied_reason = denied_reason;
    }

    public String getDenied_person() {
        return denied_person;
    }

    public void setDenied_person(String denied_person) {
        this.denied_person = denied_person;
    }

    public List<WalletCreditDetailsDTO> getWalletCreditDetailsDTOList() {
        return walletCreditDetailsDTOList;
    }

    public void setWalletCreditDetailsDTOList(List<WalletCreditDetailsDTO> walletCreditDetailsDTOList) {
        this.walletCreditDetailsDTOList = walletCreditDetailsDTOList;
    }

    public List<StatementDetailsDTO> getStatementDetailsDTOS() {
        return statementDetailsDTOS;
    }

    public void setStatementDetailsDTOS(List<StatementDetailsDTO> statementDetailsDTOS) {
        this.statementDetailsDTOS = statementDetailsDTOS;
    }

    public List<LRDetailsDTO> getLrDetailsDTOList() {
        return lrDetailsDTOList;
    }

    public void setLrDetailsDTOList(List<LRDetailsDTO> lrDetailsDTOList) {
        this.lrDetailsDTOList = lrDetailsDTOList;
    }

    public String getFa_status() {
        return fa_status;
    }

    public void setFa_status(String fa_status) {
        this.fa_status = fa_status;
    }

    public String getAdmin_status() {
        return admin_status;
    }

    public void setAdmin_status(String admin_status) {
        this.admin_status = admin_status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getForward_by() {
        return forward_by;
    }

    public void setForward_by(String forward_by) {
        this.forward_by = forward_by;
    }

    public String getParent_order() {
        return parent_order;
    }

    public void setParent_order(String parent_order) {
        this.parent_order = parent_order;
    }

    public String getIs_transfered() {
        return is_transfered;
    }

    public void setIs_transfered(String is_transfered) {
        this.is_transfered = is_transfered;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
