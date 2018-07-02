package com.wahhao.mfg.sharedprefrances;

import android.content.Context;
import android.view.View;

import com.wahhao.mfg.beans.Comments;
import com.wahhao.mfg.beans.SavedProducts;
import com.wahhao.mfg.model.SavedProductList;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.utils.DataModel;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SharedPrefNames {

    public static int URL= 0;
    public static String UAT = "http://uat-apidb.nvish.com/public";
    public static String LOCAL = "http://apidb.nvish.com/public";
    public static String PDF = "http://data.nvish.com";

    public static List<String> provinces=new ArrayList<>();
    public static List<String> districts=new ArrayList<>();
    public static List<String> cities=new ArrayList<>();


    public static int PHONE_LENGTH = 11;
    public static String C_CODE = "country_code";
    public static String PHONE_NUMBER = "phone_number";

    public static String SELCTED_LANGUAGE= "language";
    public static String ACCESS_TOKEN = "access_token";
    public static String FULL_NAME = "f_name";
    public static String PROFILE_PIC = "picture";
    public static String SELLER_ID = "seller_id";
    public static String CONTRACT_ID = "contract_resource_id";
    public static String COMP_STAGE_KEY= "completed_stage_key";
    public static String HAS_SIGNED_CONTRACT= "has_signed_contract";
    public static String SUBMIT = "submit";
    public static String DOC_ID = "doc_id";
    public static String TAB_CLICK = "tab";
    public static String WORK_ID = "ws_id";
    public static String PDF_URL = "pdf_url";
    public static String PROFILE_URL = "profile_url";
    public static String TICKET_ID = "ticketID";
    public static String PRODUCT_ID = "id";

    public static List<Comments> commentsList= new ArrayList<>();


    public static List<SavedProducts> savedProductList=new ArrayList<SavedProducts>();
    public static List<String> name =new ArrayList<>();
    public static List<String> ids =new ArrayList<>();

    public static String IS_DEVICETOKEN = "token_update";
    public static String CONTACT = "contacts";
    public static String CONTACT2 = "contacts2";
    public static String ACCESSTOKEN = "access_token";
    public static String PLACE_DATA = "place";
    public static String DEVICE_TOKEN = "device_token";
    public static String CURRENT_BASKET = "current_basket";
    public static String CURRENT_UPDATES= "updates_status";
    public static String MAP_OPTIONS= "map_options";
    public static String SEURITYEMAIL= "s_email";
    public static String NEARBY= "nearby";
    public static String FAV_ROUTED= "fav_route";
    public static String FAV_PLACES= "fav_place";

    public static List<SavedProducts> list=new ArrayList<>();

    public static List<SavedProducts> getProductList(Context context,Prefs sp, final View view)
    {
        list=new ArrayList<>();
        DataModel.loading_box(context,true);
        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
        RestClientWithoutString.getApiService(context)
                .getSavedProduct(header,
                        new Callback<SavedProductList>() {
                            @Override
                            public void success(SavedProductList basic_response, Response response) {
                                DataModel.loading_box_stop();
                                list=basic_response.getResponse().getProducts();
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                DataModel.loading_box_stop();
                                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                DataModel.showSnackBarError(view,json);
                            }
                        });

        return list;
    }

    public static List<SavedProducts> getInReviewList(Context context,Prefs sp, final View view)
    {
        list=new ArrayList<>();
        DataModel.loading_box(context,true);
        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
        RestClientWithoutString.getApiService(context)
                .getSavedProduct(header,
                        new Callback<SavedProductList>() {
                            @Override
                            public void success(SavedProductList basic_response, Response response) {
                                DataModel.loading_box_stop();
                                list=basic_response.getResponse().getProducts();
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                DataModel.loading_box_stop();
                                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                DataModel.showSnackBarError(view,json);
                            }
                        });

        return list;
    }


   }
