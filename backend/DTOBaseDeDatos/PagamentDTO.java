package com.imperiumfitness.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentDTO {

    private Long id;
    private Long usuariId;
    private BigDecimal import_;
    private LocalDateTime dataPagament;
    private String metodePagament;

    public PagamentDTO() {}

    public PagamentDTO(Long id, Long usuariId, BigDecimal import_, LocalDateTime dataPagament, String metodePagament) {
        this.id = id;
        this.usuariId = usuariId;
        this.import_ = import_;
        this.dataPagament = dataPagament;
        this.metodePagament = metodePagament;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuariId() { return usuariId; }
    public void setUsuariId(Long usuariId) { this.usuariId = usuariId; }

    public BigDecimal getImport() { return import_; }
    public void setImport(BigDecimal import_) { this.import_ = import_; }

    public LocalDateTime getDataPagament() { return dataPagament; }
    public void setDataPagament(LocalDateTime dataPagament) { this.dataPagament = dataPagament; }

    public String getMetodePagament() { return metodePagament; }
    public void setMetodePagament(String metodePagament) { this.metodePagament = metodePagament; }
}
