package com.imperiumfitness.controller;

import com.imperiumfitness.dto.PagamentDTO;
import com.imperiumfitness.service.PagamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagaments")
public class PagamentController {

    private final PagamentService service;

    public PagamentController(PagamentService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PagamentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/usuari/{usuariId}")
    public ResponseEntity<List<PagamentDTO>> getByUsuari(@PathVariable Long usuariId) {
        return ResponseEntity.ok(service.getByUsuari(usuariId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PagamentDTO> create(@RequestBody PagamentDTO dto) {
        return ResponseEntity.status(201).body(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}