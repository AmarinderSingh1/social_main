package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProfile {


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

        @SerializedName("seller_id")
        @Expose
        public int seller_id;

        @SerializedName("full_name")
        @Expose
        public String full_name;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("registered_mobile_area_code")
        @Expose
        public String registered_mobile_area_code;
        @SerializedName("registered_mobile")
        @Expose
        public String registered_mobile;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("company_email")
        @Expose
        public String company_email;
        @SerializedName("profile_pic_name")
        @Expose
        public String profile_pic_name;

        public int getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(int seller_id) {
            this.seller_id = seller_id;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getRegistered_mobile_area_code() {
            return registered_mobile_area_code;
        }

        public void setRegistered_mobile_area_code(String registered_mobile_area_code) {
            this.registered_mobile_area_code = registered_mobile_area_code;
        }

        public String getRegistered_mobile() {
            return registered_mobile;
        }

        public void setRegistered_mobile(String registered_mobile) {
            this.registered_mobile = registered_mobile;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getCompany_email() {
            return company_email;
        }

        public void setCompany_email(String company_email) {
            this.company_email = company_email;
        }

        public String getProfile_pic_name() {
            return profile_pic_name;
        }

        public void setProfile_pic_name(String profile_pic_name) {
            this.profile_pic_name = profile_pic_name;
        }
    }

}
