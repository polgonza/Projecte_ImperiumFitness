package com.example.gymapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    CALENDARI ACTIVITY
    ==================
    Mostra un calendari mensual amb les reserves de l'usuari loguejat.

    Sin tocar backend:
    - El calendario usa classe.horari para marcar el día real de la clase.
    - El popup muestra la capacidad de la clase.
    - El popup permite cancelar la reserva.
*/
public class Calendari extends BaseActivity {

    private GridLayout gridDies;
    private LinearLayout llDiesSetmana;
    private TextView tvMesAny, tvProperaReserva;
    private Button btnMesAnterior, btnMesSeguent;

    private int mesActual, anyActual;

    /*
        Mapa de reservas por fecha.
        Clave: "yyyy-MM-dd"
    */
    private Map<String, List<ReservaCalendari>> reservesPerData = new HashMap<>();

    private Long usuariId;

    private final String[] DIES_SETMANA = {"Dl", "Dt", "Dc", "Dj", "Dv", "Ds", "Dg"};

    private final String[] MESOS = {
            "Gener", "Febrer", "Març", "Abril", "Maig", "Juny",
            "Juliol", "Agost", "Setembre", "Octubre", "Novembre", "Desembre"
    };

    /*
        Guardamos también ClasseDTO para poder mostrar la capacidad.
    */
    private static class ReservaCalendari {
        ReservaDTO reserva;
        ClasseDTO classe;
        String nomClasse;
        String dataHora;

        ReservaCalendari(ReservaDTO reserva, ClasseDTO classe, String nomClasse, String dataHora) {
            this.reserva = reserva;
            this.classe = classe;
            this.nomClasse = nomClasse;
            this.dataHora = dataHora;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendari);
        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridDies = findViewById(R.id.gridDies);
        llDiesSetmana = findViewById(R.id.llDiesSetmana);
        tvMesAny = findViewById(R.id.tvMesAny);
        tvProperaReserva = findViewById(R.id.tvProperaReserva);
        btnMesAnterior = findViewById(R.id.btnMesAnterior);
        btnMesSeguent = findViewById(R.id.btnMesSeguent);

        java.util.Calendar ara = java.util.Calendar.getInstance(
                java.util.TimeZone.getTimeZone("Europe/Madrid")
        );

        mesActual = ara.get(java.util.Calendar.MONTH);
        anyActual = ara.get(java.util.Calendar.YEAR);

        SharedPreferences prefs = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", "");
        String userIdStr = JwtUtils.getClaim(token, "userId");

        if (userIdStr != null) {
            try {
                usuariId = Long.parseLong(userIdStr);
                carregarReservesBackend();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Sessió no vàlida.", Toast.LENGTH_SHORT).show();
                construirCapcaleraSetmana();
                construirCalendari();
            }
        } else {
            Toast.makeText(this, "Sessió no vàlida.", Toast.LENGTH_SHORT).show();
            construirCapcaleraSetmana();
            construirCalendari();
        }

        btnMesAnterior.setOnClickListener(v -> {
            mesActual--;

            if (mesActual < 0) {
                mesActual = 11;
                anyActual--;
            }

            construirCalendari();
        });

        btnMesSeguent.setOnClickListener(v -> {
            mesActual++;

            if (mesActual > 11) {
                mesActual = 0;
                anyActual++;
            }

            construirCalendari();
        });
    }

    /*
        Primero cargamos reservas.
        Después cargamos clases para saber:
        - horario real
        - nombre
        - capacidad
    */
    private void carregarReservesBackend() {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.getReservesUsuari(usuariId).enqueue(new Callback<List<ReservaDTO>>() {
            @Override
            public void onResponse(Call<List<ReservaDTO>> call, Response<List<ReservaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carregarClassesIOrganitzar(response.body());
                } else {
                    tvProperaReserva.setText("No s'han pogut carregar les reserves.");
                    construirCapcaleraSetmana();
                    construirCalendari();
                }
            }

            @Override
            public void onFailure(Call<List<ReservaDTO>> call, Throwable t) {
                Toast.makeText(
                        Calendari.this,
                        "Error de connexió: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

                construirCapcaleraSetmana();
                construirCalendari();
            }
        });
    }

    /*
        Cargamos todas las clases para poder relacionar:
        reserva.classeId -> classe.horari / classe.capacitat
    */
    private void carregarClassesIOrganitzar(List<ReservaDTO> reserves) {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.getClasses().enqueue(new Callback<List<ClasseDTO>>() {
            @Override
            public void onResponse(Call<List<ClasseDTO>> call, Response<List<ClasseDTO>> response) {
                Map<Long, ClasseDTO> classesPerId = new HashMap<>();

                if (response.isSuccessful() && response.body() != null) {
                    for (ClasseDTO classe : response.body()) {
                        if (classe.getId() != null) {
                            classesPerId.put(classe.getId(), classe);
                        }
                    }
                }

                organitzarReservesPerData(reserves, classesPerId);
                mostrarProperaReserva();

                construirCapcaleraSetmana();
                construirCalendari();
            }

            @Override
            public void onFailure(Call<List<ClasseDTO>> call, Throwable t) {
                /*
                    Si falla cargar clases, usamos dataReserva como antes.
                    No podremos mostrar capacidad en este caso.
                */
                organitzarReservesPerData(reserves, new HashMap<>());
                mostrarProperaReserva();

                construirCapcaleraSetmana();
                construirCalendari();
            }
        });
    }

    /*
        Organiza reservas por fecha usando preferiblemente classe.horari.
        Si no encuentra la clase, usa reserva.dataReserva.
    */
    private void organitzarReservesPerData(
            List<ReservaDTO> reserves,
            Map<Long, ClasseDTO> classesPerId
    ) {
        reservesPerData.clear();

        for (ReservaDTO reserva : reserves) {
            String dataHora = null;
            String nomClasse = null;

            ClasseDTO classe = null;

            if (reserva.getClasseId() != null) {
                classe = classesPerId.get(reserva.getClasseId());
            }

            if (classe != null) {
                nomClasse = classe.getNom();

                if (classe.getHorari() != null && !classe.getHorari().isEmpty()) {
                    dataHora = classe.getHorari();
                }
            }

            /*
                Fallback:
                Si no tenemos horario de clase, usamos dataReserva.
            */
            if (dataHora == null || dataHora.isEmpty()) {
                dataHora = reserva.getDataReserva();
            }

            if (nomClasse == null || nomClasse.isEmpty()) {
                if (reserva.getNomClasse() != null && !reserva.getNomClasse().isEmpty()) {
                    nomClasse = reserva.getNomClasse();
                } else {
                    nomClasse = "Classe #" + reserva.getClasseId();
                }
            }

            if (dataHora == null || dataHora.isEmpty()) {
                continue;
            }

            String clauData = obtenirClauData(dataHora);

            if (clauData == null || clauData.isEmpty()) {
                continue;
            }

            if (!reservesPerData.containsKey(clauData)) {
                reservesPerData.put(clauData, new ArrayList<>());
            }

            reservesPerData.get(clauData).add(
                    new ReservaCalendari(reserva, classe, nomClasse, dataHora)
            );
        }
    }

    private String obtenirClauData(String dataHora) {
        try {
            if (dataHora.contains("T")) {
                return dataHora.split("T")[0];
            }

            if (dataHora.contains(" ")) {
                return dataHora.split(" ")[0];
            }

            if (dataHora.length() >= 10) {
                return dataHora.substring(0, 10);
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    private void mostrarProperaReserva() {
        java.util.Calendar avui = java.util.Calendar.getInstance(
                java.util.TimeZone.getTimeZone("Europe/Madrid")
        );

        avui.set(java.util.Calendar.HOUR_OF_DAY, 0);
        avui.set(java.util.Calendar.MINUTE, 0);
        avui.set(java.util.Calendar.SECOND, 0);
        avui.set(java.util.Calendar.MILLISECOND, 0);

        ReservaCalendari propera = null;
        java.util.Calendar dataPropera = null;

        for (List<ReservaCalendari> reservesDia : reservesPerData.values()) {
            for (ReservaCalendari reservaCalendari : reservesDia) {
                try {
                    String dataHora = netejarDecimalsData(reservaCalendari.dataHora);
                    String[] parts = dataHora.split("T");
                    String[] dataParts = parts[0].split("-");

                    int any = Integer.parseInt(dataParts[0]);
                    int mes = Integer.parseInt(dataParts[1]) - 1;
                    int dia = Integer.parseInt(dataParts[2]);

                    java.util.Calendar calReserva = java.util.Calendar.getInstance();
                    calReserva.set(any, mes, dia, 0, 0, 0);
                    calReserva.set(java.util.Calendar.MILLISECOND, 0);

                    if (!calReserva.before(avui)) {
                        if (dataPropera == null || calReserva.before(dataPropera)) {
                            propera = reservaCalendari;
                            dataPropera = calReserva;
                        }
                    }

                } catch (Exception ignored) {
                }
            }
        }

        if (propera != null) {
            String dataHora = netejarDecimalsData(propera.dataHora);
            String[] parts = dataHora.split("T");

            String dataFormatada = formatarData(parts[0]);
            String hora = "";

            if (parts.length > 1 && parts[1].length() >= 5) {
                hora = parts[1].substring(0, 5);
            }

            tvProperaReserva.setText(
                    "Propera reserva: " + propera.nomClasse + " - " +
                            dataFormatada + " a les " + hora + "h"
            );

        } else if (reservesPerData.isEmpty()) {
            tvProperaReserva.setText("No tens cap reserva feta.");
        } else {
            tvProperaReserva.setText("No tens reserves properes.");
        }
    }

    private void construirCapcaleraSetmana() {
        llDiesSetmana.removeAllViews();

        for (String dia : DIES_SETMANA) {
            TextView tv = new TextView(this);
            tv.setText(dia);
            tv.setTextColor(Color.parseColor("#F0DB1A"));
            tv.setTextSize(12);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );

            tv.setLayoutParams(p);
            llDiesSetmana.addView(tv);
        }
    }

    private void construirCalendari() {
        gridDies.removeAllViews();

        tvMesAny.setText(MESOS[mesActual] + " " + anyActual);

        java.util.Calendar avui = java.util.Calendar.getInstance(
                java.util.TimeZone.getTimeZone("Europe/Madrid")
        );

        int diaAvui = avui.get(java.util.Calendar.DAY_OF_MONTH);
        int mesAvui = avui.get(java.util.Calendar.MONTH);
        int anyAvui = avui.get(java.util.Calendar.YEAR);

        java.util.Calendar primerDia = java.util.Calendar.getInstance();
        primerDia.set(anyActual, mesActual, 1);

        int diesDelMes = primerDia.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        int diaSemana = primerDia.get(java.util.Calendar.DAY_OF_WEEK);
        int offset = (diaSemana == java.util.Calendar.SUNDAY) ? 6 : diaSemana - 2;

        int amplada = (int) (getResources().getDisplayMetrics().widthPixels / 7f) - dp(2);
        int alcada = dp(42);

        for (int i = 0; i < offset; i++) {
            afegirCella("", amplada, alcada, Color.TRANSPARENT, Color.TRANSPARENT, null);
        }

        for (int dia = 1; dia <= diesDelMes; dia++) {
            String clauData = String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    anyActual,
                    mesActual + 1,
                    dia
            );

            boolean teReserves = reservesPerData.containsKey(clauData)
                    && !reservesPerData.get(clauData).isEmpty();

            boolean esAvui = dia == diaAvui
                    && mesActual == mesAvui
                    && anyActual == anyAvui;

            boolean esPassat = anyActual < anyAvui
                    || (anyActual == anyAvui && mesActual < mesAvui)
                    || (anyActual == anyAvui && mesActual == mesAvui && dia < diaAvui);

            int colorFons;
            int colorText;

            /*
                Si hay reserva, gana el amarillo.
            */
            if (teReserves) {
                colorFons = Color.parseColor("#F0DB1A");
                colorText = Color.BLACK;
            } else if (esAvui) {
                colorFons = Color.parseColor("#7B2CFF");
                colorText = Color.WHITE;
            } else if (esPassat) {
                colorFons = Color.TRANSPARENT;
                colorText = Color.parseColor("#666666");
            } else {
                colorFons = Color.TRANSPARENT;
                colorText = Color.WHITE;
            }

            final String clauFinal = clauData;
            final int diaFinal = dia;

            afegirCella(
                    String.valueOf(dia),
                    amplada,
                    alcada,
                    colorFons,
                    colorText,
                    teReserves ? v -> mostrarDetallaReserves(clauFinal, diaFinal) : null
            );
        }
    }

    private void afegirCella(
            String text,
            int amplada,
            int alcada,
            int colorFons,
            int colorText,
            android.view.View.OnClickListener listener
    ) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(13);
        tv.setTextColor(colorText);
        tv.setBackgroundColor(colorFons);

        if (!text.isEmpty() && listener != null) {
            tv.setTypeface(null, Typeface.BOLD);
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = amplada;
        params.height = alcada;
        params.setMargins(dp(1), dp(1), dp(1), dp(1));

        tv.setLayoutParams(params);

        if (listener != null) {
            tv.setOnClickListener(listener);
        }

        gridDies.addView(tv);
    }

    private void mostrarDetallaReserves(String clauData, int dia) {
        List<ReservaCalendari> reserves = reservesPerData.get(clauData);

        if (reserves == null || reserves.isEmpty()) {
            return;
        }

        String missatge = construirMissatgeReserves(reserves, clauData);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Reserves del " + formatarData(clauData))
                .setMessage(missatge)
                .setPositiveButton("Tancar", null);

        /*
            Si solo hay una reserva ese día, cancelamos directamente esa.
            Si hay varias, dejamos elegir cuál cancelar.
        */
        if (reserves.size() == 1) {
            ReservaCalendari reserva = reserves.get(0);

            builder.setNegativeButton("Cancel·lar reserva", (dialog, which) -> {
                confirmarCancelacio(reserva);
            });

        } else {
            builder.setNegativeButton("Cancel·lar una", (dialog, which) -> {
                mostrarSelectorCancelacio(reserves);
            });
        }

        builder.show();
    }

    private String construirMissatgeReserves(List<ReservaCalendari> reserves, String clauData) {
        StringBuilder missatge = new StringBuilder();

        for (ReservaCalendari reservaCalendari : reserves) {
            String dataHora = netejarDecimalsData(reservaCalendari.dataHora);
            String hora = "";

            if (dataHora.contains("T")) {
                String horaPart = dataHora.split("T")[1];

                if (horaPart.length() >= 5) {
                    hora = horaPart.substring(0, 5) + "h";
                }
            }

            missatge.append("🏋️ ").append(reservaCalendari.nomClasse).append("\n");
            missatge.append("📅 ").append(formatarData(clauData)).append("\n");
            missatge.append("🕐 ").append(hora).append("\n");

            if (reservaCalendari.classe != null && reservaCalendari.classe.getCapacitat() != null) {
                missatge.append("👥 Capacitat: ")
                        .append(reservaCalendari.classe.getCapacitat())
                        .append(" persones")
                        .append("\n");
            } else {
                missatge.append("👥 Capacitat: No disponible\n");
            }

            missatge.append("\n");
        }

        return missatge.toString().trim();
    }

    private void mostrarSelectorCancelacio(List<ReservaCalendari> reserves) {
        String[] opcions = new String[reserves.size()];

        for (int i = 0; i < reserves.size(); i++) {
            ReservaCalendari reserva = reserves.get(i);
            String hora = obtenirHora(reserva.dataHora);

            opcions[i] = reserva.nomClasse + " - " + hora;
        }

        new AlertDialog.Builder(this)
                .setTitle("Quina reserva vols cancel·lar?")
                .setItems(opcions, (dialog, which) -> confirmarCancelacio(reserves.get(which)))
                .setNegativeButton("Tornar", null)
                .show();
    }

    private void confirmarCancelacio(ReservaCalendari reservaCalendari) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel·lar reserva")
                .setMessage("Vols cancel·lar la reserva de " + reservaCalendari.nomClasse + "?")
                .setPositiveButton("Sí, cancel·lar", (dialog, which) -> cancelarReserva(reservaCalendari))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelarReserva(ReservaCalendari reservaCalendari) {
        if (reservaCalendari.reserva == null || reservaCalendari.reserva.getClasseId() == null) {
            Toast.makeText(this, "No s'ha pogut identificar la classe.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.cancelarReserva(usuariId, reservaCalendari.reserva.getClasseId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                    Calendari.this,
                                    "Reserva cancel·lada correctament",
                                    Toast.LENGTH_SHORT
                            ).show();

                            /*
                                Recargamos reservas para actualizar el calendario.
                            */
                            carregarReservesBackend();

                        } else {
                            Toast.makeText(
                                    Calendari.this,
                                    "Error cancel·lant reserva: codi " + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(
                                Calendari.this,
                                "Error de connexió: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private String obtenirHora(String dataHora) {
        try {
            String neta = netejarDecimalsData(dataHora);

            if (neta.contains("T")) {
                String horaPart = neta.split("T")[1];

                if (horaPart.length() >= 5) {
                    return horaPart.substring(0, 5) + "h";
                }
            }
        } catch (Exception ignored) {
        }

        return "";
    }

    private String netejarDecimalsData(String dataHora) {
        if (dataHora == null) {
            return "";
        }

        if (dataHora.contains(".")) {
            return dataHora.substring(0, dataHora.indexOf("."));
        }

        return dataHora;
    }

    private String formatarData(String clauData) {
        try {
            String[] parts = clauData.split("-");
            return parts[2] + "/" + parts[1] + "/" + parts[0];
        } catch (Exception e) {
            return clauData;
        }
    }

    private int dp(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}