package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: moltes vendes fan referència a un producte
    @ManyToOne
    @JoinColumn(name = "producte_id")
    private Producte producte;

    // @ManyToOne: moltes vendes pertanyen a un usuari
    @ManyToOne
    @JoinColumn(name = "usuari_id")
    private Usuari usuari;

    private Integer quantitat;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    public Venda() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Producte getProducte() { return producte; }
    public void setProducte(Producte producte) { this.producte = producte; }

    public Usuari getUsuari() { return usuari; }
    public void setUsuari(Usuari usuari) { this.usuari = usuari; }

    public Integer getQuantitat() { return quantitat; }
    public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
}