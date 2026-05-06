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
            null, // contrasenya mai en resposta
            u.getDataRegistre(),
            u.getRol(),
            u.getTarifa() != null ? u.getTarifa().getId()  : null,
            u.getTarifa() != null ? u.getTarifa().getNom() : null
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

    public UsuariDTO assignarTarifa(Long usuariId, Long tarifaId) {
    Usuari u = repo.findById(usuariId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuari no trobat"));

    if (tarifaId == null) {
        u.setTarifa(null); // cancel·lar tarifa
    } else {
        // Necessitem TarifaRepository — afegeix-lo al constructor
        Tarifa t = tarifaRepo.findById(tarifaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tarifa no trobada"));
        u.setTarifa(t);
    }

    return toDTO(repo.save(u));
}
}