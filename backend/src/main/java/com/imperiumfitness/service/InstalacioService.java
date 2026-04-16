package com.imperiumfitness.service;

import com.imperiumfitness.dto.InstalacioDTO;
import com.imperiumfitness.model.entity.Gimnas;
import com.imperiumfitness.model.entity.Instalacio;
import com.imperiumfitness.repository.GimnasRepository;
import com.imperiumfitness.repository.InstalacioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstalacioService {

    private final InstalacioRepository repo;
    private final GimnasRepository gimnasRepo;

    public InstalacioService(InstalacioRepository repo, GimnasRepository gimnasRepo) {
        this.repo = repo;
        this.gimnasRepo = gimnasRepo;
    }

    public List<InstalacioDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public InstalacioDTO getById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instal·lació no trobada")));
    }

    public InstalacioDTO save(InstalacioDTO dto) {
        return toDTO(repo.save(toEntity(dto)));
    }

    public InstalacioDTO update(Long id, InstalacioDTO dto) {
        Instalacio i = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instal·lació no trobada"));
        i.setNom(dto.getNom());
        i.setDescripcio(dto.getDescripcio());
        i.setUbicacio(dto.getUbicacio());
        return toDTO(repo.save(i));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Instal·lació no trobada");
        repo.deleteById(id);
    }

    private InstalacioDTO toDTO(Instalacio i) {
        return new InstalacioDTO(i.getId(), i.getNom(), i.getDescripcio(), i.getUbicacio(),
                i.getGimnas() != null ? i.getGimnas().getId() : null);
    }

    private Instalacio toEntity(InstalacioDTO dto) {
        Instalacio i = new Instalacio();
        i.setNom(dto.getNom());
        i.setDescripcio(dto.getDescripcio());
        i.setUbicacio(dto.getUbicacio());
        if (dto.getGimnasId() != null) {
            Gimnas g = gimnasRepo.findById(dto.getGimnasId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gimnas no trobat"));
            i.setGimnas(g);
        }
        return i;
    }
}