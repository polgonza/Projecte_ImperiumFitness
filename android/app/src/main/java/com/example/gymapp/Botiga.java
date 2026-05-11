package com.example.gymapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    BOTIGA ACTIVITY
    ===============
    Pantalla que mostra els productes de la botiga del gimnàs.
    Actualment té 2 productes:
    - Proteïna (39,99€)
    - Camiseta (19,99€)

    En prémer el botó "Compra ara" d'un producte, obre la pantalla ProductesBotiga
    amb el seu detall (similar al funcionament de la pantalla Classes).

    També té una icona de cistella a la topbar que permet accedir directament
    a la pantalla CistellaBotiga per veure els productes afegits.

    @author ImperiumGym
    @version 2.0
*/
public class Botiga extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_botiga);

        // Configura el footer navigation
        setupBottomNav();

        // Configura els insets per a la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ==================== ICONA DE LA CISTELLA ====================
        // Permet anar a la pantalla de la cistella directament
        LinearLayout llCistella = findViewById(R.id.llCistella);
        if (llCistella != null) {
            llCistella.setOnClickListener(v -> {
                Intent intent = new Intent(this, CistellaBotiga.class);
                startActivity(intent);
            });
        }

        // ==================== PRODUCTE 1: PROTEÏNA ====================
        Button btnComprar1 = findViewById(R.id.btnComprar1);
        if (btnComprar1 != null) {
            btnComprar1.setOnClickListener(v -> obrirProducte(
                    "Proteïna",           // Nom del producte
                    39.99,                // Preu
                    "Proteïna de sèrum d'alta qualitat. Perfecta per després de l'entrenament per a la recuperació muscular."
            ));
        }

        // ==================== PRODUCTE 2: CAMISETA ====================
        Button btnComprar2 = findViewById(R.id.btnComprar2);
        if (btnComprar2 != null) {
            btnComprar2.setOnClickListener(v -> obrirProducte(
                    "Camiseta Gym",       // Nom del producte
                    19.99,                // Preu
                    "Camiseta oficial d'ImperiumGym. 100% cotó, transpirable i còmoda per als teus entrenaments."
            ));
        }
    }

    /*
        MÈTODE PER OBRIR EL DETALL D'UN PRODUCTE
        ========================================
        Crea un Intent cap a ProductesBotiga i li passa les dades del producte.
    */
    private void obrirProducte(String nom, double preu, String descripcio) {
        Intent intent = new Intent(this, ProductesBotiga.class);
        intent.putExtra("nom_producte", nom);
        intent.putExtra("preu_producte", preu);
        intent.putExtra("descripcio_producte", descripcio);
        startActivity(intent);
    }
}