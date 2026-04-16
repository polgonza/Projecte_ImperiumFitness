package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuari") // nom exacte de la taula SQL
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT de MariaDB
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contrasenya; // aquí sempre s'emmagatzemarà el hash BCrypt

    @Column(name = "data_registre") // nom exacte de la columna SQL
    private LocalDateTime dataRegistre;

    @Column(nullable = false)
    private String rol; // valors: "USER" o "ADMIN"

    // ── Constructor buit obligatori per JPA ──────────────────────────────────
    public Usuari() {}

    // ── Getters i Setters ────────────────────────────────────────────────────

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