package com.imperiumfitness.domain;

//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Table;
//import jakarta.persistence.Id;

//@Entity
//@Table(name="usuari")
public class Usuari {
    
    //@Id
    //@GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    private String contrasenya;
    private String rol;

    public Usuari() {
    }

    public Usuari(Long id, String nom, String email, String contrasenya, String rol) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.contrasenya = contrasenya;
        this.rol = rol;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}