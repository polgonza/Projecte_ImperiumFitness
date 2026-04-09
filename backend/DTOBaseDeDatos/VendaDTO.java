package com.imperiumfitness.backend.dto;

import java.time.LocalDateTime;

public class VendaDTO {

    private Long id;
    private Long producteId;
    private Long usuariId;
    private Integer quantitat;
    private LocalDateTime dataVenda;

    public VendaDTO() {}

    public VendaDTO(Long id, Long producteId, Long usuariId, Integer quantitat, LocalDateTime dataVenda) {
        this.id = id;
        this.producteId = producteId;
        this.usuariId = usuariId;
        this.quantitat = quantitat;
        this.dataVenda = dataVenda;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProducteId() { return producteId; }
    public void setProducteId(Long producteId) { this.producteId = producteId; }

    public Long getUsuariId() { return usuariId; }
    public void setUsuariId(Long usuariId) { this.usuariId = usuariId; }

    public Integer getQuantitat() { return quantitat; }
    public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
}
