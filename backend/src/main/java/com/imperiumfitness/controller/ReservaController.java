package com.imperiumfitness.controller;

import com.imperiumfitness.dto.ReservaDTO;
import com.imperiumfitness.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.repository.UsuariRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reserves")
public class ReservaController {

    private final ReservaService service;
    private final UsuariRepository usuariRepo;

    public ReservaController(ReservaService service, UsuariRepository usuariRepo) {
        this.service    = service;
        this.usuariRepo = usuariRepo;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservaDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // Un usuari pot veure les seves pròpies reserves
    @GetMapping("/usuari/{usuariId}")
    public ResponseEntity<List<ReservaDTO>> getByUsuari(@PathVariable Long usuariId) {
        return ResponseEntity.ok(service.getByUsuari(usuariId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> create(@RequestBody ReservaDTO dto) {
        return ResponseEntity.status(201).body(service.save(dto));
    }

    // Fragment suggerit per assistent IA - revisar i adaptar
    @DeleteMapping("/usuari/{usuariId}/classe/{classeId}")
    public ResponseEntity<Void> cancelar(
            @PathVariable Long usuariId,
            @PathVariable Long classeId,
            Authentication auth) {
        String emailToken = auth.getName();
        Usuari usuariToken = usuariRepo.findByEmail(emailToken)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "No autoritzat"));
        boolean esAdmin = "ADMIN".equalsIgnoreCase(usuariToken.getRol());
        if (!esAdmin && !usuariToken.getId().equals(usuariId)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "No pots cancel·lar reserves d'altres usuaris");
        }
        service.cancelarReserva(usuariId, classeId);
        return ResponseEntity.noContent().build();
    }
    // Fragment suggerit per assistent IA - revisar i adaptar
    // Retorna el nombre de reserves d'una classe (per calcular places disponibles)
    @GetMapping("/classe/{classeId}/count")
    public ResponseEntity<Long> countByClasse(@PathVariable Long classeId) {
        return ResponseEntity.ok((long) service.getByClasse(classeId).size());
    }
}