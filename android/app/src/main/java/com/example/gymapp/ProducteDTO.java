package com.example.gymapp;

import com.google.gson.annotations.SerializedName;

public class ProducteDTO {

    private Long id;
    private String nom;
    private String descripcio;
    private Double preu;
    private String categoria;
    private Integer estoc;

    /*
        Imagen del producto recibida desde backend.

        Puede llegar como:
        - imatgeUrl
        - imatge_url
        - imagen_url


    */
    @SerializedName(value = "imatgeUrl", alternate = {"imatge_url", "imagen_url"})
    private String imatgeUrl;

    public ProducteDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public Double getPreu() {
        return preu;
    }

    public String getCategoria() {
        return categoria;
    }

    public Integer getEstoc() {
        return estoc;
    }

    public String getImatgeUrl() {
        return imatgeUrl;
    }

    public String getImagenUrl() {
        return imatgeUrl;
    }
}