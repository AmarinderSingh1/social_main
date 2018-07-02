package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSeller {
    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("response")
    @Expose
    public SellerData response;


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

    public SellerData getResponse() {
        return response;
    }

    public void setResponse(SellerData response) {
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


    public class SellerData{

        @SerializedName("details")
        @Expose
        public Details details;

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details details) {
            this.details = details;
        }
    }
    public class Details{
        @SerializedName("user")
        @Expose
        public User User;

        public GetSeller.User getUser() {
            return User;
        }

        public void setUser(GetSeller.User user) {
            User = user;
        }
    }

    public class User{
        @SerializedName("company_name")
        @Expose
        public String c_name;

        @SerializedName("province")
        @Expose
        public String province;

        @SerializedName("district")
        @Expose
        public String district;
        @SerializedName("city")
        @Expose
        public String city;

        @SerializedName("street")
        @Expose
        public String street;
        @SerializedName("postal_code")
        @Expose
        public String postal_code;
        @SerializedName("business_registration_number")
        @Expose
        public String business_registration_number;
        @SerializedName("registration_agent")
        @Expose
        public String registration_agent;
        @SerializedName("registration_agent_phone_area_code")
        @Expose
        public String registration_agent_phone_area_code;
        @SerializedName("registration_agent_phone")
        @Expose
        public String registration_agent_phone;

        @SerializedName("year_annual_manufacturing_capacity")
        @Expose
        public String year_annual_manufacturing_capacity;

        @SerializedName("year_annual_turnover")
        @Expose
        public String year_annual_turnover;
        @SerializedName("year_exports")
        @Expose
        public String year_exports;
        @SerializedName("experience_in_us_market")
        @Expose
        public String experience_in_us_market;
        @SerializedName("other_info")
        @Expose
        public String other_info;


        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getPostal_code() {
            return postal_code;
        }

        public void setPostal_code(String postal_code) {
            this.postal_code = postal_code;
        }

        public String getBusiness_registration_number() {
            return business_registration_number;
        }

        public void setBusiness_registration_number(String business_registration_number) {
            this.business_registration_number = business_registration_number;
        }

        public String getRegistration_agent() {
            return registration_agent;
        }

        public void setRegistration_agent(String registration_agent) {
            this.registration_agent = registration_agent;
        }

        public String getRegistration_agent_phone_area_code() {
            return registration_agent_phone_area_code;
        }

        public void setRegistration_agent_phone_area_code(String registration_agent_phone_area_code) {
            this.registration_agent_phone_area_code = registration_agent_phone_area_code;
        }

        public String getRegistration_agent_phone() {
            return registration_agent_phone;
        }

        public void setRegistration_agent_phone(String registration_agent_phone) {
            this.registration_agent_phone = registration_agent_phone;
        }

        public String getYear_annual_manufacturing_capacity() {
            return year_annual_manufacturing_capacity;
        }

        public void setYear_annual_manufacturing_capacity(String year_annual_manufacturing_capacity) {
            this.year_annual_manufacturing_capacity = year_annual_manufacturing_capacity;
        }

        public String getYear_annual_turnover() {
            return year_annual_turnover;
        }

        public void setYear_annual_turnover(String year_annual_turnover) {
            this.year_annual_turnover = year_annual_turnover;
        }

        public String getYear_exports() {
            return year_exports;
        }

        public void setYear_exports(String year_exports) {
            this.year_exports = year_exports;
        }

        public String getExperience_in_us_market() {
            return experience_in_us_market;
        }

        public void setExperience_in_us_market(String experience_in_us_market) {
            this.experience_in_us_market = experience_in_us_market;
        }

        public String getOther_info() {
            return other_info;
        }

        public void setOther_info(String other_info) {
            this.other_info = other_info;
        }
    }




}
