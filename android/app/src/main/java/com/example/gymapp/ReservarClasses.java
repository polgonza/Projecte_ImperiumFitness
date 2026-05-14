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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservarClasses extends AppCompatActivity {

    private ImageView ivClasseImatge;
    private TextView tvClasseNom, tvUsuariReserva;
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivClasseImatge = findViewById(R.id.ivClasseImatge);
        tvClasseNom = findViewById(R.id.tvClasseNom);
        tvUsuariReserva = findViewById(R.id.tvUsuariReserva);
        spinnerSessions = findViewById(R.id.spinnerHora);
        btnConfirmarReserva = findViewById(R.id.btnConfirmarReserva);

        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("jwt_token", "");
        usuariActiu = sharedPreferences.getString("usuari_actiu", "Usuari");

        usuariId = obtenirUsuariIdDelToken();

        if (usuariId == null) {
            Toast.makeText(
                    this,
                    "Sessió no vàlida. Torna a iniciar sessió.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        String nomClasse = getIntent().getStringExtra("nom_classe");
        String sessionsSerialized = getIntent().getStringExtra("sessions");

        ivClasseImatge.setImageResource(R.drawable.logo);
        tvClasseNom.setText(nomClasse);
        tvUsuariReserva.setText(usuariActiu + ", tria una sessió de " + nomClasse);

        if (sessionsSerialized != null && !sessionsSerialized.isEmpty()) {
            parsejaSessions(sessionsSerialized);
        } else {
            Toast.makeText(this, "No hi ha sessions disponibles.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (sessionIds.isEmpty()) {
            Toast.makeText(this, "No hi ha sessions disponibles.", Toast.LENGTH_SHORT).show();
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
            String data = parts[0];
            String hora = parts[1].substring(0, 5);

            String[] dataparts = data.split("-");
            String dataFormatada = dataparts[2] + "/" + dataparts[1] + "/" + dataparts[0];

            java.util.Calendar cal = java.util.Calendar.getInstance();

            cal.set(
                    Integer.parseInt(dataparts[0]),
                    Integer.parseInt(dataparts[1]) - 1,
                    Integer.parseInt(dataparts[2])
            );

            String[] diesSetmana = {
                    "Diumenge",
                    "Dilluns",
                    "Dimarts",
                    "Dimecres",
                    "Dijous",
                    "Divendres",
                    "Dissabte"
            };

            String diaSemana = diesSetmana[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1];

            return diaSemana + " " + dataFormatada + " a les " + hora + "h";

        } catch (Exception e) {
            return horariISO;
        }
    }

    private void confirmarReserva() {
        if (sessionIds.isEmpty()) {
            Toast.makeText(this, "No hi ha sessions disponibles.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sessioSeleccionadaIndex < 0 || sessioSeleccionadaIndex >= sessionIds.size()) {
            Toast.makeText(this, "Selecciona una sessió vàlida.", Toast.LENGTH_SHORT).show();
            return;
        }

        Long idClasseSeleccionada = sessionIds.get(sessioSeleccionadaIndex);

        /*
            IMPORTANTE:
            No enviamos dataReserva.
            El backend actual no la necesita para crear la reserva.
            Así evitamos errores de formato de fecha después de los cambios del calendario.
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
                            "Reserva confirmada! ✅",
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
                            "Error fent la reserva: " + llegirError(response),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ReservaDTO> call, Throwable t) {
                btnConfirmarReserva.setEnabled(true);

                Toast.makeText(
                        ReservarClasses.this,
                        "No s'ha pogut connectar: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
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