package com.imperium.fitness.entitat;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuari")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String contrasenya;
    
    @Column(name = "data_registre")
    private LocalDateTime dataRegistre;
    
    private String rol;
    
    public Usuario() {}
    
    public Usuario(String nom, String email, String contrasenya) {
        this.nom = nom;
        this.email = email;
        this.contrasenya = contrasenya;
        this.dataRegistre = LocalDateTime.now();
        this.rol = "USER";
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getContrasenya() {
        return contrasenya;
    }
    
    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
    
    public LocalDateTime getDataRegistre() {
        return dataRegistre;
    }
    
    public void setDataRegistre(LocalDateTime dataRegistre) {
        this.dataRegistre = dataRegistre;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
}