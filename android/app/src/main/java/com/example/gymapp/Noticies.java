package com.example.gymapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    NOTICIES ACTIVITY
    =================
    Pantalla que mostra les últimes notícies del gimnàs.
    Les notícies es carreguen des d'un WordPress mitjançant Retrofit.

    @author ImperiumGym
    @version 3.0
*/
public class Noticies extends BaseActivity {

    private static final String TAG = "Noticies";
    private RecyclerView recyclerNoticies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_noticies);

        // Configura el footer de navegació
        setupBottomNav();

        // Configura els insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialitzar RecyclerView
        recyclerNoticies = findViewById(R.id.recyclerNoticies);
        recyclerNoticies.setLayoutManager(new LinearLayoutManager(this));

        // Provar connexió manual primer
        provarConnexioManual();

        // Carregar notícies
        carregarNoticies();
    }

    /*
        MÈTODE PER PROVAR LA CONNEXIÓ MANUALMENT
        ========================================
        Aquest mètode fa una petició HTTP directa per veure què retorna el servidor.
        Ens ajuda a diagnosticar si el problema és de Retrofit o de la URL.
    */
    private void provarConnexioManual() {
        new Thread(() -> {
            try {
                // URL de l'API de WordPress
                URL url = new URL("https://noticiasappimperium.infinityfreeapp.com/wp/wp-json/wp/v2/noticia?_embed");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Codi de resposta manual: " + responseCode);

                // Llegir la resposta
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String respostaText = response.toString();
                Log.d(TAG, "Longitud de la resposta: " + respostaText.length());

                // Mostrar el primer caràcter per saber si és JSON o HTML
                if (respostaText.length() > 0) {
                    char primerCaracter = respostaText.charAt(0);
                    Log.d(TAG, "Primer caràcter: " + primerCaracter);

                    if (primerCaracter == '[') {
                        Log.d(TAG, "✅ La resposta és JSON vàlid!");
                        runOnUiThread(() -> Toast.makeText(Noticies.this, "Connexió OK! JSON rebut", Toast.LENGTH_SHORT).show());
                    } else if (primerCaracter == '<') {
                        Log.e(TAG, "❌ La resposta és HTML! Comprova la URL");
                        runOnUiThread(() -> Toast.makeText(Noticies.this, "Error: El servidor retorna HTML. Revisa la URL", Toast.LENGTH_LONG).show());
                    } else {
                        Log.d(TAG, "Primers 100 caràcters: " + respostaText.substring(0, Math.min(100, respostaText.length())));
                    }
                } else {
                    Log.e(TAG, "Resposta buida");
    private void carregarNoticies() {
        Toast.makeText(this, "Carregant notícies...", Toast.LENGTH_SHORT).show();

        ApiService apiService = RetrofitClient.getApiService();
        apiService.obtenerNoticias().enqueue(new Callback<List<Noticia>>() {

            @Override
            public void onResponse(Call<List<Noticia>> call, Response<List<Noticia>> response) {
                Log.d(TAG, "Codi resposta: " + response.code());

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Log.d(TAG, "Notícies rebudes: " + response.body().size());
                    NoticiasAdapter adapter = new NoticiasAdapter(response.body());
                    recyclerNoticies.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Resposta buida o error");
                    Toast.makeText(Noticies.this, "No s'han trobat notícies", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e(TAG, "Error en connexió manual: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(Noticies.this, "Error manual: " + e.getMessage(), Toast.LENGTH_LONG).show());
            @Override
            public void onFailure(Call<List<Noticia>> call, Throwable t) {
                Log.e(TAG, "Error de connexió: " + t.getMessage(), t);
                Toast.makeText(Noticies.this, "Error carregant notícies: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    /*
        MÈTODE PER CARREGAR NOTÍCIES AMB RETROFIT
        =========================================
        Fa una petició a l'API de WordPress i processa la resposta.
    */
    private void carregarNoticies() {
        Toast.makeText(this, "Carregant notícies...", Toast.LENGTH_SHORT).show();

        try {
            ApiService apiService = RetrofitClient.getApiService();
            Log.d(TAG, "ApiService obtingut");

            apiService.obtenerNoticias().enqueue(new Callback<List<Noticia>>() {

                @Override
                public void onResponse(Call<List<Noticia>> call, Response<List<Noticia>> response) {
                    Log.d(TAG, "onResponse - Codi: " + response.code());

                    // Mostrar informació de la capçalera
                    Log.d(TAG, "Content-Type: " + response.headers().get("Content-Type"));

                    if (response.isSuccessful()) {
                        if (response.body() != null && !response.body().isEmpty()) {
                            Log.d(TAG, "✅ Notícies rebudes: " + response.body().size());
                            NoticiasAdapter adapter = new NoticiasAdapter(response.body());
                            recyclerNoticies.setAdapter(adapter);
                            Toast.makeText(Noticies.this, "Carregades " + response.body().size() + " notícies", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "Resposta exitosa però cos buit");
                            Toast.makeText(Noticies.this, "No s'han trobat notícies", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Error del servidor (4xx, 5xx)
                        String errorMsg = "Error del servidor: " + response.code();
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "Error body: " + errorBody);
                                errorMsg += " - " + errorBody;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error llegint errorBody", e);
                        }
                        Toast.makeText(Noticies.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Noticia>> call, Throwable t) {
                    // Error de connexió o de parsing
                    Log.e(TAG, "❌ onFailure: " + t.getMessage(), t);

                    String errorMsg = t.getMessage();
                    if (errorMsg != null && errorMsg.contains("MalformedJsonException")) {
                        Toast.makeText(Noticies.this, "Error: El servidor no retorna JSON vàlid. Comprova la URL", Toast.LENGTH_LONG).show();
                    } else if (errorMsg != null && errorMsg.contains("UnknownHostException")) {
                        Toast.makeText(Noticies.this, "Error: No s'ha trobat el servidor. Comprova la connexió a Internet", Toast.LENGTH_LONG).show();
                    } else if (errorMsg != null && errorMsg.contains("SocketTimeoutException")) {
                        Toast.makeText(Noticies.this, "Error: Temps d'espera esgotat. El servidor no respon", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Noticies.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Excepció en carregar notícies: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}