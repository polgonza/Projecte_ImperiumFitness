package com.imperiumfitness.backend.dto;

import java.time.LocalDateTime;

public class ClasseDTO {

    private Long id;
    private String nom;
    private String descripcio;
    private LocalDateTime horari;
    private Integer capacitat;
    private Long gimnasId;

    public ClasseDTO() {}

    public ClasseDTO(Long id, String nom, String descripcio, LocalDateTime horari, Integer capacitat, Long gimnasId) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.horari = horari;
        this.capacitat = capacitat;
        this.gimnasId = gimnasId;
    }

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

    public Long getGimnasId() { return gimnasId; }
    public void setGimnasId(Long gimnasId) { this.gimnasId = gimnasId; }
}
