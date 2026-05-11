package com.example.gymapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    PAGAT PRODUCTE BOTIGA ACTIVITY
    ==============================
    Pantalla de confirmació que es mostra després de realitzar un pagament amb èxit.
    Aquesta pantalla s'utilitza tant per a compres directes com per a compres
    realitzades des de la cistella.

    Hereta de BaseActivity per tenir el footer de navegació (bottom navigation)
    amb les 6 opcions: Inici, Classes, Botiga, Notícies, Calendari i Contacte.

    Mostra un missatge de confirmació i una icona indicant que la transacció
    s'ha completat correctament.

    En el futur, es podria afegir un botó per tornar a la botiga o a l'inici.

    @author ImperiumGym
    @version 2.0
*/
public class PagatProducteBotiga extends BaseActivity {

    /*
        MÈTODE ONCREATE
        ===============
        S'executa quan es crea l'Activity.
        Configura el mode EdgeToEdge, carrega el layout XML, configura el footer
        de navegació i ajusta els insets per a les barres del sistema.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita el mode EdgeToEdge perquè la pantalla ocupi tota la superfície
        // (incloent les barres laterals i la barra d'estat)
        EdgeToEdge.enable(this);

        // Vincula el layout XML (activity_pagat_producte_botiga.xml) amb aquesta Activity
        setContentView(R.layout.activity_pagat_producte_botiga);

        // Configura el footer navigation (bottomNav) per navegar entre les pantalles
        // Aquest mètode està definit a la classe BaseActivity
        setupBottomNav();

        // Configura els insets per a la vista principal (gestiona les barres del sistema)
        // Això assegura que el contingut no quedi sota la barra d'estat o la barra de navegació
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}