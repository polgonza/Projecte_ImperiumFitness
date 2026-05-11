package com.example.gymapp;

/*
    RESERVA DTO
    ===========
    Model que representa una reserva.
    S'usa tant per ENVIAR reserves al backend (POST /api/reserves)
    com per REBRE-LES (GET /api/reserves/usuari/{id}).

    Quan s'envia: usuariId + classeId + dataReserva (horari de la classe)
    Quan es rep:  id + usuariId + classeId + dataReserva + nomClasse (si el backend el retorna)

    @author ImperiumGym
    @version 2.0
*/
public class ReservaDTO {

    private Long id;
    private Long usuariId;
    private Long classeId;
    private String dataReserva;
    private String nomClasse; // Opcional, si el backend el retorna

    // Constructor per enviar reserves (POST)
    public ReservaDTO(Long usuariId, Long classeId, String dataReserva) {
        this.usuariId = usuariId;
        this.classeId = classeId;
        this.dataReserva = dataReserva;
    }

    // Constructor buit necessari perquè Gson pugui deserialitzar (GET)
    public ReservaDTO() {}

    public Long getId() { return id; }
    public Long getUsuariId() { return usuariId; }
    public Long getClasseId() { return classeId; }
    public String getDataReserva() { return dataReserva; }
    public String getNomClasse() { return nomClasse; }
}