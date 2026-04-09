package com.imperiumfitness.backend.dto;

public class GimnasDTO {

    private Long id;
    private String nom;
    private String adreca;
    private String telefon;

    public GimnasDTO() {}

    public GimnasDTO(Long id, String nom, String adreca, String telefon) {
        this.id = id;
        this.nom = nom;
        this.adreca = adreca;
        this.telefon = telefon;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdreca() { return adreca; }
    public void setAdreca(String adreca) { this.adreca = adreca; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
}