package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estadistica")
public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipus;
    private Integer valor;
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "gimnas_id")
    private Gimnas gimnas;

    public Estadistica() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }

    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public Gimnas getGimnas() { return gimnas; }
    public void setGimnas(Gimnas gimnas) { this.gimnas = gimnas; }
}