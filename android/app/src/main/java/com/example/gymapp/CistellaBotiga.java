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
import java.text.Normalizer;
import java.util.ArrayList;

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

        Type type = new TypeToken<ArrayList<ProducteCistella>>() {}.getType();
        cistella = new Gson().fromJson(json, type);

        if (cistella == null) {
            cistella = new ArrayList<>();
        }

        actualitzarTotal();
    }

    private void guardarCistella() {
        String json = new Gson().toJson(cistella);
        sharedPreferences.edit().putString("cistella", json).apply();

        actualitzarTotal();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void actualitzarTotal() {
        double total = 0;

        if (cistella != null) {
            for (ProducteCistella p : cistella) {
                total += p.preu;
            }
        }

        tvTotal.setText(getString(R.string.cistella_total, total));
    }

    private void procedirPagament() {
        if (cistella == null || cistella.isEmpty()) {
            Toast.makeText(this, getString(R.string.cistella_buida), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, PagamentProductesCistella.class);
        startActivity(intent);
    }

    private int imatgePerProducte(ProducteCistella producte) {
        if (producte == null) {
            return R.drawable.producto_2;
        }

        String nom = producte.nom != null ? normalitzar(producte.nom) : "";

        String nomDrawable = "producto_2";

        if (nom.contains("creatina")) {
            nomDrawable = "creatina";

        } else if (nom.contains("barra")) {
            nomDrawable = "barra_proteina";

        } else if (nom.contains("omega")) {
            nomDrawable = "omega3";

        } else if (nom.contains("proteina") || nom.contains("whey")) {
            nomDrawable = "proteina";

        } else if (nom.contains("guants") || nom.contains("guant")) {
            nomDrawable = "guants";

        } else if (nom.contains("bossa") || nom.contains("bolsa") || nom.contains("motxilla")) {
            nomDrawable = "bossa";

        } else if (nom.contains("malla")) {
            nomDrawable = "malla";

        } else if (nom.contains("ampolla") || nom.contains("botella")) {
            nomDrawable = "ampolla";

        } else if (nom.contains("samarreta") || nom.contains("camiseta")) {
            nomDrawable = "samarreta";
        }

        int resId = getResources().getIdentifier(
                nomDrawable,
                "drawable",
                getPackageName()
        );

        if (resId != 0) {
            return resId;
        }

        if (producte.imatge != 0) {
            return producte.imatge;
        }

        return R.drawable.producto_2;
    }

    private String normalitzar(String text) {
        String normalitzat = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalitzat = normalitzat.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalitzat.toLowerCase().trim();
    }

    private class ProducteCistellaAdapter extends RecyclerView.Adapter<ProducteCistellaAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_cistella, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ProducteCistella producte = cistella.get(position);

            holder.ivImatge.setImageResource(imatgePerProducte(producte));
            holder.tvNom.setText(producte.nom != null ? producte.nom : "");
            holder.tvPreu.setText(String.format("%.2f€", producte.preu));

            holder.btnEliminar.setOnClickListener(v -> {
                int posicioActual = holder.getAdapterPosition();

                if (posicioActual == RecyclerView.NO_POSITION) {
                    return;
                }

                new AlertDialog.Builder(CistellaBotiga.this)
                        .setTitle(getString(R.string.cistella_eliminar_titol))
                        .setMessage(getString(R.string.cistella_eliminar_missatge))
                        .setPositiveButton(getString(R.string.si), (dialog, which) -> {
                            cistella.remove(posicioActual);
                            guardarCistella();
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            return cistella != null ? cistella.size() : 0;
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