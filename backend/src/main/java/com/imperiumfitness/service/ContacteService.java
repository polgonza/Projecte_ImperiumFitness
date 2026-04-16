package com.imperiumfitness.service;

import com.imperiumfitness.dto.ContacteDTO;
import com.imperiumfitness.model.entity.Contacte;
import com.imperiumfitness.repository.ContacteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContacteService {

    private final ContacteRepository repo;

    public ContacteService(ContacteRepository repo) {
        this.repo = repo;
    }

    public List<ContacteDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Qualsevol pot enviar un contacte (endpoint públic)
    public ContacteDTO save(ContacteDTO dto) {
        Contacte c = toEntity(dto);
        c.setDataEnviament(LocalDateTime.now());
        return toDTO(repo.save(c));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contacte no trobat");
        repo.deleteById(id);
    }

    private ContacteDTO toDTO(Contacte c) {
        return new ContacteDTO(c.getId(), c.getNom(), c.getEmail(),
                c.getMissatge(), c.getDataEnviament());
    }

    private Contacte toEntity(ContacteDTO dto) {
        Contacte c = new Contacte();
        c.setNom(dto.getNom());
        c.setEmail(dto.getEmail());
        c.setMissatge(dto.getMissatge());
        return c;
    }
}