package com.imperiumfitness.service;

import com.imperiumfitness.dto.ReservaDTO;
import com.imperiumfitness.model.entity.Classe;
import com.imperiumfitness.model.entity.Reserva;
import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.repository.ClasseRepository;
import com.imperiumfitness.repository.ReservaRepository;
import com.imperiumfitness.repository.UsuariRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository repo;
    private final UsuariRepository usuariRepo;
    private final ClasseRepository classeRepo;

    public ReservaService(ReservaRepository repo,
                          UsuariRepository usuariRepo,
                          ClasseRepository classeRepo) {
        this.repo = repo;
        this.usuariRepo = usuariRepo;
        this.classeRepo = classeRepo;
    }

    public List<ReservaDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ReservaDTO> getByUsuari(Long usuariId) {
        return repo.findByUsuariId(usuariId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ReservaDTO getById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reserva no trobada")));
    }

    public ReservaDTO save(ReservaDTO dto) {
        // Regla de negoci: un usuari no pot reservar dues vegades la mateixa classe
        if (repo.existsByUsuariIdAndClasseId(dto.getUsuariId(), dto.getClasseId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "L'usuari ja té una reserva per aquesta classe");
        }
        Reserva r = toEntity(dto);
        r.setDataReserva(LocalDateTime.now()); // assignem la data automàticament
        return toDTO(repo.save(r));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no trobada");
        repo.deleteById(id);
    }

    private ReservaDTO toDTO(Reserva r) {
        return new ReservaDTO(
                r.getId(),
                r.getUsuari() != null ? r.getUsuari().getId() : null,
                r.getClasse() != null ? r.getClasse().getId() : null,
                r.getDataReserva()
        );
    }

    private Reserva toEntity(ReservaDTO dto) {
        Reserva r = new Reserva();
        Usuari u = usuariRepo.findById(dto.getUsuariId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuari no trobat"));
        Classe c = classeRepo.findById(dto.getClasseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Classe no trobada"));
        r.setUsuari(u);
        r.setClasse(c);
        return r;
    }
}