package com.wahhao.mfg.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comments{

    @SerializedName("added_by")
    @Expose
    public String added_by ;
    @SerializedName("comment_text")
    @Expose
    public String comment_text ;
    @SerializedName("docs")
    @Expose
    public Files[] docs ;
    @SerializedName("created_on")
    @Expose
    public String created_on ;

    @SerializedName("full_name")
    @Expose
    public String full_name ;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public Files[] getDocs() {
        return docs;
    }

    public void setDocs(Files[] docs) {
        this.docs = docs;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}