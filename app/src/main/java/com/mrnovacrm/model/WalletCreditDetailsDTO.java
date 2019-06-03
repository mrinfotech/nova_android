package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletCreditDetailsDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("order_id")
    @Expose
    String order_id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("grade_amount")
    @Expose
    String grade_amount;

    @SerializedName("grade")
    @Expose
    String grade;

    @SerializedName("user_id")
    @Expose
    String user_id;

    @SerializedName("user_role")
    @Expose
    String user_role;

    @SerializedName("prev_balance")
    @Expose
    String prev_balance;

    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("reference_no")
    @Expose
    String reference_no;

    @SerializedName("transaction_no")
    @Expose
    String transaction_no;

    @SerializedName("transaction_mode")
    @Expose
    String transaction_mode;

    @SerializedName("payment_mode")
    @Expose
    String payment_mode;

    @SerializedName("transaction_date")
    @Expose
    String transaction_date;

    @SerializedName("action_by")
    @Expose
    String action_by;

    @SerializedName("action_date")
    @Expose
    String action_date;


    @SerializedName("status")
    @Expose
    String status;


    @SerializedName("cheque_no")
    @Expose
    String cheque_no;

    @SerializedName("cheque_status")
    @Expose
    String cheque_status;

    @SerializedName("account_name")
    @Expose
    String account_name;

    @SerializedName("account_number")
    @Expose
    String account_number;

    @SerializedName("bank_name")
    @Expose
    String bank_name;

    @SerializedName("action_role")
    @Expose
    String action_role;

   @SerializedName("uniq_id")
    @Expose
    String uniq_id;

    @SerializedName("cheque_deposit_date")
    @Expose
    String cheque_deposit_date;

    @SerializedName("dealer_code")
    @Expose
    String dealer_code;

    @SerializedName("company_name")
    @Expose
    String company_name;


    @SerializedName("follower_name")
    @Expose
    String follower_name;


    @SerializedName("days_left")
    @Expose
    String days_left;

    @SerializedName("days_delay")
    @Expose
    String days_delay;



    @SerializedName("follower_id")
    @Expose
    String follower_id;

    @SerializedName("follower_pkey")
    @Expose
    String follower_pkey;


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

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getPrev_balance() {
        return prev_balance;
    }

    public void setPrev_balance(String prev_balance) {
        this.prev_balance = prev_balance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getTransaction_mode() {
        return transaction_mode;
    }

    public void setTransaction_mode(String transaction_mode) {
        this.transaction_mode = transaction_mode;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getAction_by() {
        return action_by;
    }

    public void setAction_by(String action_by) {
        this.action_by = action_by;
    }

    public String getAction_date() {
        return action_date;
    }

    public void setAction_date(String action_date) {
        this.action_date = action_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade_amount() {
        return grade_amount;
    }

    public void setGrade_amount(String grade_amount) {
        this.grade_amount = grade_amount;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCheque_no() {
        return cheque_no;
    }

    public void setCheque_no(String cheque_no) {
        this.cheque_no = cheque_no;
    }

    public String getCheque_status() {
        return cheque_status;
    }

    public void setCheque_status(String cheque_status) {
        this.cheque_status = cheque_status;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAction_role() {
        return action_role;
    }

    public void setAction_role(String action_role) {
        this.action_role = action_role;
    }

    public String getUniq_id() {
        return uniq_id;
    }

    public void setUniq_id(String uniq_id) {
        this.uniq_id = uniq_id;
    }

    public String getCheque_deposit_date() {
        return cheque_deposit_date;
    }

    public void setCheque_deposit_date(String cheque_deposit_date) {
        this.cheque_deposit_date = cheque_deposit_date;
    }

    public String getDealer_code() {
        return dealer_code;
    }

    public void setDealer_code(String dealer_code) {
        this.dealer_code = dealer_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    public String getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(String follower_id) {
        this.follower_id = follower_id;
    }

    public String getFollower_pkey() {
        return follower_pkey;
    }

    public void setFollower_pkey(String follower_pkey) {
        this.follower_pkey = follower_pkey;
    }

    public String getDays_left() {
        return days_left;
    }

    public void setDays_left(String days_left) {
        this.days_left = days_left;
    }

    public String getDays_delay() {
        return days_delay;
    }

    public void setDays_delay(String days_delay) {
        this.days_delay = days_delay;
    }
}
