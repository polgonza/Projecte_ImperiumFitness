package com.imperiumfitness.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "instalacio")
public class Instalacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String descripcio;
    private String ubicacio;

    @ManyToOne
    @JoinColumn(name = "gimnas_id")
    private Gimnas gimnas;

    public Instalacio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public String getUbicacio() { return ubicacio; }
    public void setUbicacio(String ubicacio) { this.ubicacio = ubicacio; }

    public Gimnas getGimnas() { return gimnas; }
    public void setGimnas(Gimnas gimnas) { this.gimnas = gimnas; }
}