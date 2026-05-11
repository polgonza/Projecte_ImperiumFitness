package com.imperiumfitness.dto;

import java.time.LocalDateTime;

public class UsuariDTO {

    private Long id;
    private String nom;
    private String email;
    private String contrasenya;
    private LocalDateTime dataRegistre;
    private String rol;
    private Long tarifaId;
    private String tarifaNom;
    private LocalDateTime tarifaDataInici;
    private LocalDateTime tarifaDataFi;
    private Boolean tarifaCancellada;
    private Boolean subscripcioActiva;

    public UsuariDTO() {}

    public UsuariDTO(Long id, String nom, String email, String contrasenya,
                 LocalDateTime dataRegistre, String rol,
                 Long tarifaId, String tarifaNom) {
    this.id = id;
    this.nom = nom;
    this.email = email;
    this.contrasenya = contrasenya;
    this.dataRegistre = dataRegistre;
    this.rol = rol;
    this.tarifaId = tarifaId;
    this.tarifaNom = tarifaNom;
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

    public Long getTarifaId() { return tarifaId; }
    public void setTarifaId(Long tarifaId) { this.tarifaId = tarifaId; }
    
    public String getTarifaNom() { return tarifaNom; }
    public void setTarifaNom(String tarifaNom) { this.tarifaNom = tarifaNom; }

    public LocalDateTime getTarifaDataInici() { return tarifaDataInici; }
    public void setTarifaDataInici(LocalDateTime t) { this.tarifaDataInici = t; }
    
    public LocalDateTime getTarifaDataFi() { return tarifaDataFi; }
    public void setTarifaDataFi(LocalDateTime t) { this.tarifaDataFi = t; }
    
    public Boolean getTarifaCancellada() { return tarifaCancellada; }
    public void setTarifaCancellada(Boolean t) { this.tarifaCancellada = t; }
    
    public Boolean getSubscripcioActiva() { return subscripcioActiva; }
    public void setSubscripcioActiva(Boolean t) { this.subscripcioActiva = t; }
}
