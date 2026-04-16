package com.imperiumfitness.service;

import com.imperiumfitness.dto.GimnasDTO;
import com.imperiumfitness.model.entity.Gimnas;
import com.imperiumfitness.repository.GimnasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GimnasService {

    private final GimnasRepository repo;

    public GimnasService(GimnasRepository repo) {
        this.repo = repo;
    }

    public List<GimnasDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public GimnasDTO getById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Gimnas no trobat")));
    }

    public GimnasDTO save(GimnasDTO dto) {
        return toDTO(repo.save(toEntity(dto)));
    }

    public GimnasDTO update(Long id, GimnasDTO dto) {
        Gimnas g = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Gimnas no trobat"));
        g.setNom(dto.getNom());
        g.setAdreca(dto.getAdreca());
        g.setTelefon(dto.getTelefon());
        return toDTO(repo.save(g));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gimnas no trobat");
        repo.deleteById(id);
    }

    private GimnasDTO toDTO(Gimnas g) {
        return new GimnasDTO(g.getId(), g.getNom(), g.getAdreca(), g.getTelefon());
    }

    private Gimnas toEntity(GimnasDTO dto) {
        Gimnas g = new Gimnas();
        g.setNom(dto.getNom());
        g.setAdreca(dto.getAdreca());
        g.setTelefon(dto.getTelefon());
        return g;
    }
}