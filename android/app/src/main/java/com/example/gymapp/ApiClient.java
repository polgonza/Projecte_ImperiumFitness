package com.example.gymapp;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //todo Para usar en CLASE + zeroTier
    private static final String BASE_URL = "http://172.21.90.3:8086/";

    //todo Para usar en CASA
    //private static final String BASE_URL = "http://127.0.0.1:8086/";

    // Ara guardem una instància per context, no global
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            // Creem el client HTTP amb l'interceptor que afegeix el token
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}