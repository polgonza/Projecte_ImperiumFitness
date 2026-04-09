package com.imperiumfitness.backend.dto;

import java.time.LocalDateTime;

public class ContacteDTO {

    private Long id;
    private String nom;
    private String email;
    private String missatge;
    private LocalDateTime dataEnviament;

    public ContacteDTO() {}

    public ContacteDTO(Long id, String nom, String email, String missatge, LocalDateTime dataEnviament) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.missatge = missatge;
        this.dataEnviament = dataEnviament;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMissatge() { return missatge; }
    public void setMissatge(String missatge) { this.missatge = missatge; }

    public LocalDateTime getDataEnviament() { return dataEnviament; }
    public void setDataEnviament(LocalDateTime dataEnviament) { this.dataEnviament = dataEnviament; }
}

