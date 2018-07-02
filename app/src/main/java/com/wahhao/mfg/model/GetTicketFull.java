package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wahhao.mfg.beans.Ticket;

import java.util.List;

public class GetTicketFull {
    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("response")
    @Expose
    public Response response;


    @SerializedName("errors")
    @Expose
    public OnboardResponse.Errors errors;

    @SerializedName("status")
    @Expose
    public boolean status;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public OnboardResponse.Errors getErrors() {
        return errors;
    }

    public void setErrors(OnboardResponse.Errors errors) {
        this.errors = errors;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public class Response{
        @SerializedName("tickets")
        @Expose
        public Ticket tickets;

        public Ticket getTickets() {
            return tickets;
        }

        public void setTickets(Ticket tickets) {
            this.tickets = tickets;
        }
    }
}
