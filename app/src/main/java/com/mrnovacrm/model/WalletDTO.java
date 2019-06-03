package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletDTO {

    String status;
    String message;

    @SerializedName("balance")
    @Expose
    List<WalletCreditDetailsDTO> walletCreditDetailsDTOList;

    @SerializedName("orders")
    @Expose
    List<WalletCreditDetailsDTO> debitordersDTOList;

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

    public List<WalletCreditDetailsDTO> getWalletCreditDetailsDTOList() {
        return walletCreditDetailsDTOList;
    }

    public void setWalletCreditDetailsDTOList(List<WalletCreditDetailsDTO> walletCreditDetailsDTOList) {
        this.walletCreditDetailsDTOList = walletCreditDetailsDTOList;
    }

    public List<WalletCreditDetailsDTO> getDebitordersDTOList() {
        return debitordersDTOList;
    }

    public void setDebitordersDTOList(List<WalletCreditDetailsDTO> debitordersDTOList) {
        this.debitordersDTOList = debitordersDTOList;
    }
}
