package com.example.gymapp;

public class VendaDTO {
    private Long id;
    private Long producteId;
    private Long usuariId;
    private Integer quantitat;
    private String dataVenda;

    public VendaDTO() {
    }

    public VendaDTO(Long producteId, Long usuariId, Integer quantitat) {
        this.producteId = producteId;
        this.usuariId = usuariId;
        this.quantitat = quantitat;
    }

    public Long getId() {
        return id;
    }

    public Long getProducteId() {
        return producteId;
    }

    public Long getUsuariId() {
        return usuariId;
    }

    public Integer getQuantitat() {
        return quantitat;
    }

    public String getDataVenda() {
        return dataVenda;
    }
}
