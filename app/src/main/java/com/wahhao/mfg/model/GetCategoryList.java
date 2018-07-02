package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCategoryList {
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

        @SerializedName("cat")
        @Expose
        public Cat[] cat;

        public Cat[] getCat() {
            return cat;
        }

        public void setCat(Cat[] cat) {
            this.cat = cat;
        }
    }

    public class Cat{

        @SerializedName("parent_ids")
        @Expose
        public String parent_ids;
        @SerializedName("name")
        @Expose
        public String name;

        public String getParent_ids() {
            return parent_ids;
        }

        public void setParent_ids(String parent_ids) {
            this.parent_ids = parent_ids;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
