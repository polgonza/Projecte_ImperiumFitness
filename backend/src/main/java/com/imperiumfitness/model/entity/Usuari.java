package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.imperiumfitness.model.entity.Tarifa;

@Entity
@Table(name = "usuari")
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tarifa_id")
    private Tarifa tarifa;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contrasenya;

    @Column(name = "data_registre") 
    private LocalDateTime dataRegistre;

    @Column(nullable = false)
    private String rol;

    @Column(name = "tarifa_data_inici")
    private LocalDateTime tarifaDataInici;

    @Column(name = "tarifa_data_fi")
    private LocalDateTime tarifaDataFi;

    @Column(name = "tarifa_cancellada")
    private Boolean tarifaCancellada = false;

    public Usuari() {}

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

    public Tarifa getTarifa() { return tarifa; }
    public void setTarifa(Tarifa tarifa) { this.tarifa = tarifa; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    // Getters i setters
    public LocalDateTime getTarifaDataInici() { return tarifaDataInici; }
    public void setTarifaDataInici(LocalDateTime t) { this.tarifaDataInici = t; }

    public LocalDateTime getTarifaDataFi() { return tarifaDataFi; }
    public void setTarifaDataFi(LocalDateTime t) { this.tarifaDataFi = t; }

    public Boolean getTarifaCancellada() { return tarifaCancellada; }
    public void setTarifaCancellada(Boolean t) { this.tarifaCancellada = t; }

    public boolean isSubscripcioActiva() {
        if (tarifa == null) return false;
        if (tarifaDataFi == null) return false;
        return LocalDateTime.now().isBefore(tarifaDataFi) && 
           !Boolean.TRUE.equals(tarifaCancellada);
    }
}