package com.imperiumfitness.controller;

import com.imperiumfitness.dto.VendaDTO;
import com.imperiumfitness.service.VendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendes")
public class VendaController {

    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VendaDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/usuari/{usuariId}")
    public ResponseEntity<List<VendaDTO>> getByUsuari(@PathVariable Long usuariId) {
        return ResponseEntity.ok(service.getByUsuari(usuariId));
    }

    @PostMapping
    public ResponseEntity<VendaDTO> create(@RequestBody VendaDTO dto) {
        return ResponseEntity.status(201).body(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}