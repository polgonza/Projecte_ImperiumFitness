package com.example.gymapp;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

        // Cogemos la cookie que ya calculó RetrofitClient
        String cookie = RetrofitClient.getLastCookie();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder()
                            .header("User-Agent",
                                    "Mozilla/5.0 (Linux; Android 13) " +
                                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                            "Chrome/120.0.0.0 Mobile Safari/537.36");

                    // Añadimos la cookie si ya la tenemos
                    if (cookie != null && !cookie.isEmpty()) {
                        builder.header("Cookie", "__test=" + cookie);
                    }

                    return chain.proceed(builder.build());
                })
                .build();

        registry.replace(
                GlideUrl.class,
                InputStream.class,
                new OkHttpUrlLoader.Factory(client)
        );
    }
}