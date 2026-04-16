package com.imperiumfitness.controller;

import com.imperiumfitness.dto.ContacteDTO;
import com.imperiumfitness.service.ContacteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contactes")
public class ContacteController {

    private final ContacteService service;

    public ContacteController(ContacteService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContacteDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping // públic: qualsevol pot enviar un missatge de contacte
    public ResponseEntity<ContacteDTO> create(@RequestBody ContacteDTO dto) {
        return ResponseEntity.status(201).body(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}