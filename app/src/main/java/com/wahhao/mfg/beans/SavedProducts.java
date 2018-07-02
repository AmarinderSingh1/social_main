package com.wahhao.mfg.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavedProducts {
    @SerializedName("product_id")
    @Expose
    public String product_id;

    @SerializedName("file")
    @Expose
    public Files[] file;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("hs_code")
    @Expose
    public String hs_code;
    @SerializedName("product_id_label")
    @Expose
    public String product_id_label;
    @SerializedName("worksheet")
    @Expose
    public Files[] worksheet;
    @SerializedName("category_id")
    @Expose
    public String category_id;
    @SerializedName("desired_selling_price")
    @Expose
    public String desired_selling_price;
    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("manufacturing_cost")
    @Expose
    public String manufacturing_cost;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("current_margin_per_unit")
    @Expose
    public String current_margin_per_unit;
    @SerializedName("current_selling_price")
    @Expose
    public String current_selling_price;
    @SerializedName("whin")
    @Expose
    public String whin;
    @SerializedName("completed_stage_key")
    @Expose
    public String completed_stage_key;
    @SerializedName("stage_key")
    @Expose
    public String stage_key;

    @SerializedName("sub_category")
    @Expose
    public String sub_category;


    public String seller_id_fk;
    public String currency_id_fk;
    public int isSelected=0;
    public String stage_id_fk;

    public String sku;


    public String getProduct_id() {
        return product_id;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStage_id_fk() {
        return stage_id_fk;
    }

    public void setStage_id_fk(String stage_id_fk) {
        this.stage_id_fk = stage_id_fk;
    }

    public String getWhin() {
        return whin;
    }

    public void setWhin(String whin) {
        this.whin = whin;
    }

    public Files[] getFile() {
        return file;
    }

    public void setFile(Files[] file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHs_code() {
        return hs_code;
    }

    public void setHs_code(String hs_code) {
        this.hs_code = hs_code;
    }

    public String getProduct_id_label() {
        return product_id_label;
    }

    public void setProduct_id_label(String product_id_label) {
        this.product_id_label = product_id_label;
    }

    public Files[] getWorksheet() {
        return worksheet;
    }

    public void setWorksheet(Files[] worksheet) {
        this.worksheet = worksheet;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDesired_selling_price() {
        return desired_selling_price;
    }

    public void setDesired_selling_price(String desired_selling_price) {
        this.desired_selling_price = desired_selling_price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeller_id_fk() {
        return seller_id_fk;
    }

    public void setSeller_id_fk(String seller_id_fk) {
        this.seller_id_fk = seller_id_fk;
    }

    public String getCurrency_id_fk() {
        return currency_id_fk;
    }

    public void setCurrency_id_fk(String currency_id_fk) {
        this.currency_id_fk = currency_id_fk;
    }

    public String getCompleted_stage_key() {
        return completed_stage_key;
    }

    public void setCompleted_stage_key(String completed_stage_key) {
        this.completed_stage_key = completed_stage_key;
    }

    public String getStage_key() {
        return stage_key;
    }

    public void setStage_key(String stage_key) {
        this.stage_key = stage_key;
    }

    public String getManufacturing_cost() {
        return manufacturing_cost;
    }

    public void setManufacturing_cost(String manufacturing_cost) {
        this.manufacturing_cost = manufacturing_cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getCurrent_margin_per_unit() {
        return current_margin_per_unit;
    }

    public void setCurrent_margin_per_unit(String current_margin_per_unit) {
        this.current_margin_per_unit = current_margin_per_unit;
    }

    public String getCurrent_selling_price() {
        return current_selling_price;
    }

    public void setCurrent_selling_price(String current_selling_price) {
        this.current_selling_price = current_selling_price;
    }


}
