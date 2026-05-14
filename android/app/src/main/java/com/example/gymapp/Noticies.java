package com.example.gymapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    NOTICIES ACTIVITY
    =================
    Pantalla que mostra les últimes notícies del gimnàs.

    Funcionalitat:
    - Carrega notícies des de WordPress mitjançant Retrofit
    - Mostra les notícies en un RecyclerView
    - Cada notícia conté: imatge, títol i descripció

    Hereta de BaseActivity per tenir el footer de navegació.

    @author ImperiumGym
    @version 2.0
*/
public class Noticies extends BaseActivity {

    private RecyclerView recyclerNoticies;
    private NoticiasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_noticies);

        // Configura el footer de navegació (heretat de BaseActivity)
        setupBottomNav();

        // Configura els insets per a la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ==================== INICIALITZAR RECYCLERVIEW ====================
        recyclerNoticies = findViewById(R.id.recyclerNoticies);
        recyclerNoticies.setLayoutManager(new LinearLayoutManager(this));

        // Mostrem un loading inicial
        Toast.makeText(this, "Carregant notícies...", Toast.LENGTH_SHORT).show();

        // Carreguem les notícies des de WordPress
        carregarNoticies();
    }

    /*
        MÈTODE PER CARREGAR NOTÍCIES DES DE WORDPRESS
        =============================================
        Fa una petició GET a la API de WordPress i obté totes les notícies.
        Un cop rebudes, les mostra al RecyclerView.
    */
    private void carregarNoticies() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.obtenerNoticias().enqueue(new Callback<List<Noticia>>() {

            @Override
            public void onResponse(Call<List<Noticia>> call, Response<List<Noticia>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Creem l'adaptador amb les notícies rebudes
                    adapter = new NoticiasAdapter(response.body());
                    recyclerNoticies.setAdapter(adapter);
                } else {
                    Toast.makeText(Noticies.this, "No s'han trobat notícies", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Noticia>> call, Throwable t) {
                // Error de connexió amb el servidor
                Toast.makeText(Noticies.this, "Error carregant notícies: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}