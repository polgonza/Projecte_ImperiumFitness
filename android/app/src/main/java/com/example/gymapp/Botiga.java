package com.example.gymapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
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

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

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
        tvBotigaEstat.setText(getString(R.string.botiga_carregant_productes));

        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.getProductes().enqueue(new Callback<List<ProducteDTO>>() {
            @Override
            public void onResponse(Call<List<ProducteDTO>> call, Response<List<ProducteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pintarProductes(response.body());
                } else {
                    tvBotigaEstat.setText(getString(R.string.botiga_error_carregar_productes));

                    Toast.makeText(
                            Botiga.this,
                            getString(R.string.botiga_error_carregar_productes),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProducteDTO>> call, Throwable t) {
                tvBotigaEstat.setText(getString(R.string.botiga_error_connexio));

                Toast.makeText(
                        Botiga.this,
                        getString(R.string.botiga_error_prefix, t.getMessage()),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void pintarProductes(List<ProducteDTO> productes) {
        layoutProductes.removeAllViews();

        if (productes == null || productes.isEmpty()) {
            TextView buit = new TextView(this);
            buit.setText(getString(R.string.botiga_no_productes));
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

        /*
            Nueva lógica:
            1. Si el producto viene con imatgeUrl desde backend, usamos esa imagen.
            2. Si no viene o falla, usamos la imagen hardcodeada antigua.
        */
        pintarImatgeProducte(imagen, producte);

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        TextView tvNom = new TextView(this);
        tvNom.setText(producte.getNom() != null
                ? producte.getNom()
                : getString(R.string.botiga_producte_default));
        tvNom.setTextColor(Color.WHITE);
        tvNom.setTextSize(18);
        tvNom.setTypeface(null, Typeface.BOLD);

        TextView tvPreu = new TextView(this);
        double preu = producte.getPreu() != null ? producte.getPreu() : 0.0;
        tvPreu.setText(String.format(Locale.getDefault(), "%.2f€", preu));
        tvPreu.setTextColor(Color.WHITE);
        tvPreu.setTextSize(16);
        tvPreu.setPadding(0, dp(4), 0, 0);

        TextView tvEstoc = new TextView(this);
        int estoc = producte.getEstoc() != null ? producte.getEstoc() : 0;
        tvEstoc.setText(getString(R.string.botiga_estoc, estoc));
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
        btnComprar.setText(getString(R.string.btn_comprar_ara));
        btnComprar.setTextSize(12);
        btnComprar.setTextColor(Color.WHITE);
        btnComprar.setTypeface(null, Typeface.BOLD);
        btnComprar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));

        if (estoc <= 0) {
            btnComprar.setText(getString(R.string.botiga_sense_estoc));
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

        /*
            Imagen antigua local como fallback.
        */
        intent.putExtra("imatge_producte", imatgePerProducte(producte));

        /*
            Nueva imagen real del backend.
            ProductesBotiga la podrá usar en la pantalla de detalle.
        */
        intent.putExtra("imatge_url_producte", producte.getImatgeUrl());

        startActivity(intent);
    }

    private void pintarImatgeProducte(ImageView imageView, ProducteDTO producte) {
        if (producte != null) {
            String imatgeUrl = producte.getImatgeUrl();

            if (imatgeUrl != null && !imatgeUrl.trim().isEmpty()) {
                Bitmap bitmap = convertirBase64ABitmap(imatgeUrl);

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    return;
                }
            }
        }

        imageView.setImageResource(imatgePerProducte(producte));
    }

    private Bitmap convertirBase64ABitmap(String imatgeBase64) {
        try {
            if (imatgeBase64 == null || imatgeBase64.trim().isEmpty()) {
                return null;
            }

            String base64Net = imatgeBase64.trim();

            /*
                El backend lo devuelve así:
                data:image/jpeg;base64,/9j/4AAQ...
                Quitamos la parte inicial antes de la coma.
            */
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

    private int imatgePerProducte(ProducteDTO producte) {
        if (producte == null) {
            return R.drawable.producto_2;
        }

        String nom = producte.getNom() != null
                ? normalitzar(producte.getNom())
                : "";

        String categoria = producte.getCategoria() != null
                ? normalitzar(producte.getCategoria())
                : "";

        String nomDrawable = "producto_2";

        /*
            Productos actuales:
            - Proteïna Whey 1kg / 2kg       -> proteina
            - Samarreta Imperium            -> samarreta
            - Creatina 300g                 -> creatina
            - Barra de proteïna             -> barra_proteina
            - Guants gimnàs                 -> guants
            - Bossa de gimnàs               -> bossa
            - Samarreta tècnica home/dona   -> samarreta
            - Malla esportiva               -> malla
            - Ampolla 750ml                 -> ampolla
            - Omega-3                       -> omega3
        */

        if (nom.contains("creatina")) {
            nomDrawable = "creatina";

        } else if (nom.contains("barra")) {
            nomDrawable = "barra_proteina";

        } else if (nom.contains("omega")) {
            nomDrawable = "omega3";

        } else if (nom.contains("proteina") || nom.contains("protein") || nom.contains("whey")) {
            nomDrawable = "proteina";

        } else if (nom.contains("guants") || nom.contains("guant") || nom.contains("guantes")) {
            nomDrawable = "guants";

        } else if (nom.contains("bossa") || nom.contains("bolsa") || nom.contains("motxilla") || nom.contains("mochila")) {
            nomDrawable = "bossa";

        } else if (nom.contains("malla")) {
            nomDrawable = "malla";

        } else if (nom.contains("ampolla") || nom.contains("botella")) {
            nomDrawable = "ampolla";

        } else if (nom.contains("samarreta") || nom.contains("camiseta") || nom.contains("tshirt")) {
            nomDrawable = "samarreta";

        } else if (categoria.contains("suplement")) {
            nomDrawable = "proteina";

        } else if (categoria.contains("roba") || categoria.contains("ropa")) {
            nomDrawable = "samarreta";

        } else if (categoria.contains("accesori") || categoria.contains("accesorio")) {
            nomDrawable = "guants";
        }

        return obtenirDrawableONDefault(nomDrawable);
    }

    private int obtenirDrawableONDefault(String nomDrawable) {
        int resId = getResources().getIdentifier(
                nomDrawable,
                "drawable",
                getPackageName()
        );

        if (resId == 0) {
            return R.drawable.producto_2;
        }

        return resId;
    }

    private String normalitzar(String text) {
        String normalitzat = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalitzat = normalitzat.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalitzat.toLowerCase().trim();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}