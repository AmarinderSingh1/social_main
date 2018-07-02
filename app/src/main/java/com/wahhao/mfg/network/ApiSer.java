package com.wahhao.mfg.network;

import com.squareup.okhttp.ResponseBody;

import retrofit.http.GET;
import retrofit2.Call;
import retrofit2.http.Streaming;

public interface ApiSer {

    @GET("/assets/contract-format.pdf")
    @Streaming
    Call<ResponseBody> downloadFile();

    @GET("/assets/contract-format.pdf")
    Call<ResponseBody> downloadFileWithFixedUrl();
}
