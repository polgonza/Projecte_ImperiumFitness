package com.example.gymapp;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

import retrofit2.Response;

public class PlaHelper {

    public static void mostrarPopupNecessitaPla(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.pla_necessari_titol))
                .setMessage(activity.getString(R.string.pla_necessari_missatge))
                .setPositiveButton(activity.getString(R.string.pla_necessari_boto), null)
                .show();
    }

    public static boolean esErrorDePla(Response<?> response, String errorBackend) {
        if (response != null && response.code() == 403) {
            return true;
        }

        if (errorBackend == null) {
            return false;
        }

        String error = errorBackend.toLowerCase(Locale.ROOT);

        return error.contains("pla")
                || error.contains("plan")
                || error.contains("tarifa")
                || error.contains("subscrip")
                || error.contains("membres")
                || error.contains("membership")
                || error.contains("no tens")
                || error.contains("no tienes")
                || error.contains("sense pla")
                || error.contains("sin plan")
                || error.contains("plan activo")
                || error.contains("pla actiu");
    }
}