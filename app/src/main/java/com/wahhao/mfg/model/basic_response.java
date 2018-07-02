package com.wahhao.mfg.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class basic_response {



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

    public ResponseOrg getResponse() {
        return response;
    }

    public void setResponse(ResponseOrg response) {
        this.response = response;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public class ResponseOrg
    {
        @SerializedName("pickup_address")
        @Expose
        public String pickAddress;

        @SerializedName("user_info")
        @Expose
        public UserInfo userInfo;;

        @SerializedName("message")
        @Expose
        private String message;


        public String getPickAddress ()
        {
            return pickAddress;
        }

        public void setPickAddress (String pickAddress)
        {
            this.pickAddress = message;
        }


        public String getMessage ()
        {
            return message;
        }

        public void setMessage (String message)
        {
            this.message = message;
        }

        public void setUserInfo(UserInfo obj){
            this.userInfo=obj;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

    }

    public class UserInfo{
        @SerializedName("access_token")
        @Expose
        public String accessToken;

        @SerializedName("full_name")
        @Expose

        public String fullName;

        @SerializedName("profile_pic")
        @Expose
        public String profilePic;

        @SerializedName("seller_id")
        @Expose
        public int sellerId;

        @SerializedName("contract_resource_id")
        @Expose
        public int contractResourceID;


        @SerializedName("completed_stage_key")
        @Expose
        public String comp_stage_key;

        public String getComp_stage_key() {
            return comp_stage_key;
        }

        public void setComp_stage_key(String comp_stage_key) {
            this.comp_stage_key = comp_stage_key;
        }


        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public int getSellerId() {
            return sellerId;
        }

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public int getContractResourceID() {
            return contractResourceID;
        }

        public void setContractResourceID(int contractResourceID) {
            this.contractResourceID = contractResourceID;
        }

        public String getAccessToken ()
        {
            return accessToken;
        }

        public void setMessage (String token)
        {
            this.accessToken = token;
        }

    }

    public class Errors
    {
        private Registered_mobile registered_mobile;

        public Registered_mobile getRegistered_mobile ()
        {
            return registered_mobile;
        }

        public void setRegistered_mobile (Registered_mobile registered_mobile)
        {
            this.registered_mobile = registered_mobile;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [registered_mobile = "+registered_mobile+"]";
        }
    }

    public class Registered_mobile
    {
        private String message;

        private String code;

        public String getMessage ()
        {
            return message;
        }

        public void setMessage (String message)
        {
            this.message = message;
        }

        public String getCode ()
        {
            return code;
        }

        public void setCode (String code)
        {
            this.code = code;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [message = "+message+", code = "+code+"]";
        }
    }
}
