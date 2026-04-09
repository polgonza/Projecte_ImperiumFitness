package com.imperiumfitness.backend.dto;

import java.time.LocalDateTime;

public class NoticiaDTO {

    private Long id;
    private String titol;
    private String contingut;
    private LocalDateTime dataPublicacio;
    private Long autorId;

    public NoticiaDTO() {}

    public NoticiaDTO(Long id, String titol, String contingut, LocalDateTime dataPublicacio, Long autorId) {
        this.id = id;
        this.titol = titol;
        this.contingut = contingut;
        this.dataPublicacio = dataPublicacio;
        this.autorId = autorId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitol() { return titol; }
    public void setTitol(String titol) { this.titol = titol; }

    public String getContingut() { return contingut; }
    public void setContingut(String contingut) { this.contingut = contingut; }

    public LocalDateTime getDataPublicacio() { return dataPublicacio; }
    public void setDataPublicacio(LocalDateTime dataPublicacio) { this.dataPublicacio = dataPublicacio; }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }
}
