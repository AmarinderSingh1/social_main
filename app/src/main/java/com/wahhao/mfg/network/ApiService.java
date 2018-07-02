package com.wahhao.mfg.network;




import com.squareup.okhttp.ResponseBody;
import com.wahhao.mfg.model.ComentResponse;
import com.wahhao.mfg.model.GetCategoryList;
import com.wahhao.mfg.model.GetCity;
import com.wahhao.mfg.model.GetDistrict;
import com.wahhao.mfg.model.GetProduct;
import com.wahhao.mfg.model.GetProfile;
import com.wahhao.mfg.model.GetProvince;
import com.wahhao.mfg.model.GetQueryTypes;
import com.wahhao.mfg.model.GetSample;
import com.wahhao.mfg.model.GetSeller;
import com.wahhao.mfg.model.GetSerReqList;
import com.wahhao.mfg.model.GetTicket;
import com.wahhao.mfg.model.GetTicketFull;
import com.wahhao.mfg.model.OnboardResponse;
import com.wahhao.mfg.model.SavedProductList;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.model.basic_response;

import retrofit.Callback;


import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedString;
import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Url;


/**
 *Define all server calls here
 */
public interface ApiService {


    @Headers( "Content-Type: application/json")
    @POST("/otp/generate")
    void userAuth(
            @Header("App-Identifier") String header,
            @Body TypedString secret_key, Callback<basic_response> cb);

    @Headers( "Content-Type: application/json")
    @POST("/sellers/auth")
    void userOtp(
            @Header("App-Identifier") String header,
            @Body TypedString secret_key, Callback<basic_response> cb);

    @Headers( "Content-Type: application/json")
    @POST("/sellers")
    void useRegister(
            @Header("App-Identifier") String header,
            @Body TypedString secret_key, Callback<basic_response> cb);


    @Headers( "Content-Type: application/json")
    @GET("/sellers/summary")
    void getDropDown(
            @Header("Auth-Identifier") String header,
            Callback<OnboardResponse> cb);


    @Headers( "Content-Type: application/json")
    @GET("/sellers/{sellerId}")
    void getSeller(@Path("sellerId") String sellerId,
            @Header("Auth-Identifier") String header,
            Callback<GetSeller> cb);


    @Headers( "Content-Type: application/json")
    @GET("/provinces")
    void getProvinces(
            @Header("Auth-Identifier") String header,
            Callback<GetProvince> cb);

    @Headers( "Content-Type: application/json")
    @GET("/provinces/{province_id}/districts")
    void getDistrict(@Path("province_id") String proId,
            @Header("Auth-Identifier") String header,
            Callback<GetDistrict> cb);

    @Headers( "Content-Type: application/json")
    @GET("/provinces/districts/{district_id}/cities")
    void getCity(@Path("district_id") String disId,
            @Header("Auth-Identifier") String header,
            Callback<GetCity> cb);

    @Headers( "Content-Type: application/json")
    @POST("/sellers/company_info")
    void userCompInfo(
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<Submit> cb);

    @Headers( "Content-Type: application/json")
    @POST("/sellers/agent_details")
    void userAgentDetails(
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<Submit> cb);

    @Headers( "Content-Type: application/json")
    @POST("/sellers/other_info")
    void userOtherInfo(
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<Submit> cb);

    @Headers( "Content-Type: application/json")
    @POST("/sellers/contract")
    void userContract(
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<Submit> cb);

    @Headers( "Content-Type: application/json")
    @GET("/products/list/1")
    void getSavedProduct(
            @Header("Auth-Identifier") String header,
            Callback<SavedProductList> cb);

    @Headers( "Content-Type: application/json")
    @GET("/products/list/2")
    void getInReviewProduct(
            @Header("Auth-Identifier") String header,
            Callback<SavedProductList> cb);

    @Headers( "Content-Type: application/json")
    @GET("/products/list/3")
    void getDisapprovedProduct(
            @Header("Auth-Identifier") String header,
            Callback<SavedProductList> cb);

    @Headers( "Content-Type: application/json")
    @GET("/products/items/4")
    void getLiveProduct(
            @Header("Auth-Identifier") String header,
            Callback<SavedProductList> cb);

    @Headers( "Content-Type: application/json")
    @GET("/products/items/5")
    void getApprovedProduct(
            @Header("Auth-Identifier") String header,
            Callback<SavedProductList> cb);


    @Headers( "Content-Type: application/json")
    @GET("/categories/list/all")
    void getProductCategory(
            @Header("Auth-Identifier") String header,
            Callback<GetCategoryList> cb);


    @Headers( "Content-Type: application/json")
    @POST("/products")
    void submitProduct(
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<GetProduct> cb);

    @Headers( "Content-Type: application/json")
    @PUT("/products/{id}")
    void updateProduct(@Path("id") String id,
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<GetProduct> cb);

    @Headers( "Content-Type: image/png; charset=binary")
    @POST("/assets/contract-format.pdf")
    @Streaming
    void downloadRetrofit(Callback<Object> cb);



    @Headers( "Content-Type: application/json")
    @PUT("/samples/ship")
    void submitSample(
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<GetSample> cb);


    @Headers( "Content-Type: application/json")
    @DELETE("/products/{product_id}")
    void deleteProduct(@Path("product_id") String product_id,
            @Header("Auth-Identifier") String header,Callback<Submit> cb);

    @Headers( "Content-Type: application/json")
    @GET("/logout")
    void logOut(
            @Header("Auth-Identifier") String header,
            Callback<basic_response> cb);


    @Headers( "Content-Type: application/json")
    @GET("/sellers/myprofile")
    void getProfile(
            @Header("Auth-Identifier") String header,
            Callback<GetProfile> cb);

    @Headers( "Content-Type: application/json")
    @PUT("/sellers/myprofile")
    void saveProfile(
            @Header("Auth-Identifier") String header,
            @Body TypedString se, Callback<basic_response> cb);

    @Headers( "Content-Type: application/json")
    @GET("/support/myTickets")
    void getSerReqList(
            @Header("Auth-Identifier") String header,
            Callback<GetSerReqList> cb);

    @Headers( "Content-Type: application/json")
    @POST("/support/myTickets")
    void createRequest(
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<GetTicket> cb);

    @Headers( "Content-Type: application/json")
    @GET("/support/myTickets/{ticket_id}")
    void getTicket(@Path("ticket_id") String ticket_id ,
            @Header("Auth-Identifier") String header,
            Callback<GetTicketFull> cb);



    @Headers( "Content-Type: application/json")
    @GET("/support/queryTypes")
    void getQueryList(
            @Header("Auth-Identifier") String header,
            Callback<GetQueryTypes> cb);

    @Headers( "Content-Type: application/json")
    @POST("/support/myComments")
    void addComment(
            @Header("Auth-Identifier") String header,
            @Body TypedString secret_key, Callback<ComentResponse> cb);



}
