package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    PAGAMENT PRODUCTES BOTIGA ACTIVITY
    ==================================
    Pantalla de pagament simulat per a un producte comprat directament.
    Aunque el pago sea simulado, se registra la venta en backend.
*/
public class PagamentProductesBotiga extends AppCompatActivity {

    private ImageView imgProducte;
    private TextView tvNom, tvPreu, tvQuantitat;
    private EditText etNomComplet, etAdreca, etCiutat, etCodiPostal,
            etNumeroTargeta, etNomTargeta, etDataCaducitat, etCVV;
    private Button btnPagar;

    private SharedPreferences sharedPreferences;

    private Long producteId;
    private int quantitat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagament_productes_botiga);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgProducte = findViewById(R.id.imgProducte);
        tvNom = findViewById(R.id.tvNomProducte);
        tvPreu = findViewById(R.id.tvPreu);
        tvQuantitat = findViewById(R.id.tvQuantitat);
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

        long idRebut = sharedPreferences.getLong("producte_directe_id", -1);
        producteId = idRebut != -1 ? idRebut : null;

        String nom = sharedPreferences.getString("producte_directe_nom", "");
        float preu = sharedPreferences.getFloat("producte_directe_preu", 0f);
        int imatge = sharedPreferences.getInt("producte_directe_imatge", R.drawable.producto_2);
        quantitat = sharedPreferences.getInt("producte_directe_quantitat", 1);

        imgProducte.setImageResource(imatge);
        tvNom.setText(nom);
        tvPreu.setText(String.format("%.2f€", preu));
        tvQuantitat.setText(getString(R.string.pagament_quantitat_producte, quantitat));

        btnPagar.setOnClickListener(v -> ferPagament());
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

            Toast.makeText(this, getString(R.string.pagament_error_camps), Toast.LENGTH_SHORT).show();
            return;
        }

        if (producteId == null) {
            Toast.makeText(this, "Error: no s'ha trobat l'ID del producte", Toast.LENGTH_LONG).show();
            return;
        }

        Long usuariId = obtenirUsuariId();

        if (usuariId == null) {
            Toast.makeText(this, "Error: sessió no vàlida. Torna a iniciar sessió.", Toast.LENGTH_LONG).show();
            return;
        }

        btnPagar.setEnabled(false);

        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        VendaDTO vendaDTO = new VendaDTO(producteId, usuariId, quantitat);

        api.crearVenda(vendaDTO).enqueue(new Callback<VendaDTO>() {
            @Override
            public void onResponse(Call<VendaDTO> call, Response<VendaDTO> response) {
                btnPagar.setEnabled(true);

                if (response.isSuccessful()) {
                    netejarProducteDirecte();

                    Toast.makeText(
                            PagamentProductesBotiga.this,
                            "Pagament realitzat i venda registrada correctament",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(PagamentProductesBotiga.this, PagatProducteBotiga.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(
                            PagamentProductesBotiga.this,
                            "Error registrant la venda: " + llegirError(response),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<VendaDTO> call, Throwable t) {
                btnPagar.setEnabled(true);

                Toast.makeText(
                        PagamentProductesBotiga.this,
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

    private void netejarProducteDirecte() {
        sharedPreferences.edit()
                .remove("producte_directe_id")
                .remove("producte_directe_nom")
                .remove("producte_directe_preu")
                .remove("producte_directe_imatge")
                .remove("producte_directe_quantitat")
                .remove("producte_directe_descripcio")
                .apply();
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
}