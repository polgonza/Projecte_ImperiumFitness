package com.example.gymapp;

public class UsuariDTO {

    private Long id;
    private String nom;
    private String email;
    private String contrasenya;
    private String dataRegistre;
    private String rol;

    private Long tarifaId;
    private String tarifaNom;
    private String tarifaDataInici;
    private String tarifaDataFi;
    private Boolean tarifaCancellada;
    private Boolean subscripcioActiva;

    public UsuariDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public String getDataRegistre() {
        return dataRegistre;
    }

    public String getRol() {
        return rol;
    }

    public Long getTarifaId() {
        return tarifaId;
    }

    public String getTarifaNom() {
        return tarifaNom;
    }

    public String getTarifaDataInici() {
        return tarifaDataInici;
    }

    public String getTarifaDataFi() {
        return tarifaDataFi;
    }

    public Boolean getTarifaCancellada() {
        return tarifaCancellada;
    }

    public Boolean getSubscripcioActiva() {
        return subscripcioActiva;
    }
}