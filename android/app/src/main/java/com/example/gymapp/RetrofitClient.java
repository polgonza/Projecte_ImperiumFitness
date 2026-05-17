package com.example.gymapp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String TAG = "RetrofitClient";

    private static final String BASE_URL =
            "https://noticiasappimperium.infinityfreeapp.com/";

    private static Retrofit retrofit;

    // Cookie compartida con Glide
    private static String lastCookie = null;

    public static String getLastCookie() {
        return lastCookie;
    }

    public static ApiService getApiService() {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request().newBuilder()
                                .header("User-Agent",
                                        "Mozilla/5.0 (Linux; Android 13) " +
                                                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                                "Chrome/120.0.0.0 Mobile Safari/537.36")
                                .header("Accept", "application/json")
                                .build();

                        Response response = chain.proceed(originalRequest);

                        String contentType = response.header("Content-Type", "");
                        if (contentType != null && contentType.contains("text/html")) {
                            ResponseBody body = response.body();
                            String html = body != null ? body.string() : "";

                            Log.d(TAG, "Challenge detectat, resolent...");

                            String cookieValue = resolveAesChallenge(html);

                            if (cookieValue != null) {
                                Log.d(TAG, "Cookie resolta: __test=" + cookieValue);

                                // Guardamos para que Glide la pueda usar
                                lastCookie = cookieValue;

                                Request newRequest = originalRequest.newBuilder()
                                        .header("Cookie", "__test=" + cookieValue)
                                        .build();

                                response.close();
                                return chain.proceed(newRequest);
                            } else {
                                Log.e(TAG, "No s'ha pogut resoldre el challenge AES");
                            }
                        }

                        return response;
                    })
                    .addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    private static String resolveAesChallenge(String html) {
        try {
            Pattern p = Pattern.compile(
                    "toNumbers\\(\"([0-9a-f]+)\"\\)[^t]*toNumbers\\(\"([0-9a-f]+)\"\\)[^t]*toNumbers\\(\"([0-9a-f]+)\"\\)"
            );
            Matcher m = p.matcher(html);

            if (!m.find()) {
                Log.e(TAG, "No s'han trobat els valors AES al HTML");
                return null;
            }

            byte[] a = hexToBytes(m.group(1));
            byte[] b = hexToBytes(m.group(2));
            byte[] c = hexToBytes(m.group(3));

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    new SecretKeySpec(a, "AES"),
                    new IvParameterSpec(b)
            );
            byte[] decrypted = cipher.doFinal(c);

            return bytesToHex(decrypted);

        } catch (Exception e) {
            Log.e(TAG, "Error resolent AES challenge", e);
            return null;
        }
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}