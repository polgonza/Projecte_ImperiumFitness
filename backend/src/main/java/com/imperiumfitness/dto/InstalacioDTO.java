package com.imperiumfitness.dto;

public class InstalacioDTO {
    private Long id;
    private String nom;
    private String descripcio;
    private String ubicacio;
    private Long gimnasId;

    public InstalacioDTO() {}

    public InstalacioDTO(Long id, String nom, String descripcio,
                         String ubicacio, Long gimnasId) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.ubicacio = ubicacio;
        this.gimnasId = gimnasId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public String getUbicacio() { return ubicacio; }
    public void setUbicacio(String ubicacio) { this.ubicacio = ubicacio; }

    public Long getGimnasId() { return gimnasId; }
    public void setGimnasId(Long gimnasId) { this.gimnasId = gimnasId; }
}