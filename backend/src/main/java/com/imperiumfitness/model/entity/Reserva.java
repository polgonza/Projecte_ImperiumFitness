package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: moltes reserves pertanyen a un usuari
    @ManyToOne
    @JoinColumn(name = "usuari_id")
    private Usuari usuari;

    // @ManyToOne: moltes reserves pertanyen a una classe
    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @Column(name = "data_reserva")
    private LocalDateTime dataReserva;

    public Reserva() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuari getUsuari() { return usuari; }
    public void setUsuari(Usuari usuari) { this.usuari = usuari; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    public LocalDateTime getDataReserva() { return dataReserva; }
    public void setDataReserva(LocalDateTime dataReserva) { this.dataReserva = dataReserva; }
}