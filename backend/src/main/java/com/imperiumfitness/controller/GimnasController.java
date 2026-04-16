package com.imperiumfitness.controller;

import com.imperiumfitness.dto.GimnasDTO;
import com.imperiumfitness.service.GimnasService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/gimnas")
public class GimnasController {

    private final GimnasService service;

    public GimnasController(GimnasService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GimnasDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GimnasDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // només l'admin pot crear
    public ResponseEntity<GimnasDTO> create(@RequestBody GimnasDTO dto) {
        return ResponseEntity.status(201).body(service.save(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GimnasDTO> update(@PathVariable Long id, @RequestBody GimnasDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}