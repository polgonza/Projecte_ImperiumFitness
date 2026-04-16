package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "classe")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String descripcio;
    private LocalDateTime horari;
    private Integer capacitat;

    // @ManyToOne: moltes classes pertanyen a un gimnas
    // @JoinColumn indica quina columna de la taula "classe" fa de clau forana
    @ManyToOne
    @JoinColumn(name = "gimnas_id")
    private Gimnas gimnas;

    public Classe() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public LocalDateTime getHorari() { return horari; }
    public void setHorari(LocalDateTime horari) { this.horari = horari; }

    public Integer getCapacitat() { return capacitat; }
    public void setCapacitat(Integer capacitat) { this.capacitat = capacitat; }

    public Gimnas getGimnas() { return gimnas; }
    public void setGimnas(Gimnas gimnas) { this.gimnas = gimnas; }
}