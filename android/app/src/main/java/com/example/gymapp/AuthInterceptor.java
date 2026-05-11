package com.example.gymapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Llegim el token guardat quan l'usuari va fer login
        SharedPreferences prefs = context.getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", "");

        // Agafem la petició original i li afegim la capçalera del token
        Request request = chain.request().newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        // Enviem la petició modificada
        return chain.proceed(request);
    }
}