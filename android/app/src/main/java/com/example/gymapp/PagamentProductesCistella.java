package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    PAGAMENT PRODUCTES CISTELLA ACTIVITY
    ====================================
    Pantalla de pagament simulat per als productes de la cistella.
    Aunque el pago sea simulado, se registran las ventas en backend.
*/
public class PagamentProductesCistella extends AppCompatActivity {

    private RecyclerView rvResum;
    private TextView tvTotal;
    private EditText etNomComplet, etAdreca, etCiutat, etCodiPostal,
            etNumeroTargeta, etNomTargeta, etDataCaducitat, etCVV;
    private Button btnPagar;

    private ArrayList<ProducteCistella> cistella;
    private SharedPreferences sharedPreferences;
    private double totalComanda = 0.0;

    private static class LiniaVenda {
        Long producteId;
        int quantitat;

        LiniaVenda(Long producteId, int quantitat) {
            this.producteId = producteId;
            this.quantitat = quantitat;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagament_productes_cistella);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvResum = findViewById(R.id.rvResumComanda);
        tvTotal = findViewById(R.id.tvTotalComanda);
        etNomComplet = findViewById(R.id.etNomComplet);
        etAdreca = findViewById(R.id.etAdreca);
        etCiutat = findViewById(R.id.etCiutat);
        etCodiPostal = findViewById(R.id.etCodiPostal);
        etNumeroTargeta = findViewById(R.id.etNumeroTargeta);
        etNomTargeta = findViewById(R.id.etNomTargeta);
        etDataCaducitat = findViewById(R.id.etDataCaducitat);
        etCVV = findViewById(R.id.etCVV);
        btnPagar = findViewById(R.id.btnPagar);

        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        carregarCistella();

        rvResum.setLayoutManager(new LinearLayoutManager(this));
        rvResum.setAdapter(new ResumAdapter());

        calcularTotal();
        tvTotal.setText(String.format("Total: %.2f€", totalComanda));

        btnPagar.setOnClickListener(v -> ferPagament());
    }

    private void carregarCistella() {
        String json = sharedPreferences.getString("cistella", "[]");
        Type type = new TypeToken<ArrayList<ProducteCistella>>() {}.getType();
        cistella = new Gson().fromJson(json, type);

        if (cistella == null) {
            cistella = new ArrayList<>();
        }
    }

    private void calcularTotal() {
        totalComanda = 0.0;

        for (ProducteCistella p : cistella) {
            totalComanda += p.preu;
        }
    }

    private void ferPagament() {
        if (TextUtils.isEmpty(etNomComplet.getText()) ||
                TextUtils.isEmpty(etAdreca.getText()) ||
                TextUtils.isEmpty(etCiutat.getText()) ||
                TextUtils.isEmpty(etCodiPostal.getText()) ||
                TextUtils.isEmpty(etNumeroTargeta.getText()) ||
                TextUtils.isEmpty(etNomTargeta.getText()) ||
                TextUtils.isEmpty(etDataCaducitat.getText()) ||
                TextUtils.isEmpty(etCVV.getText())) {

            Toast.makeText(this, "Omple tots els camps de pagament", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cistella == null || cistella.isEmpty()) {
            Toast.makeText(this, "La cistella està buida", Toast.LENGTH_SHORT).show();
            return;
        }

        Long usuariId = obtenirUsuariId();

        if (usuariId == null) {
            Toast.makeText(this, "Error: sessió no vàlida. Torna a iniciar sessió.", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<LiniaVenda> linies = crearLiniesVenda();

        if (linies.isEmpty()) {
            Toast.makeText(
                    this,
                    "Error: hi ha productes sense ID. Buida la cistella i torna a afegir-los.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        btnPagar.setEnabled(false);

        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        crearVendesSequencialment(api, usuariId, linies, 0);
    }

    private ArrayList<LiniaVenda> crearLiniesVenda() {
        Map<Long, Integer> quantitats = new HashMap<>();

        for (ProducteCistella p : cistella) {
            if (p.producteId == null || p.producteId <= 0) {
                return new ArrayList<>();
            }

            int quantitatActual = quantitats.containsKey(p.producteId)
                    ? quantitats.get(p.producteId)
                    : 0;

            quantitats.put(p.producteId, quantitatActual + 1);
        }

        ArrayList<LiniaVenda> linies = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : quantitats.entrySet()) {
            linies.add(new LiniaVenda(entry.getKey(), entry.getValue()));
        }

        return linies;
    }

    private void crearVendesSequencialment(ApiService api, Long usuariId, ArrayList<LiniaVenda> linies, int index) {
        if (index >= linies.size()) {
            sharedPreferences.edit().putString("cistella", "[]").apply();

            Toast.makeText(
                    PagamentProductesCistella.this,
                    "Pagament simulat i venda registrada correctament",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(PagamentProductesCistella.this, PagatProducteBotiga.class);
            startActivity(intent);
            finish();
            return;
        }

        LiniaVenda linia = linies.get(index);
        VendaDTO vendaDTO = new VendaDTO(linia.producteId, usuariId, linia.quantitat);

        api.crearVenda(vendaDTO).enqueue(new Callback<VendaDTO>() {
            @Override
            public void onResponse(Call<VendaDTO> call, Response<VendaDTO> response) {
                if (response.isSuccessful()) {
                    crearVendesSequencialment(api, usuariId, linies, index + 1);
                } else {
                    btnPagar.setEnabled(true);

                    Toast.makeText(
                            PagamentProductesCistella.this,
                            "Error registrant la venda: " + llegirError(response),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<VendaDTO> call, Throwable t) {
                btnPagar.setEnabled(true);

                Toast.makeText(
                        PagamentProductesCistella.this,
                        "Error de connexió: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private Long obtenirUsuariId() {
        String token = sharedPreferences.getString("jwt_token", "");
        String userIdStr = JwtUtils.getClaim(token, "userId");

        try {
            return userIdStr != null ? Long.parseLong(userIdStr) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String llegirError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                return response.errorBody().string();
            }
        } catch (IOException ignored) {
        }

        return "codi " + response.code();
    }

    private class ResumAdapter extends RecyclerView.Adapter<ResumAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resum_comanda, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ProducteCistella p = cistella.get(position);
            holder.ivImatge.setImageResource(p.imatge);
            holder.tvNom.setText(p.nom);
            holder.tvPreu.setText(String.format("%.2f€", p.preu));
        }

        @Override
        public int getItemCount() {
            return cistella.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImatge;
            TextView tvNom, tvPreu;

            ViewHolder(View itemView) {
                super(itemView);
                ivImatge = itemView.findViewById(R.id.ivImatge);
                tvNom = itemView.findViewById(R.id.tvNomProducte);
                tvPreu = itemView.findViewById(R.id.tvPreuProducte);
            }
        }
    }
}