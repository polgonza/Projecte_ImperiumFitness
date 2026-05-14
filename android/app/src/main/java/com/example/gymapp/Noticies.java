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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    NOTICIES ACTIVITY
    =================
    Pantalla que mostra les últimes notícies del gimnàs.

    @author ImperiumGym
    @version 2.0
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

        // Carregar notícies
        carregarNoticies();
    }

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
            }

            @Override
            public void onFailure(Call<List<Noticia>> call, Throwable t) {
                Log.e(TAG, "Error de connexió: " + t.getMessage(), t);
                Toast.makeText(Noticies.this, "Error carregant notícies: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}