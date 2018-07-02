package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnboardResponse {

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("response")
    @Expose
    public ResponseOrg response;


    @SerializedName("errors")
    @Expose
    public Errors errors;

    @SerializedName("status")
    @Expose
    public boolean status;

    public class ResponseOrg {

        @SerializedName("completed_stage_key")
        @Expose
        public String completed_stage_key;

        @SerializedName("has_signed_contract")
        @Expose
        public boolean has_signed_contract;

        public String getCompleted_stage_key() {
            return completed_stage_key;
        }

        public void setCompleted_stage_key(String completed_stage_key) {
            this.completed_stage_key = completed_stage_key;
        }

        public boolean isHas_signed_contract() {
            return has_signed_contract;
        }

        public void setHas_signed_contract(boolean has_signed_contract) {
            this.has_signed_contract = has_signed_contract;
        }
    }


    public class Errors
    {
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;

        @Override
        public String toString()
        {
            return "ClassPojo [registered_mobile = "+message+"]";
        }
    }

}
