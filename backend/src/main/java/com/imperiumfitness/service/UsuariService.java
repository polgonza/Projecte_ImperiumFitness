package com.imperiumfitness.service;

import com.imperiumfitness.dto.UsuariDTO;
import com.imperiumfitness.model.entity.Usuari;
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

    public UsuariService(UsuariRepository repo) {
        this.repo = repo;
    }

    // ── Obtenir tots els usuaris (sense contrasenya!) ────────────────────────
    public List<UsuariDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Obtenir un usuari per ID ─────────────────────────────────────────────
    public UsuariDTO getById(Long id) {
        Usuari u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuari amb id " + id + " no trobat"));
        return toDTO(u);
    }

    // ── Crear un nou usuari ──────────────────────────────────────────────────
    // IMPORTANT: el hashejat de contrasenya es fa a AuthService en el registre.
    // Aquest mètode és per creació directa per part de l'ADMIN.
    public UsuariDTO save(UsuariDTO dto) {
        Usuari u = toEntity(dto);
        return toDTO(repo.save(u));
    }

    // ── Eliminar un usuari per ID ────────────────────────────────────────────
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuari amb id " + id + " no trobat");
        }
        repo.deleteById(id);
    }

    // ── Conversió Entity → DTO (mai retornem la contrasenya!) ───────────────
    private UsuariDTO toDTO(Usuari u) {
        return new UsuariDTO(
                u.getId(),
                u.getNom(),
                u.getEmail(),
                null,               // contrasenya sempre null en la resposta
                u.getDataRegistre(),
                u.getRol()
        );
    }

    // ── Conversió DTO → Entity ───────────────────────────────────────────────
    private Usuari toEntity(UsuariDTO dto) {
        Usuari u = new Usuari();
        u.setNom(dto.getNom());
        u.setEmail(dto.getEmail());
        u.setContrasenya(dto.getContrasenya()); // s'ha de passar ja hashejada
        u.setRol(dto.getRol() != null ? dto.getRol() : "USER");
        u.setDataRegistre(LocalDateTime.now());
        return u;
    }
}