package com.example.gymapp;

/*
    FEATUREDMEDIA.JAVA
    ==================
    Classe model per extreure la URL de la imatge destacada de la notícia.
    La resposta de WordPress té l'estructura: { "source_url": "https://..." }

    @author ImperiumGym
    @version 1.0
*/
public class FeaturedMedia {

    private String source_url;

    public String getSource_url() {
        return source_url;
    }
}