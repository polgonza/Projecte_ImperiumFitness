package com.imperiumfitness.dto;

import java.math.BigDecimal;

public class TarifaDTO {
    private Long id;
    private String nom;
    private BigDecimal preu;
    private String descripcio;
    private Long gimnasId;

    public TarifaDTO() {}

    public TarifaDTO(Long id, String nom, BigDecimal preu,
                     String descripcio, Long gimnasId) {
        this.id = id;
        this.nom = nom;
        this.preu = preu;
        this.descripcio = descripcio;
        this.gimnasId = gimnasId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public BigDecimal getPreu() { return preu; }
    public void setPreu(BigDecimal preu) { this.preu = preu; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public Long getGimnasId() { return gimnasId; }
    public void setGimnasId(Long gimnasId) { this.gimnasId = gimnasId; }
}