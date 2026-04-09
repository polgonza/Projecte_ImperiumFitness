package com.imperium.fitness.entitat;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Producte")
public class Producte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(length = 500)
    private String descripcio;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preu;
    
    private String categoria;
    
    private Integer estoc;
    
    public Producte() {}
    
    public Producte(String nom, BigDecimal preu, String categoria, Integer estoc) {
        this.nom = nom;
        this.preu = preu;
        this.categoria = categoria;
        this.estoc = estoc;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescripcio() {
        return descripcio;
    }
    
    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }
    
    public BigDecimal getPreu() {
        return preu;
    }
    
    public void setPreu(BigDecimal preu) {
        this.preu = preu;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public Integer getEstoc() {
        return estoc;
    }
    
    public void setEstoc(Integer estoc) {
        this.estoc = estoc;
    }
}