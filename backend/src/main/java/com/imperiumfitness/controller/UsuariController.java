package com.imperiumfitness.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;

import com.imperiumfitness.dto.UsuariDTO;
import com.imperiumfitness.service.UsuariService;

@RestController
@RequestMapping("/api/usuaris")
public class UsuariController {

    private final UsuariService service;

    // ✅ Injecció per constructor (millor pràctica)
    public UsuariController(UsuariService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuariDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuariDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<UsuariDTO> create(@RequestBody UsuariDTO dto) {
        return ResponseEntity.status(201).body(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint de perfil accessible per USER i ADMIN
    @GetMapping("/perfil/{id}")
    public ResponseEntity<UsuariDTO> getPerfil(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}/tarifa")
    public ResponseEntity<UsuariDTO> assignarTarifa(
        @PathVariable Long id,
        @RequestBody Map<String, Long> body) {
    return ResponseEntity.ok(service.assignarTarifa(id, body.get("tarifaId")));
}

    @PutMapping("/{id}/cancel-tarifa")
    public ResponseEntity<UsuariDTO> cancelarTarifa(@PathVariable Long id) {
    return ResponseEntity.ok(service.cancelarTarifa(id));
}
// Fragment suggerit per assistent IA - revisar i adaptar
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/rol")
    public ResponseEntity<UsuariDTO> canviarRol(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String nouRol = body.get("rol");
        if (nouRol == null || (!nouRol.equalsIgnoreCase("ADMIN") && !nouRol.equalsIgnoreCase("USER"))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.canviarRol(id, nouRol.toUpperCase()));
    }
}