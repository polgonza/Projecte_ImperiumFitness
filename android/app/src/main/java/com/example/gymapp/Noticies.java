package com.example.gymapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    NOTICIES ACTIVITY
    =================
    Pantalla que mostra les últimes notícies i novetats del gimnàs.
    Heretera de BaseActivity per heretar el footer navigation.
    Les notícies es mostren en forma de targetes (cards) organitzades en files de 2.
    Inclou un menú lateral (DrawerLayout) per accedir a opcions addicionals.

    En el futur, les notícies es carregaran des d'una base de dades o API externa,
    i el menú lateral permetrà accedir a InfoPerfilUsuari i tancar sessió.

    @author ImperiumGym
    @version 1.0
*/
public class Noticies extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita el mode EdgeToEdge per ocupar tota la pantalla
        EdgeToEdge.enable(this);

        // Vincula el layout XML activity_noticies.xml
        setContentView(R.layout.activity_noticies);

        // Configura el footer navigation (heretat de BaseActivity)
        // Aquesta línia és OBLIGATÒRIA perquè el footer funcioni
        setupBottomNav();

        // Configura els insets per a la vista principal (gestiona barres del sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}