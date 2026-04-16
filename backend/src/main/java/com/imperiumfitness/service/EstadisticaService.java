package com.imperiumfitness.service;

import com.imperiumfitness.dto.EstadisticaDTO;
import com.imperiumfitness.model.entity.Estadistica;
import com.imperiumfitness.model.entity.Gimnas;
import com.imperiumfitness.repository.EstadisticaRepository;
import com.imperiumfitness.repository.GimnasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadisticaService {

    private final EstadisticaRepository repo;
    private final GimnasRepository gimnasRepo;

    public EstadisticaService(EstadisticaRepository repo, GimnasRepository gimnasRepo) {
        this.repo = repo;
        this.gimnasRepo = gimnasRepo;
    }

    public List<EstadisticaDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EstadisticaDTO save(EstadisticaDTO dto) {
        return toDTO(repo.save(toEntity(dto)));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estadística no trobada");
        repo.deleteById(id);
    }

    private EstadisticaDTO toDTO(Estadistica e) {
        return new EstadisticaDTO(e.getId(), e.getTipus(), e.getValor(), e.getData(),
                e.getGimnas() != null ? e.getGimnas().getId() : null);
    }

    private Estadistica toEntity(EstadisticaDTO dto) {
        Estadistica e = new Estadistica();
        e.setTipus(dto.getTipus());
        e.setValor(dto.getValor());
        e.setData(dto.getData());
        if (dto.getGimnasId() != null) {
            Gimnas g = gimnasRepo.findById(dto.getGimnasId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gimnas no trobat"));
            e.setGimnas(g);
        }
        return e;
    }
}