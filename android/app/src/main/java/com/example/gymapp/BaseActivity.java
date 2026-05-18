package com.example.gymapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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

    // Codi de sol·licitud de permís d'escriptura per a la descàrrega del PDF
    private static final int REQUEST_WRITE_STORAGE = 112;

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

    protected void mostrarMenuLogo(View view) {
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

            } else if (id == R.id.menu_documentacio) {
                // Opció de descàrrega del PDF de documentació de l'aplicació
                mostrarDialogDocumentacio();
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

    /*
        MÈTODE PER MOSTRAR EL DIÀLEG DE CONFIRMACIÓ DE DESCÀRREGA
        ==========================================================
        Mostra un AlertDialog per confirmar si l'usuari vol descarregar
        el document PDF de documentació de l'aplicació.
        Si accepta, comprova els permisos necessaris abans de continuar.
    */
    private void mostrarDialogDocumentacio() {
        new AlertDialog.Builder(this)
                .setTitle("Descarregar documentació")
                .setMessage("Estàs segur que vols descarregar la documentació d'ImperiumGym?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Per a Android 10 o inferior cal comprovar el permís d'escriptura manualment
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_STORAGE);
                        } else {
                            descarregarDocumentacio();
                        }
                    } else {
                        // Android 11+ no necessita permís per escriure a Downloads
                        descarregarDocumentacio();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /*
        MÈTODE PER DESCARREGAR EL FITXER PDF
        =====================================
        Copia el fitxer PDF des de la carpeta res/raw del projecte
        a la carpeta Downloads del dispositiu de l'usuari.
        Nom del fitxer resultant: Documentacio_ImperiumGym.pdf
    */
    private void descarregarDocumentacio() {
        try {
            // Obtenim el fitxer PDF des de la carpeta res/raw
            InputStream inputStream = getResources().openRawResource(R.raw.documentacio_imperiumgym);

            // Carpeta Downloads del dispositiu (accessible per l'usuari)
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }

            // Fitxer de destinació dins de Downloads
            File outputFile = new File(downloadsDir, "Documentacio_ImperiumGym.pdf");

            // Copiem el contingut byte a byte
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            Toast.makeText(this, "Documentació descarregada a: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error en la descàrrega: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*
        MÈTODE PER GESTIONAR LA RESPOSTA DELS PERMISOS
        ===============================================
        S'executa quan l'usuari respon a la sol·licitud de permís d'escriptura.
        Si es concedeix, s'inicia la descàrrega del PDF. Si es denega, es mostra un avís.
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                descarregarDocumentacio();
            } else {
                Toast.makeText(this, "Permís denegat. No es pot descarregar la documentació.", Toast.LENGTH_LONG).show();
            }
        }
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