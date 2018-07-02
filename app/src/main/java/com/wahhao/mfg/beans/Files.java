package com.wahhao.mfg.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Files{

    @SerializedName("resource_id")
    @Expose
    public String resource_id;
    @SerializedName("created_on")
    @Expose
    public String created_on;
    @SerializedName("name")
    @Expose
    public String fname;
    @SerializedName("mime")
    @Expose
    public String mime;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("file_name")
    @Expose
    public String file_name;
    @SerializedName("product_id_fk")
    @Expose
    public String product_id_fk;
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("extension")
    @Expose
    public String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getFName() {
        return fname;
    }

    public void setFName(String name) {
        this.fname = name;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getProduct_id_fk() {
        return product_id_fk;
    }

    public void setProduct_id_fk(String product_id_fk) {
        this.product_id_fk = product_id_fk;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
