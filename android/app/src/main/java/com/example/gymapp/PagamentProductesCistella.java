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

import java.lang.reflect.Type;
import java.util.ArrayList;

/*
    PAGAMENT PRODUCTES CISTELLA ACTIVITY
    ====================================
    Pantalla de pagament per als productes de la cistella.
    Mostra el resum de la comanda (RecyclerView) i el total.
    L'usuari ha d'omplir les dades d'enviament i targeta.
    En prémer "Pagar ara", valida els camps, buida la cistella i redirigeix a PagatProducteBotiga.

    @author ImperiumGym
    @version 2.0
*/
public class PagamentProductesCistella extends AppCompatActivity {

    private RecyclerView rvResum;
    private TextView tvTotal;
    private EditText etNomComplet, etAdreca, etCiutat, etCodiPostal,
            etNumeroTargeta, etNomTargeta, etDataCaducitat, etCVV;
    private Button btnPagar;

    private ArrayList<ProducteCistella> cistella;
    private SharedPreferences sharedPreferences;

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

        double total = 0;
        for (ProducteCistella p : cistella) total += p.preu;
        tvTotal.setText(String.format("Total: %.2f€", total));

        btnPagar.setOnClickListener(v -> ferPagament());
    }

    private void carregarCistella() {
        String json = sharedPreferences.getString("cistella", "[]");
        Type type = new TypeToken<ArrayList<ProducteCistella>>(){}.getType();
        cistella = new Gson().fromJson(json, type);
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

        // Buida la cistella
        sharedPreferences.edit().putString("cistella", "[]").apply();

        Toast.makeText(this, "Pagament realitzat amb èxit", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, PagatProducteBotiga.class);
        startActivity(intent);
        finish();
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