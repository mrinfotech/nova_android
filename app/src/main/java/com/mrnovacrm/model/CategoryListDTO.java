package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 15-03-2018.
 */

public class CategoryListDTO {

    @SerializedName("id")
    @Expose
    String id;

    public List<SubCategoryListDTO> getSubcategoryList() {
        return subcategoryList;
    }

    public void setSubcategoryList(List<SubCategoryListDTO> subcategoryList) {
        this.subcategoryList = subcategoryList;
    }

    @SerializedName("category")
    @Expose
    String category;


    @SerializedName("children")
    @Expose
    List<SubCategoryListDTO> subcategoryList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
