package com.example.gymapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/*
    EMBEDDED.JAVA
    =============
    Classe model per extreure la informació incrustada (embed) de la notícia.

    @author ImperiumGym
    @version 1.0
*/
public class Embedded {

    @SerializedName("wp:featuredmedia")
    private List<FeaturedMedia> featuredmedia;

    public List<FeaturedMedia> getFeaturedmedia() {
        return featuredmedia;
    }
}