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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    RESERVAR CLASSES ACTIVITY
    =========================
    Pantalla per confirmar la reserva d'una classe.

    Rep de Classes.java (via Intent):
    - nom_classe: nom del tipus de classe (ex: "Spinning")
    - descripcio_classe: descripció
    - sessions: string serialitzat amb totes les sessions disponibles
                Format: "id|horari;id|horari;..."
                Exemple: "1|2026-05-09T09:00:00;13|2026-05-11T09:00:00"

    El desplegable mostra totes les sessions disponibles en els pròxims 14 dies.
    L'usuari escull quina sessió vol i confirma la reserva.

    La reserva es guarda al backend associada a l'usuari via token JWT.

    @author ImperiumGym
    @version 4.0
*/
public class ReservarClasses extends AppCompatActivity {

    // Vistes
    private ImageView ivClasseImatge;
    private TextView tvClasseNom, tvUsuariReserva;
    private Spinner spinnerSessions;
    private Button btnConfirmarReserva;

    // Dades de la sessió activa (usuari loguejat)
    private String usuariActiu;
    private String token;
    private Long usuariId;
    private SharedPreferences sharedPreferences;

    // Dades de les sessions disponibles
    // Llistes paral·leles: sessionIds.get(i) correspon a sessionLabels.get(i)
    private List<Long> sessionIds = new ArrayList<>();
    private List<String> sessionHoraris = new ArrayList<>();
    private List<String> sessionLabels = new ArrayList<>();

    // Sessió seleccionada actualment al desplegable
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

        // Inicialitzem vistes
        ivClasseImatge = findViewById(R.id.ivClasseImatge);
        tvClasseNom = findViewById(R.id.tvClasseNom);
        tvUsuariReserva = findViewById(R.id.tvUsuariReserva);
        spinnerSessions = findViewById(R.id.spinnerHora); // Reutilitzem el spinner existent
        btnConfirmarReserva = findViewById(R.id.btnConfirmarReserva);

        // Llegim el token i extraiem l'userId per associar la reserva a l'usuari
        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("jwt_token", "");
        usuariActiu = sharedPreferences.getString("usuari_actiu", "Usuari");

        String userIdStr = JwtUtils.getClaim(token, "userId");
        if (userIdStr != null) {
            usuariId = Long.parseLong(userIdStr);
        } else {
            // Sense token vàlid no podem fer la reserva
            Toast.makeText(this, "Sessió no vàlida. Torna a iniciar sessió.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Recollim dades enviades per Classes.java
        String nomClasse = getIntent().getStringExtra("nom_classe");
        String sessionsSerialized = getIntent().getStringExtra("sessions");
        // Format: "id|horari;id|horari;..."

        // Configurem la imatge i el nom de la classe
        ivClasseImatge.setImageResource(R.drawable.logo);
        tvClasseNom.setText(nomClasse);
        tvUsuariReserva.setText(usuariActiu + ", tria una sessió de " + nomClasse);

        // Parsegem les sessions rebudes i omplim el desplegable
        if (sessionsSerialized != null && !sessionsSerialized.isEmpty()) {
            parsejaSessions(sessionsSerialized);
        } else {
            Toast.makeText(this, "No hi ha sessions disponibles.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurem el desplegable de sessions
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, sessionLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSessions.setAdapter(adapter);

        // Guardem l'índex seleccionat quan l'usuari canvia d'opció
        spinnerSessions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                sessioSeleccionadaIndex = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnConfirmarReserva.setOnClickListener(v -> confirmarReserva());
    }

    /*
        Parseja el string de sessions rebut de Classes.java.
        Format entrada: "1|2026-05-09T09:00:00;13|2026-05-11T09:00:00"

        Omple tres llistes paral·leles:
        - sessionIds: IDs de les classes (per fer la reserva)
        - sessionHoraris: horaris originals ISO (per guardar a la BD)
        - sessionLabels: textos llegibles pel desplegable (ex: "Divendres 09/05 a les 09:00")
    */
    private void parsejaSessions(String sessionsSerialized) {
        String[] parts = sessionsSerialized.split(";");

        for (String part : parts) {
            String[] idIHorari = part.split("\\|");
            if (idIHorari.length != 2) continue;

            try {
                Long id = Long.parseLong(idIHorari[0]);
                String horari = idIHorari[1];
                // Netegem nanosegons si n'hi ha
                if (horari.contains(".")) horari = horari.substring(0, horari.indexOf("."));

                sessionIds.add(id);
                sessionHoraris.add(horari);
                sessionLabels.add(formatarHorariPerMostrar(horari));

            } catch (Exception e) {
                continue;
            }
        }
    }

    /*
        Converteix un horari ISO a format llegible per al desplegable.
        Exemple: "2026-05-09T09:00:00" → "Dissabte 09/05 a les 09:00h"
    */
    private String formatarHorariPerMostrar(String horariISO) {
        try {
            // Separem data i hora
            String[] parts = horariISO.split("T");
            String data = parts[0]; // "2026-05-09"
            String hora = parts[1].substring(0, 5); // "09:00"

            // Convertim data a dd/MM/yyyy
            String[] dataparts = data.split("-");
            String dataFormatada = dataparts[2] + "/" + dataparts[1] + "/" + dataparts[0];

            // Obtenim el dia de la setmana
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(Integer.parseInt(dataparts[0]),
                    Integer.parseInt(dataparts[1]) - 1,
                    Integer.parseInt(dataparts[2]));

            String[] diesSetmana = {"Diumenge", "Dilluns", "Dimarts",
                    "Dimecres", "Dijous", "Divendres", "Dissabte"};
            String diaSemana = diesSetmana[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1];

            return diaSemana + " " + dataFormatada + " a les " + hora + "h";

        } catch (Exception e) {
            // Si no es pot formatar, retornem el valor original
            return horariISO;
        }
    }

    /*
        Envia la reserva al backend amb:
        - usuariId: identificador de l'usuari (del token JWT)
        - classeId: ID de la sessió seleccionada al desplegable
        - dataReserva: horari de la sessió en format ISO (de la BD)

        La reserva queda associada a l'usuari a la base de dades
        i serà visible al Calendari de reserves.
    */
    private void confirmarReserva() {
        if (sessionIds.isEmpty()) {
            Toast.makeText(this, "No hi ha sessions disponibles.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Agafem les dades de la sessió seleccionada al desplegable
        Long idClasseSeleccionada = sessionIds.get(sessioSeleccionadaIndex);
        String horariSeleccionat = sessionHoraris.get(sessioSeleccionadaIndex);

        // Creem el DTO de la reserva
        ReservaDTO reserva = new ReservaDTO(usuariId, idClasseSeleccionada, horariSeleccionat);

        // Enviem la reserva al backend
        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.crearReserva(reserva).enqueue(new Callback<ReservaDTO>() {
            @Override
            public void onResponse(Call<ReservaDTO> call, Response<ReservaDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservarClasses.this,
                            "Reserva confirmada! ✅", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ReservarClasses.this, ConfirmadaReservaClasse.class));
                    finish();
                } else {
                    Toast.makeText(ReservarClasses.this,
                            "Error fent la reserva. Torna-ho a intentar.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReservaDTO> call, Throwable t) {
                Toast.makeText(ReservarClasses.this,
                        "No s'ha pogut connectar: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}