package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCity {

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
        @SerializedName("cities")
        @Expose
        public City[] city;

        public City[] getCity() {
            return city;
        }

        public void setCity(City[] city) {
            this.city = city;
        }
    }



    public class City{

        @SerializedName("city_id")
        @Expose
        public int city_id;

        @SerializedName("district_id_fk")
        @Expose
        public int district_id_fk;

        @SerializedName("city_name")
        @Expose
        public String city_name;


        @SerializedName("province_id_fk")
        @Expose
        public int province_id_fk;

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public int getProvince_id_fk() {
            return province_id_fk;
        }

        public void setProvince_id_fk(int province_id_fk) {
            this.province_id_fk = province_id_fk;
        }

        public int getDistrict_id_fk() {
            return district_id_fk;
        }

        public void setDistrict_id_fk(int district_id_fk) {
            this.district_id_fk = district_id_fk;
        }
    }

}
