package com.imperiumfitness.service;

import com.imperiumfitness.dto.UsuariDTO;
import com.imperiumfitness.model.entity.Tarifa;
import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.repository.TarifaRepository;
import com.imperiumfitness.repository.UsuariRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuariService {

    private final UsuariRepository repo;
    private final TarifaRepository tarifaRepo;

    public UsuariService(UsuariRepository repo, TarifaRepository tarifaRepo) {
    this.repo = repo;
    this.tarifaRepo = tarifaRepo;
}

    public List<UsuariDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuariDTO getById(Long id) {
        Usuari u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuari amb id " + id + " no trobat"));
        return toDTO(u);
    }

    public UsuariDTO save(UsuariDTO dto) {
        Usuari u = toEntity(dto);
        return toDTO(repo.save(u));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuari amb id " + id + " no trobat");
        }
        repo.deleteById(id);
    }

   private UsuariDTO toDTO(Usuari u) {
    UsuariDTO dto = new UsuariDTO(
            u.getId(), u.getNom(), u.getEmail(), null,
            u.getDataRegistre(), u.getRol(),
            u.getTarifa() != null ? u.getTarifa().getId()  : null,
            u.getTarifa() != null ? u.getTarifa().getNom() : null
    );
    dto.setTarifaDataInici(u.getTarifaDataInici());
    dto.setTarifaDataFi(u.getTarifaDataFi());
    dto.setTarifaCancellada(u.getTarifaCancellada());
    dto.setSubscripcioActiva(u.isSubscripcioActiva());
    return dto;
}

    private Usuari toEntity(UsuariDTO dto) {
        Usuari u = new Usuari();
        u.setNom(dto.getNom());
        u.setEmail(dto.getEmail());
        u.setContrasenya(dto.getContrasenya()); 
        u.setRol(dto.getRol() != null ? dto.getRol() : "USER");
        u.setDataRegistre(LocalDateTime.now());
        return u;
    }

    public UsuariDTO assignarTarifa(Long usuariId, Long tarifaId) {
        Usuari u = repo.findById(usuariId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuari no trobat"));

        if (tarifaId == null) {
            u.setTarifa(null);
            u.setTarifaDataInici(null);
            u.setTarifaDataFi(null);
            u.setTarifaCancellada(false);
            return toDTO(repo.save(u));
        }

        Tarifa novaTarifa = tarifaRepo.findById(tarifaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tarifa no trobada"));

        LocalDateTime ara = LocalDateTime.now();

        boolean teActivaVigent = u.getTarifa() != null
                && u.getTarifaDataFi() != null
                && u.getTarifaDataFi().isAfter(ara)
                && !Boolean.TRUE.equals(u.getTarifaCancellada());

        if (teActivaVigent) {

            LocalDateTime iniciNova = u.getTarifaDataFi();
            u.setTarifa(novaTarifa);
            u.setTarifaDataInici(iniciNova);
            u.setTarifaDataFi(iniciNova.plusMonths(1));
            u.setTarifaCancellada(false);
        } else {

            u.setTarifa(novaTarifa);
            u.setTarifaDataInici(ara);
            u.setTarifaDataFi(ara.plusMonths(1));
            u.setTarifaCancellada(false);
        }

        return toDTO(repo.save(u));
    }
    
public UsuariDTO cancelarTarifa(Long usuariId) {
    Usuari u = repo.findById(usuariId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuari no trobat"));

    u.setTarifaCancellada(true);
    return toDTO(repo.save(u));
}

    public UsuariDTO canviarRol(Long id, String nouRol) {
        Usuari usuari = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuari no trobat: " + id));
        usuari.setRol(nouRol);
        return toDTO(repo.save(usuari));
    }
}