package com.example.gymapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    HOME ACTIVITY
    =============
    Pantalla principal després d'iniciar sessió.
    Mostra un missatge de benvinguda amb el NOM REAL de l'usuari (llegit de SharedPreferences).
    El menú desplegable (engranatge) i tota la seva lògica estan centralitzats a BaseActivity.

    @author ImperiumGym
    @version 5.0
*/
public class Home extends BaseActivity {

    private TextView tvBenvinguda;
    private ImageView ivSettings;
    private SharedPreferences sharedPreferences;
    private String usuariActiu;
    private String nomRealUsuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Carreguem l'usuari que ha iniciat sessió
        sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);

        usuariActiu = sharedPreferences.getString(
                "usuari_actiu",
                getString(R.string.usuari_default)
        );

        /*
            Intentem carregar el nom real.
            Si nom_actiu està buit o és un email, netegem usuari_actiu:
            admin@imperium.com -> Admin
        */
        String nomGuardat = sharedPreferences.getString("nom_actiu", "");

        if (nomGuardat != null && !nomGuardat.trim().isEmpty() && !nomGuardat.contains("@")) {
            nomRealUsuari = nomGuardat;
        } else {
            nomRealUsuari = netejarNomUsuari(usuariActiu);
        }

        // Inicialitzem les vistes
        tvBenvinguda = findViewById(R.id.tvBenvinguda);
        ivSettings = findViewById(R.id.ivSettings);

        // Text de benvinguda
        tvBenvinguda.setText(getString(R.string.benvinguda, nomRealUsuari));

        // Usem el menú centralitzat de BaseActivity (inclou Documentació, Idioma, Perfil, Ajuda...)
        if (ivSettings != null) {
            ivSettings.setOnClickListener(this::mostrarMenuLogo);
        }

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String netejarNomUsuari(String usuari) {
        if (usuari == null || usuari.trim().isEmpty()) {
            return getString(R.string.usuari_default);
        }

        String net = usuari.trim();

        if (net.contains("@")) {
            net = net.substring(0, net.indexOf("@"));
        }

        if (net.isEmpty()) {
            return getString(R.string.usuari_default);
        }

        return net.substring(0, 1).toUpperCase() + net.substring(1);
    }
}