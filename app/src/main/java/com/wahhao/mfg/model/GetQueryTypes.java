package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetQueryTypes {

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
        @SerializedName("query_types")
        @Expose
        public QueryTypes[] query_types;

        public QueryTypes[] getQuery_types() {
            return query_types;
        }

        public void setQuery_types(QueryTypes[] query_types) {
            this.query_types = query_types;
        }
    }

    public class QueryTypes{

        @SerializedName("id")
        @Expose
        public int id ;
        @SerializedName("name")
        @Expose
        public String name ;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
