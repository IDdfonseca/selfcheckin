package com.idlogistics.selfcheckin.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.0.152:8002/"; //LOCALHOST ANDROID STUDIO 10.0.2.2
    private static Retrofit retrofit = null;
    public static ApiService getApiService() {

        OkHttpClient client = new OkHttpClient.Builder().build();

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
