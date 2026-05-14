package com.example.gymapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    RETROFIT CLIENT
    ===============
    Classe singleton que configura el client Retrofit per connectar amb WordPress.

    URL BASE: https://noticiasappimperium.infinityfreeapp.com/wp/

    @author ImperiumGym
    @version 1.0
*/
public class RetrofitClient {

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