package com.example.gymapp;

public class ProducteCistella {

    public Long producteId;
    public String nom;
    public double preu;
    public int imatge;
    public String imatgeUrl;
    public String descripcio;

    public ProducteCistella() {
    }


    public ProducteCistella(Long producteId, String nom, double preu, int imatge, String descripcio) {
        this.producteId = producteId;
        this.nom = nom;
        this.preu = preu;
        this.imatge = imatge;
        this.imatgeUrl = null;
        this.descripcio = descripcio;
    }

    /*
        Constructor nuevo con imagen real del backend.
    */
    public ProducteCistella(
            Long producteId,
            String nom,
            double preu,
            int imatge,
            String imatgeUrl,
            String descripcio
    ) {
        this.producteId = producteId;
        this.nom = nom;
        this.preu = preu;
        this.imatge = imatge;
        this.imatgeUrl = imatgeUrl;
        this.descripcio = descripcio;
    }
}