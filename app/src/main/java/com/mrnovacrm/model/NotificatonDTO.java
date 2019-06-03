package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by prasad on 5/8/2018.
 */

public class NotificatonDTO {

    @SerializedName("notify_type")
    @Expose
    String notify_type;

    @SerializedName("user_role")
    @Expose
    String user_role;
    @SerializedName("user_id")
    @Expose
    String user_id;
    @SerializedName("branch")
    @Expose
    String branch;
    @SerializedName("is_seen")
    String is_seen;

    @SerializedName("id")
    @Expose
    String id;

   @SerializedName("related_id")
    @Expose
    String related_id;

    @SerializedName("notification")
    @Expose
    String notification;

    @SerializedName("notifiy_on")
    @Expose
    String notifiy_on;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("body")
    @Expose
    String body;

    @SerializedName("grievance_id")
    @Expose
    String grievance_id;

    @SerializedName("notification_on")
    @Expose
    String notification_on;

    @SerializedName("notifiy_date")
    @Expose
    String notifiy_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGrievance_id() {
        return grievance_id;
    }

    public void setGrievance_id(String grievance_id) {
        this.grievance_id = grievance_id;
    }

    public String getNotification_on() {
        return notification_on;
    }

    public void setNotification_on(String notification_on) {
        this.notification_on = notification_on;
    }

    public String getIs_seen() {
        return is_seen;
    }

    public void setIs_seen(String is_seen) {
        this.is_seen = is_seen;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNotifiy_on() {
        return notifiy_on;
    }

    public void setNotifiy_on(String notifiy_on) {
        this.notifiy_on = notifiy_on;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public String getNotifiy_date() {
        return notifiy_date;
    }

    public void setNotifiy_date(String notifiy_date) {
        this.notifiy_date = notifiy_date;
    }
}
