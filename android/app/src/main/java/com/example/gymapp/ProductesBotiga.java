package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

/*
    PRODUCTES BOTIGA ACTIVITY
    =========================
    Pantalla de detall d'un producte seleccionat.
    Mostra imatge, nom, descripció i preu.
    L'usuari pot triar una quantitat i:
    - Afegir a la cistella
    - Pagar ara
*/
public class ProductesBotiga extends BaseActivity {

    private ImageView ivProducte;
    private TextView tvNom, tvDescripcio, tvPreu;
    private EditText etQuantitat;
    private Button btnAfegirCesta, btnPagarAra;

    private Long idProducte;
    private String nomProducte;
    private double preuProducte;
    private int idImatge;
    private String imatgeUrlProducte;
    private String descripcio;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productes_botiga);

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivProducte = findViewById(R.id.ivProducto);
        tvNom = findViewById(R.id.tvNombreProducto);
        tvDescripcio = findViewById(R.id.tvDescripcionProducto);
        tvPreu = findViewById(R.id.tvPrecio);
        etQuantitat = findViewById(R.id.etQuantitat);
        btnAfegirCesta = findViewById(R.id.btnAfegirCesta);
        btnPagarAra = findViewById(R.id.btnPagarAra);

        long idRebut = getIntent().getLongExtra("id_producte", -1);
        idProducte = idRebut != -1 ? idRebut : null;

        nomProducte = getIntent().getStringExtra("nom_producte");
        preuProducte = getIntent().getDoubleExtra("preu_producte", 0.0);
        idImatge = getIntent().getIntExtra("imatge_producte", R.drawable.producto_2);
        imatgeUrlProducte = getIntent().getStringExtra("imatge_url_producte");
        descripcio = getIntent().getStringExtra("descripcio_producte");

        pintarImatgeProducte();

        tvNom.setText(nomProducte != null ? nomProducte : "");
        tvDescripcio.setText(descripcio != null ? descripcio : "");
        tvPreu.setText(String.format(Locale.getDefault(), "%.2f€", preuProducte));

        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);

        btnAfegirCesta.setOnClickListener(v -> afegirACesta());
        btnPagarAra.setOnClickListener(v -> pagarAra());
    }

    private void pintarImatgeProducte() {
        if (imatgeUrlProducte != null && !imatgeUrlProducte.trim().isEmpty()) {
            Bitmap bitmap = convertirBase64ABitmap(imatgeUrlProducte);

            if (bitmap != null) {
                ivProducte.setImageBitmap(bitmap);
                return;
            }
        }

        ivProducte.setImageResource(idImatge);
    }

    private Bitmap convertirBase64ABitmap(String imatgeBase64) {
        try {
            if (imatgeBase64 == null || imatgeBase64.trim().isEmpty()) {
                return null;
            }

            String base64Net = imatgeBase64.trim();

            if (base64Net.contains(",")) {
                base64Net = base64Net.substring(base64Net.indexOf(",") + 1);
            }

            byte[] decodedBytes = Base64.decode(base64Net, Base64.DEFAULT);

            return BitmapFactory.decodeByteArray(
                    decodedBytes,
                    0,
                    decodedBytes.length
            );

        } catch (Exception e) {
            return null;
        }
    }

    private int obtenirQuantitat() {
        int quantitat;

        try {
            quantitat = Integer.parseInt(etQuantitat.getText().toString());

            if (quantitat < 1) {
                quantitat = 1;
            }

        } catch (NumberFormatException e) {
            quantitat = 1;
        }

        return quantitat;
    }

    private void afegirACesta() {
        if (idProducte == null) {
            Toast.makeText(
                    this,
                    getString(R.string.pagament_producte_error_id),
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        int quantitat = obtenirQuantitat();

        String json = sharedPreferences.getString("cistella", "[]");
        Type type = new TypeToken<ArrayList<ProducteCistella>>() {}.getType();
        ArrayList<ProducteCistella> cistella = new Gson().fromJson(json, type);

        if (cistella == null) {
            cistella = new ArrayList<>();
        }

        for (int i = 0; i < quantitat; i++) {
            cistella.add(new ProducteCistella(
                    idProducte,
                    nomProducte,
                    preuProducte,
                    idImatge,
                    imatgeUrlProducte,
                    descripcio
            ));
        }

        String newJson = new Gson().toJson(cistella);
        sharedPreferences.edit().putString("cistella", newJson).apply();

        Toast.makeText(
                this,
                getString(R.string.producte_afegit_cistella, quantitat),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void pagarAra() {
        if (idProducte == null) {
            Toast.makeText(
                    this,
                    getString(R.string.pagament_producte_error_id),
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        int quantitat = obtenirQuantitat();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("producte_directe_id", idProducte);
        editor.putString("producte_directe_nom", nomProducte);
        editor.putFloat("producte_directe_preu", (float) preuProducte);
        editor.putInt("producte_directe_imatge", idImatge);
        editor.putString("producte_directe_imatge_url", imatgeUrlProducte);
        editor.putInt("producte_directe_quantitat", quantitat);
        editor.putString("producte_directe_descripcio", descripcio);
        editor.apply();

        Intent intent = new Intent(this, PagamentProductesBotiga.class);
        startActivity(intent);
    }
}