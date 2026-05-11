package com.example.gymapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    CONFIRMADA RESERVA CLASSE ACTIVITY
    ==================================
    Pantalla que es mostra després de reservar una classe amb èxit.
    Hereta de BaseActivity per tenir el footer de navegació.
    Mostra un missatge de confirmació i una icona.

    @author ImperiumGym
    @version 2.0
*/
public class ConfirmadaReservaClasse extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmada_reserva_classe);

        // Configura el footer navigation (heretat de BaseActivity)
        setupBottomNav();

        // Configura els insets per a la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}