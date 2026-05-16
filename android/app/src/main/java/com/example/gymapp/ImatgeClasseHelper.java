package com.example.gymapp;

import android.content.Context;

import java.text.Normalizer;

public class ImatgeClasseHelper {

    public static int obtenirImatgeClasse(Context context, String nomClasse) {
        if (context == null || nomClasse == null) {
            return R.drawable.logo;
        }

        String nom = normalitzar(nomClasse);

        String nomDrawable = "logo";

        if (nom.contains("zumba")) {
            nomDrawable = "zumba";

        } else if (nom.contains("spinning") || nom.contains("spining") || nom.contains("ciclisme")) {

            nomDrawable = "spining";

        } else if (nom.contains("body pump") || nom.contains("bodypump") || nom.contains("pump")) {
            nomDrawable = "bodypump";

        } else if (nom.contains("yoga")) {
            nomDrawable = "yoga";

        } else if (nom.contains("pilates")) {
            nomDrawable = "pilates";

        } else if (nom.contains("crossfit") || nom.contains("cross fit")) {
            nomDrawable = "crossfit";

        } else if (nom.contains("hiit")) {
            nomDrawable = "hiit";
        }

        int resId = context.getResources().getIdentifier(
                nomDrawable,
                "drawable",
                context.getPackageName()
        );

        if (resId == 0) {
            return R.drawable.logo;
        }

        return resId;
    }

    private static String normalitzar(String text) {
        String normalitzat = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalitzat = normalitzat.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalitzat.toLowerCase().trim();
    }
}