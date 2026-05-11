package com.example.gymapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/*
    LOCALE HELPER
    =============
    Classe auxiliar per gestionar el canvi d'idioma de l'aplicació.
    Guarda l'idioma seleccionat a SharedPreferences i aplica la configuració
    al context de l'app.

    @author ImperiumGym
    @version 1.0
*/
public class LocaleHelper {

    private static final String PREF_LANG = "idioma_seleccionat";
    private static final String DEFAULT_LANG = "ca"; // Català per defecte

    // Estableix l'idioma seleccionat al context
    public static Context setLocale(Context context, String languageCode) {
        // Guardem l'idioma seleccionat a SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        prefs.edit().putString(PREF_LANG, languageCode).apply();

        // Apliquem l'idioma al context
        return updateResources(context, languageCode);
    }

    // Carrega l'idioma guardat (si n'hi ha) o el per defecte
    public static Context loadSavedLocale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        String languageCode = prefs.getString(PREF_LANG, DEFAULT_LANG);
        return updateResources(context, languageCode);
    }

    // Actualitza els recursos del context amb el nou idioma
    private static Context updateResources(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            config.setLocales(new LocaleList(locale));
            return context.createConfigurationContext(config);
        } else {
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            return context;
        }
    }

    // Obté l'idioma actual
    public static String getCurrentLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Usuaris", Context.MODE_PRIVATE);
        return prefs.getString(PREF_LANG, DEFAULT_LANG);
    }
}