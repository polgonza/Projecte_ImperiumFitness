package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    CONTRASENYA USUARI ACTIVITY
    ===========================
    Pantalla per canviar la contrasenya de l'usuari.
    L'usuari introdueix la nova contrasenya i la confirmació.
    Si són iguals, s'actualitza a SharedPreferences i es torna a InfoPerfilUsuari.
    Si no, es mostra un missatge d'error.

    @author ImperiumGym
    @version 2.0
*/
public class ContrasenyaUsuari extends BaseActivity {

    private EditText etNovaPassword, etConfirmarPassword;
    private Button btnGuardarNovaPassword;
    private SharedPreferences sharedPreferences;
    private String usuariActiu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contrasenya_usuari);

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNovaPassword = findViewById(R.id.etNovaPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        btnGuardarNovaPassword = findViewById(R.id.btnGuardarNovaPassword);

        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        usuariActiu = sharedPreferences.getString("usuari_actiu", "");

        btnGuardarNovaPassword.setOnClickListener(v -> canviarContrasenya());
    }

    private void canviarContrasenya() {
        String novaPass = etNovaPassword.getText().toString().trim();
        String confirmPass = etConfirmarPassword.getText().toString().trim();

        if (TextUtils.isEmpty(novaPass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "Omple tots dos camps", Toast.LENGTH_SHORT).show();
            return;
        }

        if (novaPass.length() < 4) {
            Toast.makeText(this, "La contrasenya ha de tenir almenys 4 caràcters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!novaPass.equals(confirmPass)) {
            // Missatge d'error amb AlertDialog
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Les contrasenyes no coincideixen. Torna a provar.")
                    .setPositiveButton("D'acord", null)
                    .show();
            // Netegem els camps per tornar a escriure
            etNovaPassword.setText("");
            etConfirmarPassword.setText("");
            return;
        }

        // Guardem la nova contrasenya
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(usuariActiu + "_password", novaPass);
        editor.apply();

        Toast.makeText(this, "Contrasenya canviada correctament", Toast.LENGTH_SHORT).show();

        // Tornem a InfoPerfilUsuari
        Intent intent = new Intent(this, InfoPerfilUsuari.class);
        startActivity(intent);
        finish();
    }
}