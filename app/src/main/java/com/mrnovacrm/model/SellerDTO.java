package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by prasad on 3/29/2018.
 */

public class SellerDTO {

    String status;
    String message;
    String total_amount;
    String generate;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    @SerializedName("sellers")
    @Expose
    List<SellersListDTO> sellersListDTOS;

    public List<SellersOrdersListDTO> getSellersOrdersListDTO() {
        return sellersOrdersListDTO;
    }

    public void setSellersOrdersListDTO(List<SellersOrdersListDTO> sellersOrdersListDTO) {
        this.sellersOrdersListDTO = sellersOrdersListDTO;
    }

    @SerializedName("records")
    @Expose
    List<SellersOrdersListDTO> sellersOrdersListDTO;

    public List<DeliverStoresListDTO> getStoresListDTO() {
        return storesListDTO;
    }

    public void setStoresListDTO(List<DeliverStoresListDTO> storesListDTO) {
        this.storesListDTO = storesListDTO;
    }

    @SerializedName("stores")
    @Expose
    List<DeliverStoresListDTO> storesListDTO;

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

    public List<SellersListDTO> getSellersListDTOS() {
        return sellersListDTOS;
    }

    public void setSellersListDTOS(List<SellersListDTO> sellersListDTOS) {
        this.sellersListDTOS = sellersListDTOS;
    }

    String is_processed;

    public String getIs_processed() {
        return is_processed;
    }

    public void setIs_processed(String is_processed) {
        this.is_processed = is_processed;
    }

    public String getGenerate() {
        return generate;
    }

    public void setGenerate(String generate) {
        this.generate = generate;
    }
}
