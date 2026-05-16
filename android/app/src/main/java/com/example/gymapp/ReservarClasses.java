package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservarClasses extends BaseActivity {

    private ImageView ivClasseImatge;
    private TextView tvClasseNom, tvClasseDescripcio, tvUsuariReserva;
    private Spinner spinnerSessions;
    private Button btnConfirmarReserva;

    private String usuariActiu;
    private String token;
    private Long usuariId;
    private SharedPreferences sharedPreferences;

    private List<Long> sessionIds = new ArrayList<>();
    private List<String> sessionHoraris = new ArrayList<>();
    private List<String> sessionLabels = new ArrayList<>();

    private int sessioSeleccionadaIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservar_classes);

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivClasseImatge = findViewById(R.id.ivClasseImatge);
        tvClasseNom = findViewById(R.id.tvClasseNom);
        tvClasseDescripcio = findViewById(R.id.tvClasseDescripcio);
        tvUsuariReserva = findViewById(R.id.tvUsuariReserva);
        spinnerSessions = findViewById(R.id.spinnerHora);
        btnConfirmarReserva = findViewById(R.id.btnConfirmarReserva);

        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("jwt_token", "");
        usuariActiu = sharedPreferences.getString(
                "usuari_actiu",
                getString(R.string.usuari_default)
        );

        usuariId = obtenirUsuariIdDelToken();

        if (usuariId == null) {
            Toast.makeText(
                    this,
                    getString(R.string.reserva_sessio_invalida),
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        String nomClasse = getIntent().getStringExtra("nom_classe");
        String sessionsSerialized = getIntent().getStringExtra("sessions");
        int imatgeClasse = getIntent().getIntExtra("imatge_classe", R.drawable.logo);

        ivClasseImatge.setImageResource(imatgeClasse);
        tvClasseNom.setText(nomClasse != null ? nomClasse : "");

        /*
            Descripción hardcodeada según el nombre de la clase.
        */
        tvClasseDescripcio.setText(obtenirDescripcioClasse(nomClasse));

        tvUsuariReserva.setText(
                getString(
                        R.string.reserva_tria_sessio,
                        usuariActiu,
                        nomClasse != null ? nomClasse : ""
                )
        );

        if (sessionsSerialized != null && !sessionsSerialized.isEmpty()) {
            parsejaSessions(sessionsSerialized);
        } else {
            Toast.makeText(
                    this,
                    getString(R.string.reserva_no_sessions),
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        if (sessionIds.isEmpty()) {
            Toast.makeText(
                    this,
                    getString(R.string.reserva_no_sessions),
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                sessionLabels
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSessions.setAdapter(adapter);

        spinnerSessions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                    AdapterView<?> parent,
                    android.view.View view,
                    int position,
                    long id
            ) {
                sessioSeleccionadaIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnConfirmarReserva.setOnClickListener(v -> confirmarReserva());
    }

    private void parsejaSessions(String sessionsSerialized) {
        String[] parts = sessionsSerialized.split(";");

        for (String part : parts) {
            String[] idIHorari = part.split("\\|");

            if (idIHorari.length != 2) {
                continue;
            }

            try {
                Long id = Long.parseLong(idIHorari[0]);
                String horari = idIHorari[1];

                if (horari.contains(".")) {
                    horari = horari.substring(0, horari.indexOf("."));
                }

                sessionIds.add(id);
                sessionHoraris.add(horari);
                sessionLabels.add(formatarHorariPerMostrar(horari));

            } catch (Exception ignored) {
            }
        }
    }

    private String formatarHorariPerMostrar(String horariISO) {
        try {
            String[] parts = horariISO.split("T");

            if (parts.length < 2) {
                return horariISO;
            }

            String data = parts[0];
            String hora = parts[1].substring(0, 5);

            String[] dataParts = data.split("-");

            if (dataParts.length < 3) {
                return horariISO;
            }

            String dataFormatada = dataParts[2] + "/" + dataParts[1] + "/" + dataParts[0];

            java.util.Calendar cal = java.util.Calendar.getInstance();

            cal.set(
                    Integer.parseInt(dataParts[0]),
                    Integer.parseInt(dataParts[1]) - 1,
                    Integer.parseInt(dataParts[2])
            );

            String[] diesSetmana = getResources().getStringArray(R.array.dies_setmana_llargs);
            String diaSetmana = diesSetmana[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1];

            return getString(
                    R.string.reserva_format_horari,
                    diaSetmana,
                    dataFormatada,
                    hora
            );

        } catch (Exception e) {
            return horariISO;
        }
    }

    private void confirmarReserva() {
        if (sessionIds.isEmpty()) {
            Toast.makeText(
                    this,
                    getString(R.string.reserva_no_sessions),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (sessioSeleccionadaIndex < 0 || sessioSeleccionadaIndex >= sessionIds.size()) {
            Toast.makeText(
                    this,
                    getString(R.string.reserva_selecciona_sessio_valida),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Long idClasseSeleccionada = sessionIds.get(sessioSeleccionadaIndex);

        /*
            No enviamos dataReserva.
            El backend actual crea la reserva con usuariId + classeId.
        */
        ReservaDTO reserva = new ReservaDTO(usuariId, idClasseSeleccionada);

        btnConfirmarReserva.setEnabled(false);

        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.crearReserva(reserva).enqueue(new Callback<ReservaDTO>() {
            @Override
            public void onResponse(Call<ReservaDTO> call, Response<ReservaDTO> response) {
                btnConfirmarReserva.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(
                            ReservarClasses.this,
                            getString(R.string.reserva_confirmada_ok),
                            Toast.LENGTH_SHORT
                    ).show();

                    startActivity(new Intent(
                            ReservarClasses.this,
                            ConfirmadaReservaClasse.class
                    ));

                    finish();

                } else {
                    Toast.makeText(
                            ReservarClasses.this,
                            getString(R.string.reserva_error_backend, llegirError(response)),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ReservaDTO> call, Throwable t) {
                btnConfirmarReserva.setEnabled(true);

                Toast.makeText(
                        ReservarClasses.this,
                        getString(R.string.reserva_error_connexio, t.getMessage()),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private String obtenirDescripcioClasse(String nomClasse) {
        if (nomClasse == null) {
            return getString(R.string.classe_desc_default);
        }

        String nom = normalitzar(nomClasse);

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

    private String normalitzar(String text) {
        String normalitzat = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalitzat = normalitzat.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalitzat.toLowerCase().trim();
    }

    private Long obtenirUsuariIdDelToken() {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }

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
}