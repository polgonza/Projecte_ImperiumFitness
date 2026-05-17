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

public class Noticies extends BaseActivity {

    private static final String TAG = "Noticies";

    private RecyclerView recyclerNoticies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_noticies);

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });

        recyclerNoticies = findViewById(R.id.recyclerNoticies);

        recyclerNoticies.setLayoutManager(
                new LinearLayoutManager(this)
        );

        carregarNoticies();
    }

    private void carregarNoticies() {

        Toast.makeText(
                this,
                getString(R.string.noticies_carregant),
                Toast.LENGTH_SHORT
        ).show();

        ApiService apiService = RetrofitClient.getApiService();

        apiService.obtenerNoticias().enqueue(new Callback<List<Noticia>>() {

            @Override
            public void onResponse(
                    Call<List<Noticia>> call,
                    Response<List<Noticia>> response
            ) {

                Log.d(TAG, "Codi resposta: " + response.code());

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<Noticia> noticies = response.body();

                    Log.d(TAG, "Notícies rebudes: " + noticies.size());

                    NoticiasAdapter adapter =
                            new NoticiasAdapter(noticies);

                    recyclerNoticies.setAdapter(adapter);

                    if (noticies.isEmpty()) {
                        Toast.makeText(
                                Noticies.this,
                                getString(R.string.noticies_buides),
                                Toast.LENGTH_LONG
                        ).show();
                    }

                } else {

                    Toast.makeText(
                            Noticies.this,
                            getString(R.string.noticies_error_carregar),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<List<Noticia>> call,
                    Throwable t
            ) {

                Log.e(TAG, "Error Retrofit", t);

                Toast.makeText(
                        Noticies.this,
                        getString(R.string.noticies_error_connexio, t.getMessage()),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}