package com.imperiumfitness.backend.dto;

import java.time.LocalDateTime;

public class EstadisticaDTO {

    private Long id;
    private String tipus;
    private Integer valor;
    private LocalDateTime data;
    private Long gimnasId;

    public EstadisticaDTO() {}

    public EstadisticaDTO(Long id, String tipus, Integer valor, LocalDateTime data, Long gimnasId) {
        this.id = id;
        this.tipus = tipus;
        this.valor = valor;
        this.data = data;
        this.gimnasId = gimnasId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }

    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public Long getGimnasId() { return gimnasId; }
    public void setGimnasId(Long gimnasId) { this.gimnasId = gimnasId; }
}
