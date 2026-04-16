package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagament")
public class Pagament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: molts pagaments pertanyen a un usuari
    @ManyToOne
    @JoinColumn(name = "usuari_id")
    private Usuari usuari;

    @Column(name = "import_total")
    private BigDecimal importTotal;

    @Column(name = "data_pagament")
    private LocalDateTime dataPagament;

    @Column(name = "metode_pagament")
    private String metodePagament;

    public Pagament() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuari getUsuari() { return usuari; }
    public void setUsuari(Usuari usuari) { this.usuari = usuari; }

    public BigDecimal getImportTotal() { return importTotal; }
    public void setImportTotal(BigDecimal importTotal) { this.importTotal = importTotal; }

    public LocalDateTime getDataPagament() { return dataPagament; }
    public void setDataPagament(LocalDateTime dataPagament) { this.dataPagament = dataPagament; }

    public String getMetodePagament() { return metodePagament; }
    public void setMetodePagament(String metodePagament) { this.metodePagament = metodePagament; }
}