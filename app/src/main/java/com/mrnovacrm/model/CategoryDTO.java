package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 15-03-2018.
 */

public class CategoryDTO {

    String status;
    String message;

    @SerializedName("categories")
    @Expose
    List<CategoryListDTO> categoryList;


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

    public List<CategoryListDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryListDTO> categoryList) {
        this.categoryList = categoryList;
    }

}
