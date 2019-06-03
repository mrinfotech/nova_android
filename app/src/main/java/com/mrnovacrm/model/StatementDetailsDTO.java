package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatementDetailsDTO {


    @SerializedName("cheque_no")
    @Expose
    String cheque_no;

    @SerializedName("cheque_date")
    @Expose
    String cheque_date;

    @SerializedName("credit")
    @Expose
    String credit;

    @SerializedName("debit")
    @Expose
    String debit;

    @SerializedName("closing_balance")
    @Expose
    String closing_balance;

    public String getCheque_no() {
        return cheque_no;
    }

    public void setCheque_no(String cheque_no) {
        this.cheque_no = cheque_no;
    }

    public String getCheque_date() {
        return cheque_date;
    }

    public void setCheque_date(String cheque_date) {
        this.cheque_date = cheque_date;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getClosing_balance() {
        return closing_balance;
    }

    public void setClosing_balance(String closing_balance) {
        this.closing_balance = closing_balance;
    }
}
