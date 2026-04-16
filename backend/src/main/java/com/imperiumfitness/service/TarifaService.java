package com.imperiumfitness.service;

import com.imperiumfitness.dto.TarifaDTO;
import com.imperiumfitness.model.entity.Gimnas;
import com.imperiumfitness.model.entity.Tarifa;
import com.imperiumfitness.repository.GimnasRepository;
import com.imperiumfitness.repository.TarifaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarifaService {

    private final TarifaRepository repo;
    private final GimnasRepository gimnasRepo;

    public TarifaService(TarifaRepository repo, GimnasRepository gimnasRepo) {
        this.repo = repo;
        this.gimnasRepo = gimnasRepo;
    }

    public List<TarifaDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TarifaDTO getById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarifa no trobada")));
    }

    public TarifaDTO save(TarifaDTO dto) {
        return toDTO(repo.save(toEntity(dto)));
    }

    public TarifaDTO update(Long id, TarifaDTO dto) {
        Tarifa t = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarifa no trobada"));
        t.setNom(dto.getNom());
        t.setPreu(dto.getPreu());
        t.setDescripcio(dto.getDescripcio());
        return toDTO(repo.save(t));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarifa no trobada");
        repo.deleteById(id);
    }

    private TarifaDTO toDTO(Tarifa t) {
        return new TarifaDTO(t.getId(), t.getNom(), t.getPreu(), t.getDescripcio(),
                t.getGimnas() != null ? t.getGimnas().getId() : null);
    }

    private Tarifa toEntity(TarifaDTO dto) {
        Tarifa t = new Tarifa();
        t.setNom(dto.getNom());
        t.setPreu(dto.getPreu());
        t.setDescripcio(dto.getDescripcio());
        if (dto.getGimnasId() != null) {
            Gimnas g = gimnasRepo.findById(dto.getGimnasId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gimnas no trobat"));
            t.setGimnas(g);
        }
        return t;
    }
}