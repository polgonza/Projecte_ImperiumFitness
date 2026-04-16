package com.imperiumfitness.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "noticia")
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titol;
    private String contingut;

    @Column(name = "data_publicacio")
    private LocalDateTime dataPublicacio;

    // @ManyToOne: moltes notícies tenen un autor (Usuari)
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuari autor;

    public Noticia() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitol() { return titol; }
    public void setTitol(String titol) { this.titol = titol; }

    public String getContingut() { return contingut; }
    public void setContingut(String contingut) { this.contingut = contingut; }

    public LocalDateTime getDataPublicacio() { return dataPublicacio; }
    public void setDataPublicacio(LocalDateTime dataPublicacio) { this.dataPublicacio = dataPublicacio; }

    public Usuari getAutor() { return autor; }
    public void setAutor(Usuari autor) { this.autor = autor; }
}