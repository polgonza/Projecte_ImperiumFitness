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

/*
    INFO PERFIL USUARI ACTIVITY
    ============================
    Pantalla amb la informació del perfil de l'usuari.
    Mostra les dades de l'usuari actiu (llegides de SharedPreferences):
    - Nom
    - Email
    - Contrasenya (mostrada amb punts, amb opció de veure-la)

    Permet editar el nom i l'email.
    El botó "Guardar canvis" actualitza les dades.
    El botó "Canviar contrasenya" obre un diàleg de confirmació
    i després la pantalla ContrasenyaUsuari.

    @author ImperiumGym
    @version 3.0
*/
public class InfoPerfilUsuari extends BaseActivity {

    private static final String TAG = "InfoPerfilUsuari";

    // Variables per als camps del formulari (només 3 camps)
    private EditText etNom, etEmailUsuari, etPassword;
    private Button btnGuardarCanvis, btnCanviarPassword;
    private ImageButton btnMostrarPassword;
    private SharedPreferences sharedPreferences;
    private String usuariActiu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_info_perfil_usuari);

            // Configura el footer de navegació
            setupBottomNav();

            // Configura els insets per a la vista principal
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Inicialitza els components del layout (només 3 camps)
            etNom = findViewById(R.id.etNom);
            etEmailUsuari = findViewById(R.id.etEmailUsuari);
            etPassword = findViewById(R.id.etPassword);
            btnGuardarCanvis = findViewById(R.id.btnGuardarCanvis);
            btnCanviarPassword = findViewById(R.id.btnCanviarPassword);
            btnMostrarPassword = findViewById(R.id.btnMostrarPassword);

            // Comprova que els components no siguin null (per depuració)
            if (etNom == null) Log.e(TAG, "etNom és NULL!");
            if (etEmailUsuari == null) Log.e(TAG, "etEmailUsuari és NULL!");
            if (etPassword == null) Log.e(TAG, "etPassword és NULL!");

            // Carrega les SharedPreferences i l'usuari actiu
            sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
            usuariActiu = sharedPreferences.getString("usuari_actiu", "");

            if (usuariActiu.isEmpty()) {
                Toast.makeText(this, "Error: No hi ha sessió activa", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Carrega les dades de l'usuari als camps
            carregarDadesUsuari();

            /*
                FUNCIONALITAT DEL BOTÓ MOSTRAR CONTRASENYA
                ==========================================
                Quan l'usuari prem i manté premut el botó (ACTION_DOWN),
                la contrasenya es mostra en text clar.
                Quan deixa de prémer (ACTION_UP), torna a mostrar punts.
            */
            if (btnMostrarPassword != null) {
                btnMostrarPassword.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Quan es prem: mostrar la contrasenya en text clar
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                            etPassword.setSelection(etPassword.getText().length());
                            break;
                        case MotionEvent.ACTION_UP:
                            // Quan es deixa de prémer: tornar a mostrar punts
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPassword.setSelection(etPassword.getText().length());
                            break;
                    }
                    return true;
                });
            }

            // Listeners per als botons
            btnGuardarCanvis.setOnClickListener(v -> guardarCanvis());
            btnCanviarPassword.setOnClickListener(v -> confirmarCanviContrasenya());

        } catch (Exception e) {
            Log.e(TAG, "ERROR en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error carregant la pantalla", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        MÈTODE PER CARREGAR LES DADES DE L'USUARI
        =========================================
        Llegeix de SharedPreferences les dades de l'usuari actiu
        i les mostra als camps EditText.
        Només carrega: Nom, Email i Contrasenya.
    */
    private void carregarDadesUsuari() {
        String nom = sharedPreferences.getString(usuariActiu + "_nom", "");
        String email = sharedPreferences.getString(usuariActiu + "_email", "");
        String password = sharedPreferences.getString(usuariActiu + "_password", "");

        etNom.setText(nom);
        etEmailUsuari.setText(email);
        etPassword.setText(password);

        Log.d(TAG, "Dades carregades - Nom: " + nom + ", Email: " + email);
    }

    /*
        MÈTODE PER GUARDAR ELS CANVIS
        =============================
        Valida que els camps no estiguin buits i actualitza
        les dades de l'usuari a SharedPreferences.
        Només guarda: Nom i Email (la contrasenya es canvia a una altra pantalla)
    */
    private void guardarCanvis() {
        String nom = etNom.getText().toString().trim();
        String email = etEmailUsuari.getText().toString().trim();

        // Validació: comprova que cap camp estigui buit
        if (nom.isEmpty()) {
            Toast.makeText(this, "Introdueix el teu nom", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Introdueix el teu email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validació bàsica d'email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Introdueix un email vàlid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guarda les dades actualitzades
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(usuariActiu + "_nom", nom);
        editor.putString(usuariActiu + "_email", email);
        editor.apply();

        Toast.makeText(this, "Dades actualitzades correctament", Toast.LENGTH_SHORT).show();
    }

    /*
        MÈTODE PER CONFIRMAR CANVI DE CONTRASENYA
        =========================================
        Mostra un diàleg de confirmació.
        Si l'usuari confirma, obre la pantalla ContrasenyaUsuari.
    */
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