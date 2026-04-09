package com.imperium.fitness.entitat;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contacte")
public class Contacte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false, length = 1000)
    private String missatge;
    
    @Column(name = "data_enviament")
    private LocalDateTime dataEnviament;
    
    public Contacte() {}
    
    public Contacte(String nom, String email, String missatge) {
        this.nom = nom;
        this.email = email;
        this.missatge = missatge;
        this.dataEnviament = LocalDateTime.now();
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
    
    public String getMissatge() {
        return missatge;
    }
    
    public void setMissatge(String missatge) {
        this.missatge = missatge;
    }
    
    public LocalDateTime getDataEnviament() {
        return dataEnviament;
    }
    
    public void setDataEnviament(LocalDateTime dataEnviament) {
        this.dataEnviament = dataEnviament;
    }
}