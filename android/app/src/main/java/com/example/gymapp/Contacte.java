package com.example.gymapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/*
    CONTACTE ACTIVITY
    =================
    Pantalla de contacte del gimnàs.
    Mostra informació de contacte i obre l'aplicació de correu o telèfon
    quan es prem als botons corresponents.

    @author ImperiumGym
    @version 1.0
*/
public class Contacte extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacte);

        setupBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnEnviarMissatge = findViewById(R.id.btnEnviarMissatge);
        btnEnviarMissatge.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:info@imperiumgym.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta des de l'app");
            startActivity(Intent.createChooser(intent, "Envia un correu"));
        });
    }
}