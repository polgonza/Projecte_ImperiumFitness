package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
    CISTELLA BOTIGA ACTIVITY
    ========================
    Pantalla que mostra tots els productes afegits a la cistella.
    Permet eliminar cada producte amb una creu (X).
    Calcula el total automàticament i ofereix un botó per procedir al pagament.

    @author ImperiumGym
    @version 2.0
*/
public class CistellaBotiga extends BaseActivity {

    private RecyclerView rvCistella;
    private TextView tvTotal;
    private Button btnProcedirPagament;
    private ArrayList<ProducteCistella> cistella;
    private ProducteCistellaAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cistella_botiga);

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvCistella = findViewById(R.id.rvCistella);
        tvTotal = findViewById(R.id.tvTotal);
        btnProcedirPagament = findViewById(R.id.btnProcedirPagament);

        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        carregarCistella();

        rvCistella.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProducteCistellaAdapter();
        rvCistella.setAdapter(adapter);

        btnProcedirPagament.setOnClickListener(v -> procedirPagament());
    }

    private void carregarCistella() {
        String json = sharedPreferences.getString("cistella", "[]");
        Type type = new TypeToken<ArrayList<ProducteCistella>>(){}.getType();
        cistella = new Gson().fromJson(json, type);
        actualitzarTotal();
    }

    private void guardarCistella() {
        String json = new Gson().toJson(cistella);
        sharedPreferences.edit().putString("cistella", json).apply();
        actualitzarTotal();
        adapter.notifyDataSetChanged();
    }

    private void actualitzarTotal() {
        double total = 0;
        for (ProducteCistella p : cistella) {
            total += p.preu;
        }
        tvTotal.setText(String.format("Total: %.2f€", total));
    }

    private void procedirPagament() {
        if (cistella.isEmpty()) {
            Toast.makeText(this, "La cistella està buida", Toast.LENGTH_SHORT).show();
            return;
        }
        // Guardem la cistella per a la pantalla de pagament
        Intent intent = new Intent(this, PagamentProductesCistella.class);
        startActivity(intent);
    }

    // ----- ADAPTER PER AL RECYCLERVIEW -----
    private class ProducteCistellaAdapter extends RecyclerView.Adapter<ProducteCistellaAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cistella, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ProducteCistella producte = cistella.get(position);
            holder.ivImatge.setImageResource(producte.imatge);
            holder.tvNom.setText(producte.nom);
            holder.tvPreu.setText(String.format("%.2f€", producte.preu));
            holder.btnEliminar.setOnClickListener(v -> {
                // Confirmar eliminació
                new AlertDialog.Builder(CistellaBotiga.this)
                        .setTitle("Eliminar producte")
                        .setMessage("Vols eliminar aquest producte de la cistella?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            cistella.remove(position);
                            guardarCistella();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            return cistella.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImatge;
            TextView tvNom, tvPreu;
            Button btnEliminar;

            ViewHolder(View itemView) {
                super(itemView);
                ivImatge = itemView.findViewById(R.id.ivImatge);
                tvNom = itemView.findViewById(R.id.tvNomProducte);
                tvPreu = itemView.findViewById(R.id.tvPreuProducte);
                btnEliminar = itemView.findViewById(R.id.btnEliminar);
            }
        }
    }
}
