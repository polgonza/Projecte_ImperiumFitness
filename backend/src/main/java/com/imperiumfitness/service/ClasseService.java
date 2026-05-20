// Fragment suggerit per assistent IA - revisar i adaptar
package com.imperiumfitness.service;

import com.imperiumfitness.dto.ClasseDTO;
import com.imperiumfitness.model.entity.Classe;
import com.imperiumfitness.model.entity.Gimnas;
import com.imperiumfitness.repository.ClasseRepository;
import com.imperiumfitness.repository.GimnasRepository;
import com.imperiumfitness.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClasseService {

    private final ClasseRepository repo;
    private final GimnasRepository gimnasRepo;
    private final ReservaRepository reservaRepo;

    public ClasseService(ClasseRepository repo,
                         GimnasRepository gimnasRepo,
                         ReservaRepository reservaRepo) {
        this.repo = repo;
        this.gimnasRepo = gimnasRepo;
        this.reservaRepo = reservaRepo;
    }

    public List<ClasseDTO> getAll(boolean nomesFuturas) {
        List<Classe> classes = nomesFuturas
            ? repo.findByHorariAfterOrderByHorariAsc(LocalDateTime.now())
            : repo.findAll();
        return classes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ClasseDTO getById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Classe no trobada")));
    }

    public ClasseDTO save(ClasseDTO dto) {
        return toDTO(repo.save(toEntity(dto)));
    }

    public ClasseDTO update(Long id, ClasseDTO dto) {
        Classe c = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Classe no trobada"));
        c.setNom(dto.getNom());
        c.setDescripcio(dto.getDescripcio());
        c.setHorari(dto.getHorari());
        c.setCapacitat(dto.getCapacitat());
        if (dto.getGimnasId() != null) {
            Gimnas g = gimnasRepo.findById(dto.getGimnasId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Gimnas no trobat"));
            c.setGimnas(g);
        }
        return toDTO(repo.save(c));
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Classe no trobada");
        reservaRepo.deleteByClasseId(id);
        repo.deleteById(id);
    }

    private ClasseDTO toDTO(Classe c) {
        return new ClasseDTO(
                c.getId(), c.getNom(), c.getDescripcio(), c.getHorari(),
                c.getCapacitat(),
                c.getGimnas() != null ? c.getGimnas().getId() : null
        );
    }

    private Classe toEntity(ClasseDTO dto) {
        Classe c = new Classe();
        c.setNom(dto.getNom());
        c.setDescripcio(dto.getDescripcio());
        c.setHorari(dto.getHorari());
        c.setCapacitat(dto.getCapacitat());
        if (dto.getGimnasId() != null) {
            Gimnas g = gimnasRepo.findById(dto.getGimnasId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Gimnas no trobat"));
            c.setGimnas(g);
        }
        return c;
    }
}