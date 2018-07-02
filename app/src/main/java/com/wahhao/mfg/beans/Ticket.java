package com.wahhao.mfg.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

public class Ticket{
    @SerializedName("assignee_id")
    @Expose
    public String assignee_id ;
    @SerializedName("assignee_name")
    @Expose
    public String assignee_name ;
    @SerializedName("country_code")
    @Expose
    public String country_code ;
    @SerializedName("created_on")
    @Expose
    public String created_on ;
    @SerializedName("modified_on")
    @Expose
    public String modified_on ;
    @SerializedName("phone")
    @Expose
    public String phone  ;
    @SerializedName("query_type_name")
    @Expose
    public String query_type_name ;
    @SerializedName("requester_id")
    @Expose
    public String requester_id ;
    @SerializedName("requester_name")
    @Expose
    public String requester_name ;
    @SerializedName("ticket_code")
    @Expose
    public String ticket_code ;
    @SerializedName("ticket_desc")
    @Expose
    public String ticket_desc ;
    @SerializedName("ticket_id")
    @Expose
    public String ticket_id ;
    @SerializedName("ticket_title")
    @Expose
    public String ticket_title ;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("request_interval")
    @Expose
    public String request_interval;

    @SerializedName("ticket_status")
    @Expose
    public String ticket_status;
    @SerializedName("comments")
    @Expose
    public Comments[] comments;
    @SerializedName("documents")
    @Expose
    public Files[] documents;

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public Comments[] getComments() {
        return comments;
    }

    public void setComments(Comments[] comments) {
        this.comments = comments;
    }

    public Files[] getDocuments() {
        return documents;
    }

    public void setDocuments(Files[] documents) {
        this.documents = documents;
    }

    public String getRequest_interval() {
        return request_interval;
    }

    public void setRequest_interval(String request_interval) {
        this.request_interval = request_interval;
    }

    public String getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(String assignee_id) {
        this.assignee_id = assignee_id;
    }

    public String getAssignee_name() {
        return assignee_name;
    }

    public void setAssignee_name(String assignee_name) {
        this.assignee_name = assignee_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuery_type_name() {
        return query_type_name;
    }

    public void setQuery_type_name(String query_type_name) {
        this.query_type_name = query_type_name;
    }

    public String getRequester_id() {
        return requester_id;
    }

    public void setRequester_id(String requester_id) {
        this.requester_id = requester_id;
    }

    public String getRequester_name() {
        return requester_name;
    }

    public void setRequester_name(String requester_name) {
        this.requester_name = requester_name;
    }

    public String getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(String ticket_code) {
        this.ticket_code = ticket_code;
    }

    public String getTicket_desc() {
        return ticket_desc;
    }

    public void setTicket_desc(String ticket_desc) {
        this.ticket_desc = ticket_desc;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getTicket_title() {
        return ticket_title;
    }

    public void setTicket_title(String ticket_title) {
        this.ticket_title = ticket_title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
