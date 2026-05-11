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
    Pantalla per crear un nou compte d'usuari.
    L'usuari ha d'omplir: Nom d'usuari, Email i Contrasenya.
    Quan premi "Registra't", es validaran les dades i es guardaran a la base de dades.
    Després es redirigeix a IniciarSessio.

    @author ImperiumGym
    @version 3.0
*/
public class Registre extends AppCompatActivity {

    // Variables per als camps del formulari
    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;

    /*
        MÈTODE ATTACH BASECONTEXT
        =========================
        Carrega l'idioma guardat abans de crear l'Activity.
    */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.loadSavedLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registre);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialitza els components del layout
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> registrarUsuari());
    }

    /*
        MÈTODE PER REGISTRAR USUARI
        ===========================
        Valida que tots els camps estiguin plens i,
        envia les dades al backend per crear el nou usuari.
    */
    private void registrarUsuari() {
        // Obté el text de tots els camps
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validació: comprova que cap camp estigui buit
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.omple_tots_els_camps, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validació bàsica d'email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.email_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validació de contrasenya (mínim 4 caràcters)
        if (password.length() < 4) {
            Toast.makeText(this, R.string.password_min, Toast.LENGTH_SHORT).show();
            return;
        }

        // Cridem el backend amb els 3 camps
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("email", email);
        body.put("password", password);

        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.registre(body).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Registre.this, R.string.registre_ok, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registre.this, IniciarSessio.class));
                    finish();
                } else {
                    Toast.makeText(Registre.this, R.string.error_registre, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Registre.this, getString(R.string.error_servidor, t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }
}