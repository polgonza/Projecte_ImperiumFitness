package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/*
    BASE ACTIVITY - CLASSE BASE PER A TOTES LES PANTALLES AMB FOOTER
    ================================================================
    Aquesta classe conté la lògica comuna per a totes les pantalles:
    - Bottom navigation
    - Menú desplegable del logo superior
    - Canvi d'idioma
    - Tancar sessió
    - Accés a perfil
    - Accés a ajuda
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
        super.attachBaseContext(LocaleHelper.loadSavedLocale(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
        MÈTODE PER CONFIGURAR EL BOTTOM NAVIGATION
        ==========================================
        IMPORTANT: S'ha de cridar DESPRÉS de setContentView().
    */
    protected void setupBottomNav() {
        /*
            También activamos el menú del logo aquí.
            Así no hay que repetirlo pantalla por pantalla.
        */
        setupTopLogoMenu();

        // 1. INICI -> Home
        LinearLayout home = findViewById(R.id.navHome);
        if (home != null) {
            home.setOnClickListener(v -> {
                if (!(this instanceof Home)) {
                    startActivity(new Intent(this, Home.class));
                }
            });
        }

        // 2. CLASSES -> Classes
        LinearLayout classes = findViewById(R.id.navClasses);
        if (classes != null) {
            classes.setOnClickListener(v -> {
                if (!(this instanceof Classes)) {
                    startActivity(new Intent(this, Classes.class));
                }
            });
        }

        // 3. BOTIGA -> Botiga
        LinearLayout botiga = findViewById(R.id.navBotiga);
        if (botiga != null) {
            botiga.setOnClickListener(v -> {
                if (!(this instanceof Botiga)) {
                    startActivity(new Intent(this, Botiga.class));
                }
            });
        }


        // 4. NOTÍCIES -> Noticies
        LinearLayout noticies = findViewById(R.id.navNoticies);
        if (noticies != null) {
            noticies.setOnClickListener(v -> {
                if (!(this instanceof Noticies)) {
                    startActivity(new Intent(this, Noticies.class));
                }
            });
        }


        // 5. CALENDARI -> Calendari
        LinearLayout calendari = findViewById(R.id.navCalendari);
        if (calendari != null) {
            calendari.setOnClickListener(v -> {
                if (!(this instanceof Calendari)) {
                    startActivity(new Intent(this, Calendari.class));
                }
            });
        }

        // 6. CONTACTE -> Contacte
        LinearLayout contacte = findViewById(R.id.navContacte);
        if (contacte != null) {
            contacte.setOnClickListener(v -> {
                if (!(this instanceof Contacte)) {
                    startActivity(new Intent(this, Contacte.class));
                }
            });
        }
    }

    /*
        MENÚ DEL LOGO SUPERIOR
        ======================
        Si la pantalla tiene:
        - logoContainer
        - imgTopLogo

        Al pulsarlo se abre el desplegable con:
        - Perfil
        - Idioma
        - Cerrar sesión
        - Ayuda
    */
    protected void setupTopLogoMenu() {
        FrameLayout logoContainer = findViewById(R.id.logoContainer);
        ImageView imgTopLogo = findViewById(R.id.imgTopLogo);

        View.OnClickListener listener = this::mostrarMenuLogo;

        if (logoContainer != null) {
            logoContainer.setOnClickListener(listener);
        }

        if (imgTopLogo != null) {
            imgTopLogo.setOnClickListener(listener);
        }
    }

    private void mostrarMenuLogo(View view) {
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
                tancarSessio();
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

                        Toast.makeText(
                                this,
                                getString(R.string.idioma_canviat, idiomaSeleccionat),
                                Toast.LENGTH_SHORT
                        ).show();

                        recreate();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        builder.show();
    }

    private void tancarSessio() {
        SharedPreferences sharedPreferences = getSharedPreferences("Usuaris", Context.MODE_PRIVATE);

        sharedPreferences.edit()
                .remove("jwt_token")
                .remove("usuari_actiu")
                .remove("nom_actiu")
                .remove("email_actiu")
                .remove("pla_actiu")
                .remove("cistella")
                .remove("producte_directe_id")
                .remove("producte_directe_nom")
                .remove("producte_directe_preu")
                .remove("producte_directe_imatge")
                .remove("producte_directe_quantitat")
                .remove("producte_directe_descripcio")
                .apply();

        Intent intent = new Intent(this, MainInici.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}