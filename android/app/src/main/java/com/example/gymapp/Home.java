package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    HOME ACTIVITY
    =============
    Pantalla principal després d'iniciar sessió.
    Mostra un missatge de benvinguda amb el NOM REAL de l'usuari (llegit de SharedPreferences),
    un menú desplegable (engranatge) amb opcions de perfil, idioma, tancar sessió i ajuda.
    Aquí es centralitza la navegació del footer via BaseActivity.

    @author ImperiumGym
    @version 3.0
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
            Carreguem el NOM REAL de l'usuari.
            Si encara no existeix nom_actiu, netegem el correu:
            admin@imperium.com -> Admin
        */
        String nomGuardat = sharedPreferences.getString("nom_actiu", "");

        if (nomGuardat != null && !nomGuardat.trim().isEmpty()) {
            nomRealUsuari = nomGuardat;
        } else {
            nomRealUsuari = netejarNomUsuari(usuariActiu);
        }

        // Inicialitzem les vistes
        tvBenvinguda = findViewById(R.id.tvBenvinguda);
        ivSettings = findViewById(R.id.ivSettings);

        // Canviem el text de benvinguda amb el NOM REAL de l'usuari
        tvBenvinguda.setText(getString(R.string.benvinguda, nomRealUsuari));

        // Configura el menú desplegable en clicar la icona d'engranatge
        if (ivSettings != null) {
            ivSettings.setOnClickListener(this::mostrarMenuDesplegable);
        }

        // Configura el footer (herencia de BaseActivity)
        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /*
        MÈTODE PER MOSTRAR EL MENÚ DESPLEGABLE
        ======================================
        Mostra un PopupMenu amb les opcions:
        - Informació de Perfil
        - Idioma (Espanyol, Català, Anglès)
        - Tancar Sessió
        - Ajuda
    */
    private void mostrarMenuDesplegable(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_perfil) {
                Intent intent = new Intent(this, InfoPerfilUsuari.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.menu_idioma) {
                mostrarDialogIdioma();
                return true;
            } else if (id == R.id.menu_tancar_sessio) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("usuari_actiu");
                editor.apply();

                Intent intent = new Intent(this, MainInici.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.menu_ajuda) {
                Intent intent = new Intent(this, Ajuda.class);
                startActivity(intent);
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    /*
        MÈTODE PER MOSTRAR EL DIÀLEG DE CANVI D'IDIOMA
        ==============================================
        Mostra un AlertDialog amb tres opcions: Espanyol, Català, Anglès.
        En confirmar, canvia l'idioma de tota l'aplicació i reinicia l'Activity.
    */
    private void mostrarDialogIdioma() {
        String[] idiomes = {"Español", "Català", "English"};
        String[] codisIdioma = {"es", "ca", "en"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selecciona_idioma);
        builder.setItems(idiomes, (dialog, which) -> {
            String idiomaSeleccionat = idiomes[which];
            String codiIdioma = codisIdioma[which];

            // Diàleg de confirmació
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirmar_canvi_idioma)
                    .setMessage(getString(R.string.missatge_confirmar_idioma, idiomaSeleccionat))
                    .setPositiveButton(R.string.si, (dialog2, which2) -> {
                        // Canviem l'idioma a nivell global
                        LocaleHelper.setLocale(this, codiIdioma);

                        // Reiniciem l'Activity per aplicar els canvis
                        recreate();

                        Toast.makeText(
                                this,
                                getString(R.string.idioma_canviat, idiomaSeleccionat),
                                Toast.LENGTH_SHORT
                        ).show();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        builder.show();
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