package com.imperiumfitness.backend.dto;

import java.math.BigDecimal;

public class ProducteDTO {

    private Long id;
    private String nom;
    private String descripcio;
    private BigDecimal preu;
    private String categoria;
    private Integer estoc;

    public ProducteDTO() {}

    public ProducteDTO(Long id, String nom, String descripcio, BigDecimal preu, String categoria, Integer estoc) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.preu = preu;
        this.categoria = categoria;
        this.estoc = estoc;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public BigDecimal getPreu() { return preu; }
    public void setPreu(BigDecimal preu) { this.preu = preu; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getEstoc() { return estoc; }
    public void setEstoc(Integer estoc) { this.estoc = estoc; }
}