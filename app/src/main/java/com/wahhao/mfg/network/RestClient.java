package com.wahhao.mfg.network;


import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.wahhao.mfg.R;

import java.util.concurrent.TimeUnit;



import retrofit.RestAdapter;
import retrofit.client.OkClient;


public class RestClient {
    private static ApiService apiService = null;


    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {
        CLIENT.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
    }

    public static ApiService getApiService(Context context) {
        if (apiService == null) {
                      RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(context.getResources().getString(R.string.server_base_url)).
//
                                      setLogLevel(RestAdapter.LogLevel.FULL)
                          .setClient(new OkClient(CLIENT))

                    .setConverter(new StringConverter())

                    .build();


            apiService = restAdapter.create(ApiService.class);

        }
        return apiService;
    }



}
