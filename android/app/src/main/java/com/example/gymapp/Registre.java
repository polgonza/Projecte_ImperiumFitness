package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    REGISTRE ACTIVITY
    =================
    Pantalla per crear un nou compte d'usuari a ImperiumGym.

    Aquesta Activity permet a l'usuari registrar-se introduint:
    - Nom d'usuari (username)
    - Adreça electrònica (email)
    - Contrasenya (password)

    Un cop validades les dades, s'envien al backend mitjançant Retrofit.
    Si el registre és exitós, es redirigeix l'usuari a la pantalla d'inici de sessió.

    El backend retorna un token JWT que s'emmagatzema a SharedPreferences
    per a futures autenticacions.

    @author ImperiumGym
    @version 3.0
*/
public class Registre extends AppCompatActivity {

    /*
        ==================== VARIABLES DE LA CLASSE ====================
    */

    // Camps del formulari de registre
    private EditText etUsername;  // Camp per al nom d'usuari
    private EditText etEmail;     // Camp per a l'adreça electrònica
    private EditText etPassword;  // Camp per a la contrasenya
    private Button btnRegister;   // Botó per enviar el formulari

    /*
        ==================== MÈTODE ATTACH BASECONTEXT ====================
        Aquest mètode s'executa ABANS que onCreate().
        La seva funció és carregar l'idioma que l'usuari ha seleccionat
        prèviament a l'aplicació, abans que es mostri la pantalla.
        Això assegura que tots els textos es vegin en l'idioma correcte.
    */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.loadSavedLocale(newBase));
    }

    /*
        ==================== MÈTODE ONCREATE ====================
        S'executa quan es crea l'Activity per primera vegada.
        Aquí s'inicialitzen les vistes, es configuren els listeners
        i es prepara la interfície d'usuari.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita el mode EdgeToEdge perquè la pantalla ocupi tota la superfície
        EdgeToEdge.enable(this);

        // Vincula el layout XML (activity_registre.xml) amb aquesta Activity
        setContentView(R.layout.activity_registre);

        /*
            Configura els insets per a la vista principal.
            Això assegura que el contingut no quedi sota la barra d'estat
            o la barra de navegació del dispositiu.
        */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
            Inicialització dels components del layout.
            Es busca cada element pel seu ID definit al fitxer XML
            i s'emmagatzema en les variables de la classe.
        */
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        /*
            Configura el listener del botó "Registra't".
            Quan l'usuari faci clic, s'executarà el mètode registrarUsuari().
        */
        btnRegister.setOnClickListener(v -> registrarUsuari());
    }

    /*
        ==================== MÈTODE PER REGISTRAR USUARI ====================

        Aquest mètode s'executa quan l'usuari prem el botó "Registra't".

        PASSOS QUE REALITZA:
        1. Obté el text dels tres camps (username, email, password)
        2. Valida que cap camp estigui buit
        3. Valida que l'email tingui un format correcte
        4. Valida que la contrasenya tingui com a mínim 4 caràcters
        5. Crea un mapa amb les dades per enviar al backend
        6. Envia la petició POST al backend mitjançant Retrofit
        7. Gestiona la resposta del backend (èxit o error)

        En cas d'èxit, redirigeix a la pantalla d'inici de sessió.
        En cas d'error, mostra un missatge a l'usuari.
    */
    private void registrarUsuari() {
        /*
            PAS 1: OBTENIR LES DADES DELS CAMPS
            ===================================
            S'extreu el text de cada EditText i s'eliminen espais
            sobrants al principi i al final amb .trim().
        */
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        /*
            PAS 2: VALIDAR QUE ELS CAMPS NO ESTIGUIN BUITS
            ===============================================
            Si algun camp està buit, es mostra un missatge d'error
            i s'atura el procés.
        */
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.omple_tots_els_camps, Toast.LENGTH_SHORT).show();
            return;
        }

        /*
            PAS 3: VALIDAR EL FORMAT DE L'EMAIL
            ===================================
            Utilitza l'expressió regular de Patterns per comprovar
            que l'email té un format vàlid (ex: usuari@domini.com).
        */
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.email_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        /*
            PAS 4: VALIDAR LA LONGITUD DE LA CONTRASENYA
            ============================================
            La contrasenya ha de tenir com a mínim 4 caràcters per
            raons de seguretat bàsica.
        */
        if (password.length() < 4) {
            Toast.makeText(this, R.string.password_min, Toast.LENGTH_SHORT).show();
            return;
        }

        /*
            PAS 5: PREPARAR LES DADES PER AL BACKEND
            ========================================
            Es crea un Map amb les dades que s'enviaran al backend.
            El backend espera els camps: "nom", "email", "password".
            Nota: El camp "nom" correspon al nom d'usuari (username).
        */
        Map<String, String> body = new HashMap<>();
        body.put("nom", username);   // El backend espera "nom", no "username"
        body.put("email", email);
        body.put("password", password);

        /*
            PAS 6: ENVIAR LA PETICIÓ AL BACKEND AMB RETROFIT
            ================================================
            Es crea una instància d'ApiService i es crida el mètode registre().
            La petició és asíncrona, per tant es necessita un Callback
            per gestionar la resposta quan arribi.
        */
        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.registre(body).enqueue(new Callback<LoginResponse>() {

            /*
                CAS D'ÈXIT: RESPOSTA DEL BACKEND
                ================================
                S'executa quan el backend respon (tant si l'operació
                ha anat bé com si ha anat malament, però hi ha hagut
                comunicació).
            */
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                /*
                    Si la resposta és exitosa (codi HTTP 2xx) i el cos
                    de la resposta no és buit, el registre ha funcionat.
                */
                if (response.isSuccessful() && response.body() != null) {
                    // Mostra missatge de confirmació
                    Toast.makeText(Registre.this, R.string.registre_ok, Toast.LENGTH_SHORT).show();

                    // Redirigeix a la pantalla d'inici de sessió
                    startActivity(new Intent(Registre.this, IniciarSessio.class));

                    // Tanca l'Activity actual perquè l'usuari no pugui tornar enrere
                    finish();
                } else {
                    /*
                        Si la resposta no és exitosa (codi HTTP 4xx o 5xx),
                        vol dir que el backend ha rebutjat el registre.
                        Motius possibles: email duplicat, username duplicat, etc.
                    */
                    Toast.makeText(Registre.this, R.string.error_registre, Toast.LENGTH_LONG).show();
                }
            }

            /*
                CAS D'ERROR: FALLADA DE CONNEXIÓ
                ================================
                S'executa quan no s'ha pogut establir comunicació amb el backend.
                Motius possibles: servidor caigut, sense connexió a Internet,
                URL incorrecta, etc.
            */
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Mostra un missatge amb l'error de connexió
                Toast.makeText(Registre.this, getString(R.string.error_servidor, t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }
}