package com.example.gymapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    RETROFIT CLIENT
    ===============
    Classe singleton que configura el client Retrofit per connectar amb WordPress.

    @author ImperiumGym
    @version 2.0
*/
public class RetrofitClient {

    // La URL BASE ha d'acabar amb "wp/"
    private static final String BASE_URL = "https://noticiasappimperium.infinityfreeapp.com/wp/";

    private static Retrofit retrofit;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}