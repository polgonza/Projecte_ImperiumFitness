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

/*
    PAGAMENT PRODUCTES BOTIGA ACTIVITY
    ==================================
    Pantalla de pagament per a un producte comprat directament.
    Mostra la informació del producte (nom, imatge, preu, quantitat).
    L'usuari ha d'omplir les dades d'enviament i targeta.
    En prémer "Pagar ara", valida els camps i redirigeix a PagatProducteBotiga.

    @author ImperiumGym
    @version 2.0
*/
public class PagamentProductesBotiga extends AppCompatActivity {

    private ImageView imgProducte;
    private TextView tvNom, tvPreu, tvQuantitat;
    private EditText etNomComplet, etAdreca, etCiutat, etCodiPostal,
            etNumeroTargeta, etNomTargeta, etDataCaducitat, etCVV;
    private Button btnPagar;

    private SharedPreferences sharedPreferences;

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

        // Carregar dades del producte directe
        String nom = sharedPreferences.getString("producte_directe_nom", "");
        float preu = sharedPreferences.getFloat("producte_directe_preu", 0f);
        int imatge = sharedPreferences.getInt("producte_directe_imatge", R.drawable.producto_2);
        int quantitat = sharedPreferences.getInt("producte_directe_quantitat", 1);
        String descripcio = sharedPreferences.getString("producte_directe_descripcio", "");

        imgProducte.setImageResource(imatge);
        tvNom.setText(nom);
        tvPreu.setText(String.format("%.2f€", preu));
        tvQuantitat.setText("Quantitat: " + quantitat);

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

            Toast.makeText(this, "Omple tots els camps de pagament", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí es faria el procés de pagament real...
        Toast.makeText(this, "Pagament realitzat amb èxit", Toast.LENGTH_SHORT).show();

        // Netejar les dades del producte directe per evitar reutilització
        sharedPreferences.edit().remove("producte_directe_nom")
                .remove("producte_directe_preu")
                .remove("producte_directe_imatge")
                .remove("producte_directe_quantitat")
                .remove("producte_directe_descripcio")
                .apply();

        Intent intent = new Intent(this, PagatProducteBotiga.class);
        startActivity(intent);
        finish();
    }
}