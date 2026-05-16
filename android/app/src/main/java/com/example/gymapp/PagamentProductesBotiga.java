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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    PAGAMENT PRODUCTES BOTIGA ACTIVITY
    ==================================
    Pantalla de pagament simulat per a un producte comprat directament.
    Aunque el pago sea simulado, se registra la venta en backend.
*/
public class PagamentProductesBotiga extends BaseActivity {

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

        setupBottomNav();

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
        tvPreu.setText(String.format(Locale.getDefault(), "%.2f€", preu));
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

            Toast.makeText(
                    this,
                    getString(R.string.pagament_error_camps),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (producteId == null) {
            Toast.makeText(
                    this,
                    getString(R.string.pagament_producte_error_id),
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        Long usuariId = obtenirUsuariId();

        if (usuariId == null) {
            Toast.makeText(
                    this,
                    getString(R.string.pagament_error_sessio),
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        btnPagar.setEnabled(false);

        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        /*
            Primero comprobamos stock real.
            Así mostramos mensaje claro antes de intentar registrar la venta.
        */
        comprovarStockIComprar(api, usuariId);
    }

    private void comprovarStockIComprar(ApiService api, Long usuariId) {
        api.getProductes().enqueue(new Callback<List<ProducteDTO>>() {
            @Override
            public void onResponse(Call<List<ProducteDTO>> call, Response<List<ProducteDTO>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    btnPagar.setEnabled(true);

                    Toast.makeText(
                            PagamentProductesBotiga.this,
                            getString(
                                    R.string.pagament_error_venda,
                                    getString(R.string.pagament_error_codi, response.code())
                            ),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                Integer stockActual = null;

                for (ProducteDTO producte : response.body()) {
                    if (producte.getId() != null
                            && producte.getId().longValue() == producteId.longValue()) {

                        stockActual = producte.getEstoc() != null ? producte.getEstoc() : 0;
                        break;
                    }
                }

                if (stockActual == null || stockActual < quantitat) {
                    btnPagar.setEnabled(true);

                    Toast.makeText(
                            PagamentProductesBotiga.this,
                            getString(R.string.pagament_error_estoc_insuficient),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                registrarVenda(api, usuariId);
            }

            @Override
            public void onFailure(Call<List<ProducteDTO>> call, Throwable t) {
                btnPagar.setEnabled(true);

                Toast.makeText(
                        PagamentProductesBotiga.this,
                        getString(R.string.pagament_error_connexio, t.getMessage()),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void registrarVenda(ApiService api, Long usuariId) {
        VendaDTO vendaDTO = new VendaDTO(producteId, usuariId, quantitat);

        api.crearVenda(vendaDTO).enqueue(new Callback<VendaDTO>() {
            @Override
            public void onResponse(Call<VendaDTO> call, Response<VendaDTO> response) {
                btnPagar.setEnabled(true);

                if (response.isSuccessful()) {
                    netejarProducteDirecte();

                    Toast.makeText(
                            PagamentProductesBotiga.this,
                            getString(R.string.pagament_venda_correcta),
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            PagamentProductesBotiga.this,
                            PagatProducteBotiga.class
                    );

                    startActivity(intent);
                    finish();

                }  else {
                    String errorBackend = llegirError(response);

                    if (esErrorEstocInsuficient(response, errorBackend)) {
                        Toast.makeText(
                                PagamentProductesBotiga.this,
                                getString(R.string.pagament_error_estoc_insuficient),
                                Toast.LENGTH_LONG
                        ).show();

                    } else {
                        Toast.makeText(
                                PagamentProductesBotiga.this,
                                getString(R.string.pagament_error_venda, errorBackend),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VendaDTO> call, Throwable t) {
                btnPagar.setEnabled(true);

                Toast.makeText(
                        PagamentProductesBotiga.this,
                        getString(R.string.pagament_error_connexio, t.getMessage()),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private boolean esErrorEstocInsuficient(Response<?> response, String errorBackend) {
        if (response.code() == 409) {
            return true;
        }

        if (errorBackend == null) {
            return false;
        }

        String error = errorBackend.toLowerCase(Locale.ROOT);

        return error.contains("estoc insuficient")
                || error.contains("stock insuficiente")
                || error.contains("not enough stock")
                || error.contains("insufficient stock")
                || error.contains("estoc")
                || error.contains("stock");
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

        return getString(R.string.pagament_error_codi, response.code());
    }
}