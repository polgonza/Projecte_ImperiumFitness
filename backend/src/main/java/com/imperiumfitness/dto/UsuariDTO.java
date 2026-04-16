package com.imperiumfitness.dto;

import java.time.LocalDateTime;

public class UsuariDTO {

    private Long id;
    private String nom;
    private String email;
    private String contrasenya;
    private LocalDateTime dataRegistre;
    private String rol;

    public UsuariDTO() {}

    public UsuariDTO(Long id, String nom, String email, String contrasenya, LocalDateTime dataRegistre, String rol) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.contrasenya = contrasenya;
        this.dataRegistre = dataRegistre;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public LocalDateTime getDataRegistre() { return dataRegistre; }
    public void setDataRegistre(LocalDateTime dataRegistre) { this.dataRegistre = dataRegistre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
