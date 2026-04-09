package com.imperiumfitness.backend.dto;

import java.time.LocalDateTime;

public class ReservaDTO {

    private Long id;
    private Long usuariId;
    private Long classeId;
    private LocalDateTime dataReserva;

    public ReservaDTO() {}

    public ReservaDTO(Long id, Long usuariId, Long classeId, LocalDateTime dataReserva) {
        this.id = id;
        this.usuariId = usuariId;
        this.classeId = classeId;
        this.dataReserva = dataReserva;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuariId() { return usuariId; }
    public void setUsuariId(Long usuariId) { this.usuariId = usuariId; }

    public Long getClasseId() { return classeId; }
    public void setClasseId(Long classeId) { this.classeId = classeId; }

    public LocalDateTime getDataReserva() { return dataReserva; }
    public void setDataReserva(LocalDateTime dataReserva) { this.dataReserva = dataReserva; }
}
