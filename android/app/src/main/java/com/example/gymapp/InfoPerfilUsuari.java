package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    INFO PERFIL USUARI ACTIVITY
    ============================
    Pantalla de perfil conectada al backend.

    Ahora carga:
    - Nom
    - Email
    - Pla actual / tarifa
    - Estado de la suscripción

    Endpoint usado:
    GET /api/usuaris/perfil/{id}
*/
public class InfoPerfilUsuari extends BaseActivity {

    private static final String TAG = "InfoPerfilUsuari";

    private EditText etNom, etEmailUsuari, etPassword, etPlaActual;
    private Button btnGuardarCanvis, btnCanviarPassword;
    private ImageButton btnMostrarPassword;

    private SharedPreferences sharedPreferences;
    private Long usuariId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_info_perfil_usuari);

            setupBottomNav();

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            etNom = findViewById(R.id.etNom);
            etEmailUsuari = findViewById(R.id.etEmailUsuari);
            etPassword = findViewById(R.id.etPassword);
            etPlaActual = findViewById(R.id.etPlaActual);

            btnGuardarCanvis = findViewById(R.id.btnGuardarCanvis);
            btnCanviarPassword = findViewById(R.id.btnCanviarPassword);
            btnMostrarPassword = findViewById(R.id.btnMostrarPassword);

            sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);

            usuariId = obtenirUsuariIdDelToken();

            if (usuariId == null) {
                Toast.makeText(this, "Error: sessió no vàlida. Torna a iniciar sessió.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            configurarCamps();
            configurarBotons();

            carregarDadesUsuari();

        } catch (Exception e) {
            Log.e(TAG, "ERROR en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error carregant la pantalla", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarCamps() {
        /*
            De momento solo mostramos datos.
            El perfil se recibe del backend, pero no estamos implementando editar perfil.
        */
        etNom.setEnabled(false);
        etEmailUsuari.setEnabled(false);
        etPlaActual.setEnabled(false);

        /*
            El backend NO devuelve la contraseña por seguridad.
            Por eso mostramos puntos fijos.
        */
        etPassword.setText("********");
        etPassword.setEnabled(false);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void configurarBotons() {
        /*
            Usamos este botón para refrescar datos desde backend.
            En el XML ya le hemos puesto texto "Actualitzar dades".
        */
        btnGuardarCanvis.setOnClickListener(v -> carregarDadesUsuari());

        btnCanviarPassword.setOnClickListener(v -> confirmarCanviContrasenya());

        if (btnMostrarPassword != null) {
            btnMostrarPassword.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        etPassword.setSelection(etPassword.getText().length());
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPassword.setSelection(etPassword.getText().length());
                        break;
                }
                return true;
            });
        }
    }

    private void carregarDadesUsuari() {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.getPerfil(usuariId).enqueue(new Callback<UsuariDTO>() {
            @Override
            public void onResponse(Call<UsuariDTO> call, Response<UsuariDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UsuariDTO usuari = response.body();

                    String nom = usuari.getNom() != null ? usuari.getNom() : "";
                    String email = usuari.getEmail() != null ? usuari.getEmail() : "";

                    etNom.setText(nom);
                    etEmailUsuari.setText(email);
                    etPlaActual.setText(obtenirTextPla(usuari));

                    /*
                        Guardamos también en SharedPreferences por si otras pantallas
                        quieren mostrar el nombre/email sin volver a llamar al backend.
                    */
                    sharedPreferences.edit()
                            .putString("nom_actiu", nom)
                            .putString("email_actiu", email)
                            .putString("pla_actiu", obtenirTextPla(usuari))
                            .apply();

                    Toast.makeText(
                            InfoPerfilUsuari.this,
                            "Dades carregades correctament",
                            Toast.LENGTH_SHORT
                    ).show();

                    Log.d(TAG, "Perfil carregat: " + nom + " - " + email);

                } else {
                    Toast.makeText(
                            InfoPerfilUsuari.this,
                            "Error carregant perfil: " + llegirError(response),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<UsuariDTO> call, Throwable t) {
                Toast.makeText(
                        InfoPerfilUsuari.this,
                        "Error de connexió: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

                Log.e(TAG, "Error connexió perfil", t);
            }
        });
    }

    private String obtenirTextPla(UsuariDTO usuari) {
        String tarifaNom = usuari.getTarifaNom();
        Boolean subscripcioActiva = usuari.getSubscripcioActiva();
        Boolean tarifaCancellada = usuari.getTarifaCancellada();
        String dataFi = usuari.getTarifaDataFi();

        if (tarifaNom == null || tarifaNom.trim().isEmpty()) {
            return "Sense pla assignat";
        }

        boolean activa = subscripcioActiva != null && subscripcioActiva;
        boolean cancellada = tarifaCancellada != null && tarifaCancellada;

        if (activa && cancellada) {
            if (dataFi != null && !dataFi.isEmpty()) {
                return tarifaNom + " - cancel·lat, actiu fins " + formatData(dataFi);
            }
            return tarifaNom + " - cancel·lat";
        }

        if (activa) {
            if (dataFi != null && !dataFi.isEmpty()) {
                return tarifaNom + " - actiu fins " + formatData(dataFi);
            }
            return tarifaNom + " - actiu";
        }

        return tarifaNom + " - no actiu";
    }

    private String formatData(String data) {
        /*
            El backend normalmente devuelve algo tipo:
            2026-05-13T18:30:00

            Lo convertimos a:
            13/05/2026
        */
        try {
            if (data.length() >= 10) {
                String any = data.substring(0, 4);
                String mes = data.substring(5, 7);
                String dia = data.substring(8, 10);
                return dia + "/" + mes + "/" + any;
            }
        } catch (Exception ignored) {
        }

        return data;
    }

    private Long obtenirUsuariIdDelToken() {
        String token = sharedPreferences.getString("jwt_token", "");

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

    private void confirmarCanviContrasenya() {
        new AlertDialog.Builder(this)
                .setTitle("Canviar contrasenya")
                .setMessage("Estàs segur que vols canviar la contrasenya?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    Intent intent = new Intent(this, ContrasenyaUsuari.class);
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }
}