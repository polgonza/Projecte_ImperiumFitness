package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tarifa")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private BigDecimal preu;
    private String descripcio;

    @ManyToOne
    @JoinColumn(name = "gimnas_id")
    private Gimnas gimnas;

    public Tarifa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public BigDecimal getPreu() { return preu; }
    public void setPreu(BigDecimal preu) { this.preu = preu; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public Gimnas getGimnas() { return gimnas; }
    public void setGimnas(Gimnas gimnas) { this.gimnas = gimnas; }
}