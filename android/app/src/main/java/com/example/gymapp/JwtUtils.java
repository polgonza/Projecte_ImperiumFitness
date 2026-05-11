package com.example.gymapp;

import android.util.Base64;
import org.json.JSONObject;

public class JwtUtils {

    // Extreu qualsevol camp del token JWT
    // Exemple: JwtUtils.getClaim(token, "userId") → "5"
    public static String getClaim(String token, String clau) {
        try {
            // El token té format: header.payload.signature
            // Només necessitem la part del mig (payload)
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            JSONObject json = new JSONObject(payload);
            return json.getString(clau);
        } catch (Exception e) {
            return null;
        }
    }
}