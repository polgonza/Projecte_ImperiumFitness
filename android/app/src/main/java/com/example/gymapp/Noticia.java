package com.example.gymapp;

import com.google.gson.annotations.SerializedName;

/*
    NOTICIA.JAVA
    ============
    Classe model principal que representa una notícia completa de WordPress.

    @author ImperiumGym
    @version 2.0
*/
public class Noticia {

    private Long id;
    private Title title;
    private Acf acf;

    @SerializedName("_embedded")
    private Embedded embedded;

    // Getters
    public Long getId() { return id; }
    public Title getTitle() { return title; }
    public Acf getAcf() { return acf; }
    public Embedded getEmbedded() { return embedded; }
}