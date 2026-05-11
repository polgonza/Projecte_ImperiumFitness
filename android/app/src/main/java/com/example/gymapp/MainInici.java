package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    MainInici.java
    Pantalla principal de l'aplicació (splash/landing).
    Mostra el logo, el títol i dos botons per iniciar sessió o registrar-se.
    No té bottom navigation ni topbar perquè és la pantalla d'entrada abans de loguejar-se.

    @author ImperiumGym
    @version 2.0
*/
public class MainInici extends AppCompatActivity {

    private Button btnIniciaSessio, btnRegistre;

    /*
        MÈTODE ATTACH BASECONTEXT
        =========================
        Carrega l'idioma guardat abans de crear l'Activity.
        Això assegura que els textos es mostrin en l'idioma seleccionat.
    */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.loadSavedLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_inici);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnIniciaSessio = findViewById(R.id.btnIniciaSessio);
        btnRegistre = findViewById(R.id.btnRegistre);

        btnIniciaSessio.setOnClickListener(v -> startActivity(new Intent(this, IniciarSessio.class)));
        btnRegistre.setOnClickListener(v -> startActivity(new Intent(this, Registre.class)));
    }
}