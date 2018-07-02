package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDistrict {
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
            @SerializedName("districts")
            @Expose
            public District[] district;

            public District[] getDistrict() {
                return district;
            }

            public void setDistrict(District district[]) {
                this.district = district;
            }
        }


    public class District{

        @SerializedName("district_id")
        @Expose
        public int district_id;

        @SerializedName("district_name")
        @Expose
        public String district_name;

        @SerializedName("province_id_fk")
        @Expose
        public int province_id_fk;

        public int getProvince_id_fk() {
            return province_id_fk;
        }

        public void setProvince_id_fk(int province_id_fk) {
            this.province_id_fk = province_id_fk;
        }

        public int getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(int province_id) {
            this.district_id = province_id;
        }

        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }
    }

}
