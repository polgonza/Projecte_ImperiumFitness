package com.example.gymapp;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    RETROFIT CLIENT
    ===============
    Classe singleton que configura el client Retrofit per connectar amb WordPress.

    @author ImperiumGym
    @version 3.0
*/
public class RetrofitClient {

    // URL BASE - assegura't que acaba amb /
    private static final String BASE_URL = "https://noticiasappimperium.infinityfreeapp.com/";

    private static Retrofit retrofit;

    public static ApiService getApiService() {
        if (retrofit == null) {
            // Configurar OkHttpClient amb timeout i seguiment de redireccions
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}