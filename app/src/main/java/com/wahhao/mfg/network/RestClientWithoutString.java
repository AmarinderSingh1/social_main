package com.wahhao.mfg.network;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.wahhao.mfg.R;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;


import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Rest client using OkHttpClient to access web-methods.
 */
public class RestClientWithoutString {
    private static ApiService apiService = null;

    private static ApiSer apiSer = null;

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


                      //For UAT
                    /*  RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(context.getResources().getString(R.string.server_base_url)).setLogLevel(RestAdapter.LogLevel.FULL)
                          .setClient(new OkClient(CLIENT))

                    .build();*/

                      //For Local
            String url="";
            switch (SharedPrefNames.URL){
                case 0:
                    url=SharedPrefNames.LOCAL;
                    break;
                case 1:
                    url=SharedPrefNames.UAT;
                    break;
            }
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(url).setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(CLIENT))
                    .build();

            apiService = restAdapter.create(ApiService.class);

        }
        return apiService;
    }

    public static ApiSer getApiSer(Context context) {
        if (apiSer == null) {

                      /*RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(context.getResources().getString(R.string.server_base_url)).setLogLevel(RestAdapter.LogLevel.FULL)
                          .setClient(new OkClient(CLIENT))

                    .build();*/
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SharedPrefNames.PDF)
                    .build();
            apiSer = retrofit.create(ApiSer.class);

        }
        return apiSer;
    }




}
