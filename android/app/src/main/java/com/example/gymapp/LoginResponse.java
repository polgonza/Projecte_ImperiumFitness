package com.example.gymapp;

public class LoginResponse {
    private String token;
    private String username;
    private String nom;  // ← camp per al nom real

    // Getters
    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getNom() { return nom; }

    // Setters (necessaris per a Gson)
    public void setToken(String token) { this.token = token; }
    public void setUsername(String username) { this.username = username; }
    public void setNom(String nom) { this.nom = nom; }
}