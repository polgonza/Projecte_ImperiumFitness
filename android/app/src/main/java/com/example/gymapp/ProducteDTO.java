package com.example.gymapp;

public class ProducteDTO {

    private Long id;
    private String nom;
    private String descripcio;
    private Double preu;
    private String categoria;
    private Integer estoc;

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
}