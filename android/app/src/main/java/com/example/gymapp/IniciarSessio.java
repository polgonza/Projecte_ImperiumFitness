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
    INICIAR SESSIO ACTIVITY
    =======================
    Pantalla perquè l'usuari introdueixi les seves credencials (Nom d'usuari i contrasenya).
    Valida les dades amb el backend mitjançant Retrofit.
    Si són correctes, guarda el token JWT, el nom d'usuari i el NOM REAL a SharedPreferences,
    i redirigeix a Home.

    @author ImperiumGym
    @version 3.0
*/
public class IniciarSessio extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;

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
        setContentView(R.layout.activity_iniciar_sessio);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(v -> iniciarSessio());
    }

    /*
        MÈTODE PER INICIAR SESSIÓ
        =========================
        Envia les credencials al backend. Si són correctes, el backend retorna
        l'objecte LoginResponse que conté:
        - token: JWT per a autenticació
        - username: nom d'usuari
        - nom: nom real de l'usuari
    */
    private void iniciarSessio() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.introdueix_usuari_contrasenya, Toast.LENGTH_SHORT).show();
            return;
        }

        // El backend espera { "username": username, "password": password }
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.login(body).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();
                    String nomReal = loginResponse.getNom();

                    if (nomReal == null || nomReal.isEmpty()) {
                        nomReal = username;
                    }

                    // Guardem les dades de sessió a SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("jwt_token", token);
                    editor.putString("usuari_actiu", username);
                    editor.putString("nom_actiu", nomReal);
                    editor.apply();

                    Toast.makeText(IniciarSessio.this, R.string.sessio_iniciada, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(IniciarSessio.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(IniciarSessio.this, R.string.error_credencials, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(IniciarSessio.this, getString(R.string.error_servidor, t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }
}