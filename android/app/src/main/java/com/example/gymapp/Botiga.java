package com.example.gymapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Botiga extends BaseActivity {

    private LinearLayout layoutProductes;
    private TextView tvBotigaEstat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_botiga);

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layoutProductes = findViewById(R.id.layoutProductes);
        tvBotigaEstat = findViewById(R.id.tvBotigaEstat);

        LinearLayout llCistella = findViewById(R.id.llCistella);
        if (llCistella != null) {
            llCistella.setOnClickListener(v -> {
                Intent intent = new Intent(this, CistellaBotiga.class);
                startActivity(intent);
            });
        }

        carregarProductesBackend();
    }

    private void carregarProductesBackend() {
        tvBotigaEstat.setText("Carregant productes...");

        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.getProductes().enqueue(new Callback<List<ProducteDTO>>() {
            @Override
            public void onResponse(Call<List<ProducteDTO>> call, Response<List<ProducteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pintarProductes(response.body());
                } else {
                    tvBotigaEstat.setText("No s'han pogut carregar els productes.");
                    Toast.makeText(Botiga.this, "Error carregant productes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProducteDTO>> call, Throwable t) {
                tvBotigaEstat.setText("Error de connexió amb el servidor.");
                Toast.makeText(Botiga.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void pintarProductes(List<ProducteDTO> productes) {
        layoutProductes.removeAllViews();

        if (productes == null || productes.isEmpty()) {
            TextView buit = new TextView(this);
            buit.setText("No hi ha productes disponibles.");
            buit.setTextColor(Color.parseColor("#333333"));
            buit.setTextSize(16);
            buit.setGravity(Gravity.CENTER);
            buit.setPadding(dp(20), dp(20), dp(20), dp(20));
            layoutProductes.addView(buit);
            return;
        }

        for (ProducteDTO producte : productes) {
            layoutProductes.addView(crearCardProducte(producte));
        }
    }

    private CardView crearCardProducte(ProducteDTO producte) {
        CardView card = new CardView(this);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dp(12));

        card.setLayoutParams(cardParams);
        card.setCardBackgroundColor(Color.parseColor("#8E8E8E"));
        card.setRadius(dp(28));
        card.setCardElevation(0);

        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(dp(12), dp(12), dp(12), dp(12));

        ImageView imagen = new ImageView(this);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dp(96), dp(96));
        imgParams.setMargins(0, 0, dp(14), 0);
        imagen.setLayoutParams(imgParams);
        imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imagen.setImageResource(imatgePerProducte(producte));

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        TextView tvNom = new TextView(this);
        tvNom.setText(producte.getNom() != null ? producte.getNom() : "Producte");
        tvNom.setTextColor(Color.WHITE);
        tvNom.setTextSize(18);
        tvNom.setTypeface(null, Typeface.BOLD);

        TextView tvPreu = new TextView(this);
        double preu = producte.getPreu() != null ? producte.getPreu() : 0.0;
        tvPreu.setText(String.format("%.2f€", preu));
        tvPreu.setTextColor(Color.WHITE);
        tvPreu.setTextSize(16);
        tvPreu.setPadding(0, dp(4), 0, 0);

        TextView tvEstoc = new TextView(this);
        int estoc = producte.getEstoc() != null ? producte.getEstoc() : 0;
        tvEstoc.setText("Estoc: " + estoc);
        tvEstoc.setTextColor(Color.WHITE);
        tvEstoc.setTextSize(12);
        tvEstoc.setPadding(0, dp(4), 0, 0);

        Button btnComprar = new Button(this);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                dp(38)
        );
        btnParams.setMargins(0, dp(8), 0, 0);

        btnComprar.setLayoutParams(btnParams);
        btnComprar.setText("Compra ara");
        btnComprar.setTextSize(12);
        btnComprar.setTextColor(Color.WHITE);
        btnComprar.setTypeface(null, Typeface.BOLD);
        btnComprar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));

        if (estoc <= 0) {
            btnComprar.setText("Sense estoc");
            btnComprar.setEnabled(false);
        } else {
            btnComprar.setOnClickListener(v -> obrirProducte(producte));
        }

        info.addView(tvNom);
        info.addView(tvPreu);
        info.addView(tvEstoc);
        info.addView(btnComprar);

        fila.addView(imagen);
        fila.addView(info);

        card.addView(fila);

        return card;
    }

    private void obrirProducte(ProducteDTO producte) {
        Intent intent = new Intent(this, ProductesBotiga.class);

        double preu = producte.getPreu() != null ? producte.getPreu() : 0.0;

        if (producte.getId() != null) {
            intent.putExtra("id_producte", producte.getId().longValue());
        }

        intent.putExtra("nom_producte", producte.getNom());
        intent.putExtra("preu_producte", preu);
        intent.putExtra("descripcio_producte", producte.getDescripcio());
        intent.putExtra("imatge_producte", imatgePerProducte(producte));

        startActivity(intent);
    }

    private int imatgePerProducte(ProducteDTO producte) {
        String categoria = producte.getCategoria() != null
                ? producte.getCategoria().toLowerCase()
                : "";

        String nom = producte.getNom() != null
                ? producte.getNom().toLowerCase()
                : "";

        if (categoria.contains("suplement") || nom.contains("prote")) {
            return R.drawable.proteina;
        }

        if (categoria.contains("roba") ||
                categoria.contains("ropa") ||
                nom.contains("camiseta")) {
            return R.drawable.camiseta;
        }

        return R.drawable.producto_2;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}