package com.wahhao.mfg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wahhao.mfg.beans.SavedProducts;

import java.util.List;

public class SavedProductList {

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

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Response getResponse ()
    {
        return response;
    }

    public void setResponse (Response response)
    {
        this.response = response;
    }

    public boolean getStatus ()
    {
        return status;
    }

    public void setStatus (boolean status)
    {
        this.status = status;
    }

    public int getCode ()
    {
        return code;
    }

    public void setCode (int code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", response = "+response+", status = "+status+", code = "+code+"]";
    }

    public class Response
    {
        private String count;

        @SerializedName("products")
        @Expose
        private List<SavedProducts> products;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public List<SavedProducts> getProducts ()
        {
            return products;
        }

        public void setProducts (List<SavedProducts> products)
        {
            this.products = products;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [count = "+count+", products = "+products+"]";
        }
    }


}
