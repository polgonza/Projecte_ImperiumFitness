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

        if (ivSettings != null) {
            ivSettings.setOnClickListener(this::mostrarMenuDesplegable);
        }

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

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

                editor.remove("jwt_token");
                editor.remove("usuari_actiu");
                editor.remove("nom_actiu");
                editor.remove("email_actiu");
                editor.remove("pla_actiu");
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

    private void mostrarDialogIdioma() {
        String[] idiomes = {"Español", "Català", "English"};
        String[] codisIdioma = {"es", "ca", "en"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selecciona_idioma);

        builder.setItems(idiomes, (dialog, which) -> {
            String idiomaSeleccionat = idiomes[which];
            String codiIdioma = codisIdioma[which];

            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirmar_canvi_idioma)
                    .setMessage(getString(R.string.missatge_confirmar_idioma, idiomaSeleccionat))
                    .setPositiveButton(R.string.si, (dialog2, which2) -> {
                        LocaleHelper.setLocale(this, codiIdioma);

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