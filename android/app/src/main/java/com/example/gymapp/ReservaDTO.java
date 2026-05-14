package com.example.gymapp;

/*
    RESERVA DTO
    ===========
    Model que representa una reserva.

    Para CREAR una reserva enviamos solo:
    - usuariId
    - classeId

    El backend actual ya pone la dataReserva internamente.

    Para LEER reservas del calendario recibimos:
    - id
    - usuariId
    - classeId
    - dataReserva
    - nomClasse si algún día el backend lo devuelve
*/
public class ReservaDTO {

    private Long id;
    private Long usuariId;
    private Long classeId;
    private String dataReserva;
    private String nomClasse;

    public ReservaDTO() {
    }

    // Constructor para crear reserva desde Android
    public ReservaDTO(Long usuariId, Long classeId) {
        this.usuariId = usuariId;
        this.classeId = classeId;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuariId() {
        return usuariId;
    }

    public Long getClasseId() {
        return classeId;
    }

    public String getDataReserva() {
        return dataReserva;
    }

    public String getNomClasse() {
        return nomClasse;
    }
}