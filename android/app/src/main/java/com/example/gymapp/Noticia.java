package com.example.gymapp;

import com.google.gson.annotations.SerializedName;

/*
    NOTICIA.JAVA
    ============
    Classe model principal que representa una notícia completa de WordPress.
    Conté:
    - Títol (objecte Title)
    - Descripció (objecte Acf amb camp personalitzat)
    - Imatge destacada (dins de "_embedded")

    @author ImperiumGym
    @version 1.0
*/
public class Noticia {

    private Title title;
    private Acf acf;

    @SerializedName("_embedded")
    private Embedded embedded;

    public Title getTitle() {
        return title;
    }

    public Acf getAcf() {
        return acf;
    }

    public Embedded getEmbedded() {
        return embedded;
    }
}