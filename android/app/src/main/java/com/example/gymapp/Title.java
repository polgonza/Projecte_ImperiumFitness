package com.example.gymapp;

/*
    TITLE.JAVA
    ==========
    Classe model per extreure el títol de la notícia des de la API de WordPress.
    La resposta de WordPress té l'estructura: { "title": { "rendered": "Títol aquí" } }

    @author ImperiumGym
    @version 1.0
*/
public class Title {

    private String rendered;

    public String getRendered() {
        return rendered;
    }
}