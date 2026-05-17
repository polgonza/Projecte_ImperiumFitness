package com.imperiumfitness.controller;

import com.imperiumfitness.dto.ReservaDTO;
import com.imperiumfitness.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reserves")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
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

    @DeleteMapping("/usuari/{usuariId}/classe/{classeId}")
    public ResponseEntity<Void> cancelar(
        @PathVariable Long usuariId,
        @PathVariable Long classeId) {
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