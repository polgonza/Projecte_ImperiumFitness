package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*
    BASE ACTIVITY - CLASSE BASE PER A TOTES LES PANTALLES AMB FOOTER
    ================================================================
    Aquesta classe conté la lògica comuna per a totes les pantalles que
    tenen el bottom navigation (footer) amb 6 ítems:
    - Inici (Home)
    - Classes (Classes)
    - Botiga (Botiga)
    - Notícies (Noticies)
    - Calendari (Calendari)
    - Contacte (Contacte)

    Totes les pantalles que hereten d'aquesta classe han de cridar
    setupBottomNav() dins del seu onCreate() després de setContentView().
    Aquest mètode configura els listeners per navegar entre les diferents
    pantalles de l'aplicació.

    @author ImperiumGym
    @version 3.0
*/
public class BaseActivity extends AppCompatActivity {

    /*
        MÈTODE ATTACH BASECONTEXT
        =========================
        S'executa abans que onCreate(). Carrega l'idioma guardat perquè
        els recursos (strings) es carreguin en l'idioma correcte.
    */
    @Override
    protected void attachBaseContext(Context newBase) {
        // Carreguem l'idioma guardat abans de crear l'Activity
        super.attachBaseContext(LocaleHelper.loadSavedLocale(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // El footer es configura a cada Activity filla
    }

    /*
        MÈTODE PER CONFIGURAR EL BOTTOM NAVIGATION
        ==========================================
        Configura els listeners per a cada ítem del footer.
        Quan l'usuari fa clic a un ítem, s'obre l'Activity corresponent.
        IMPORTANT: S'ha de cridar DESPRÉS de setContentView().
    */
    protected void setupBottomNav() {
        // 1. INICI -> Home
        LinearLayout home = findViewById(R.id.navHome);
        if (home != null) {
            home.setOnClickListener(v -> startActivity(new Intent(this, Home.class)));
        }

        // 2. CLASSES -> Classes
        LinearLayout classes = findViewById(R.id.navClasses);
        if (classes != null) {
            classes.setOnClickListener(v -> startActivity(new Intent(this, Classes.class)));
        }

        // 3. BOTIGA -> Botiga
        LinearLayout botiga = findViewById(R.id.navBotiga);
        if (botiga != null) {
            botiga.setOnClickListener(v -> startActivity(new Intent(this, Botiga.class)));
        }

        // 4. NOTÍCIES -> Noticies
        LinearLayout noticies = findViewById(R.id.navNoticies);
        if (noticies != null) {
            noticies.setOnClickListener(v -> startActivity(new Intent(this, Noticies.class)));
        }

        // 5. CALENDARI -> Calendari
        LinearLayout calendari = findViewById(R.id.navCalendari);
        if (calendari != null) {
            calendari.setOnClickListener(v -> startActivity(new Intent(this, Calendari.class)));
        }

        // 6. CONTACTE -> Contacte
        LinearLayout contacte = findViewById(R.id.navContacte);
        if (contacte != null) {
            contacte.setOnClickListener(v -> startActivity(new Intent(this, Contacte.class)));
        }
    }
}