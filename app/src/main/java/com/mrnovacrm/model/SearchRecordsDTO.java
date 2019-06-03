package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRecordsDTO {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("categoryname")
    @Expose
    String categoryname;

    @SerializedName("parentid")
    @Expose
    String parentid;

    @SerializedName("parent_cateogorty")
    @Expose
    String parent_cateogorty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getParent_cateogorty() {
        return parent_cateogorty;
    }

    public void setParent_cateogorty(String parent_cateogorty) {
        this.parent_cateogorty = parent_cateogorty;
    }
}
