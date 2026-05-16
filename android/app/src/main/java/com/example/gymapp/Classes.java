package com.example.gymapp;

import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Classes extends BaseActivity {

    private LinearLayout llContainer;
    private List<ClasseDTO> totesLesClasses = new ArrayList<>();
    private String categoriaActiva = "TOTES";

    private Button btnToutes, btnCardio, btnForca, btnFlexibilitat;

    private final int[] colors = {
            Color.parseColor("#808080"),
            Color.parseColor("#000000"),
            Color.parseColor("#808080"),
            Color.parseColor("#000000")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_classes);
        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        llContainer = findViewById(R.id.llContainerClasses);
        btnToutes = findViewById(R.id.btnFiltreToutes);
        btnCardio = findViewById(R.id.btnFiltreCardio);
        btnForca = findViewById(R.id.btnFiltreForça);
        btnFlexibilitat = findViewById(R.id.btnFiltreFlexibilitat);

        configurarFiltres();
        carregarClasses();
    }

    private void configurarFiltres() {
        btnToutes.setOnClickListener(v -> aplicarFiltre("TOTES", btnToutes));
        btnCardio.setOnClickListener(v -> aplicarFiltre("CARDIO", btnCardio));
        btnForca.setOnClickListener(v -> aplicarFiltre("FORCA", btnForca));
        btnFlexibilitat.setOnClickListener(v -> aplicarFiltre("FLEXIBILITAT", btnFlexibilitat));
    }

    private void aplicarFiltre(String categoria, Button btnActiu) {
        categoriaActiva = categoria;

        int[] idsBotonsFiltre = {
                R.id.btnFiltreToutes,
                R.id.btnFiltreCardio,
                R.id.btnFiltreForça,
                R.id.btnFiltreFlexibilitat
        };

        for (int id : idsBotonsFiltre) {
            Button btn = findViewById(id);

            if (btn != null) {
                btn.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(Color.parseColor("#555555"))
                );
            }
        }

        btnActiu.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#7B2CFF"))
        );

        mostrarClassesFiltrades();
    }

    private void carregarClasses() {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.getClasses().enqueue(new Callback<List<ClasseDTO>>() {
            @Override
            public void onResponse(Call<List<ClasseDTO>> call, Response<List<ClasseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totesLesClasses = response.body();
                    mostrarClassesFiltrades();
                } else {
                    Toast.makeText(
                            Classes.this,
                            getString(R.string.classes_error_carregar),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<ClasseDTO>> call, Throwable t) {
                Toast.makeText(
                        Classes.this,
                        getString(R.string.classes_error_connexio, t.getMessage()),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void mostrarClassesFiltrades() {
        llContainer.removeAllViews();

        TimeZone madrid = TimeZone.getTimeZone("Europe/Madrid");

        Calendar avui = Calendar.getInstance(madrid);
        avui.set(Calendar.HOUR_OF_DAY, 0);
        avui.set(Calendar.MINUTE, 0);
        avui.set(Calendar.SECOND, 0);
        avui.set(Calendar.MILLISECOND, 0);

        Calendar limitMaxim = Calendar.getInstance(madrid);
        limitMaxim.add(Calendar.DAY_OF_YEAR, 14);
        limitMaxim.set(Calendar.HOUR_OF_DAY, 23);
        limitMaxim.set(Calendar.MINUTE, 59);
        limitMaxim.set(Calendar.SECOND, 59);
        limitMaxim.set(Calendar.MILLISECOND, 999);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(madrid);

        Map<String, List<ClasseDTO>> agrupades = new LinkedHashMap<>();

        for (ClasseDTO classe : totesLesClasses) {
            String categoriaClasse = getCategoriaDeClasse(classe.getNom());

            if (!categoriaActiva.equals("TOTES") && !categoriaClasse.equals(categoriaActiva)) {
                continue;
            }

            if (classe.getHorari() == null || classe.getHorari().isEmpty()) {
                continue;
            }

            try {
                String horariNet = classe.getHorari();

                if (horariNet.contains(".")) {
                    horariNet = horariNet.substring(0, horariNet.indexOf("."));
                }

                java.util.Date dataClasse = sdf.parse(horariNet);

                if (dataClasse == null) {
                    continue;
                }

                Calendar calClasse = Calendar.getInstance(madrid);
                calClasse.setTime(dataClasse);

                if (calClasse.before(avui) || calClasse.after(limitMaxim)) {
                    continue;
                }

                String clauNom = classe.getNom() != null
                        ? classe.getNom().toLowerCase().trim()
                        : "";

                if (clauNom.isEmpty()) {
                    continue;
                }

                if (!agrupades.containsKey(clauNom)) {
                    agrupades.put(clauNom, new ArrayList<>());
                }

                agrupades.get(clauNom).add(classe);

            } catch (Exception ignored) {
            }
        }

        if (agrupades.isEmpty()) {
            TextView tvBuit = new TextView(this);
            tvBuit.setText(getString(R.string.classes_buit_14_dies));
            tvBuit.setTextColor(Color.LTGRAY);
            tvBuit.setTextSize(14);
            tvBuit.setPadding(0, dp(24), 0, 0);
            llContainer.addView(tvBuit);
            return;
        }

        int index = 0;

        for (Map.Entry<String, List<ClasseDTO>> entrada : agrupades.entrySet()) {
            List<ClasseDTO> sessions = entrada.getValue();

            if (sessions == null || sessions.isEmpty()) {
                continue;
            }

            ClasseDTO representant = sessions.get(0);
            afegirTargetaClasse(representant, sessions, index);
            index++;
        }
    }

    private void afegirTargetaClasse(ClasseDTO representant, List<ClasseDTO> sessions, int index) {
        LinearLayout targeta = new LinearLayout(this);
        targeta.setOrientation(LinearLayout.HORIZONTAL);
        targeta.setBackgroundColor(colors[index % colors.length]);
        targeta.setPadding(dp(12), dp(12), dp(12), dp(12));
        targeta.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams paramsTargeta = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        paramsTargeta.setMargins(0, 0, 0, dp(12));
        targeta.setLayoutParams(paramsTargeta);

        ImageView imatge = new ImageView(this);
        imatge.setImageResource(
                ImatgeClasseHelper.obtenirImatgeClasse(this, representant.getNom())
        );
        imatge.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams paramsImg = new LinearLayout.LayoutParams(dp(100), dp(100));
        paramsImg.setMarginEnd(dp(12));
        imatge.setLayoutParams(paramsImg);

        LinearLayout columna = new LinearLayout(this);
        columna.setOrientation(LinearLayout.VERTICAL);
        columna.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));

        TextView tvNom = new TextView(this);
        tvNom.setText(representant.getNom() != null ? representant.getNom() : "");
        tvNom.setTextColor(Color.WHITE);
        tvNom.setTextSize(20);
        tvNom.setTypeface(null, Typeface.BOLD);

        TextView tvCategoria = new TextView(this);
        tvCategoria.setText(getNomCategoria(getCategoriaDeClasse(representant.getNom())));
        tvCategoria.setTextColor(Color.parseColor("#FFC107"));
        tvCategoria.setTextSize(11);
        tvCategoria.setTypeface(null, Typeface.ITALIC);

        TextView tvDesc = new TextView(this);
        tvDesc.setText(obtenirDescripcioClasse(representant.getNom()));
        tvDesc.setTextColor(Color.LTGRAY);
        tvDesc.setTextSize(12);
        tvDesc.setMaxLines(2);

        TextView tvSessions = new TextView(this);
        tvSessions.setText(getString(R.string.classes_sessions_disponibles, sessions.size()));
        tvSessions.setTextColor(Color.parseColor("#7B2CFF"));
        tvSessions.setTextSize(12);
        tvSessions.setTypeface(null, Typeface.BOLD);

        Button btnReserva = new Button(this);
        btnReserva.setText(getString(R.string.btn_reserva_ara));
        btnReserva.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#FFC107"))
        );
        btnReserva.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        paramsBtn.topMargin = dp(8);
        btnReserva.setLayoutParams(paramsBtn);
        btnReserva.setOnClickListener(v -> obrirReserva(representant, sessions));

        columna.addView(tvNom);
        columna.addView(tvCategoria);
        columna.addView(tvDesc);
        columna.addView(tvSessions);
        columna.addView(btnReserva);

        targeta.addView(imatge);
        targeta.addView(columna);

        llContainer.addView(targeta);
    }

    private String getCategoriaDeClasse(String nomClasse) {
        if (nomClasse == null) {
            return "ALTRES";
        }

        String nom = nomClasse.toLowerCase();

        if (nom.contains("spinning") || nom.contains("spining") || nom.contains("zumba") || nom.contains("crossfit")) {
            return "CARDIO";
        }

        if (nom.contains("body pump") || nom.contains("bodypump") || nom.contains("hiit")) {
            return "FORCA";
        }

        if (nom.contains("yoga") || nom.contains("pilates")) {
            return "FLEXIBILITAT";
        }

        return "ALTRES";
    }

    private String getNomCategoria(String codi) {
        switch (codi) {
            case "CARDIO":
                return getString(R.string.classes_categoria_cardio);

            case "FORCA":
                return getString(R.string.classes_categoria_forca);

            case "FLEXIBILITAT":
                return getString(R.string.classes_categoria_flexibilitat_llarga);

            default:
                return getString(R.string.classes_categoria_altres);
        }
    }

    private void obrirReserva(ClasseDTO representant, List<ClasseDTO> sessions) {
        Intent intent = new Intent(this, ReservarClasses.class);

        intent.putExtra("nom_classe", representant.getNom());

        intent.putExtra(
                "descripcio_classe",
                representant.getDescripcio() != null ? representant.getDescripcio() : ""
        );

        intent.putExtra(
                "imatge_classe",
                ImatgeClasseHelper.obtenirImatgeClasse(this, representant.getNom())
        );

        StringBuilder sb = new StringBuilder();

        for (ClasseDTO s : sessions) {
            if (sb.length() > 0) {
                sb.append(";");
            }

            sb.append(s.getId()).append("|").append(s.getHorari());
        }

        intent.putExtra("sessions", sb.toString());

        startActivity(intent);
    }

    private String obtenirDescripcioClasse(String nomClasse) {
        if (nomClasse == null) {
            return getString(R.string.classe_desc_default);
        }

        String nom = java.text.Normalizer.normalize(nomClasse, java.text.Normalizer.Form.NFD);
        nom = nom.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        nom = nom.toLowerCase().trim();

        if (nom.contains("zumba")) {
            return getString(R.string.classe_desc_zumba);
        }

        if (nom.contains("spinning") || nom.contains("spining") || nom.contains("ciclisme")) {
            return getString(R.string.classe_desc_spinning);
        }

        if (nom.contains("body pump") || nom.contains("bodypump") || nom.contains("pump")) {
            return getString(R.string.classe_desc_bodypump);
        }

        if (nom.contains("yoga")) {
            return getString(R.string.classe_desc_yoga);
        }

        if (nom.contains("pilates")) {
            return getString(R.string.classe_desc_pilates);
        }

        if (nom.contains("crossfit") || nom.contains("cross fit")) {
            return getString(R.string.classe_desc_crossfit);
        }

        if (nom.contains("hiit")) {
            return getString(R.string.classe_desc_hiit);
        }

        return getString(R.string.classe_desc_default);
    }
    private int dp(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}