package com.imperium.fitness.controller;

import com.imperium.fitness.entitat.Usuario;
import com.imperium.fitness.servei.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuaris")
@CrossOrigin(origins = "*")
public class UsuariController {
    
    @Autowired
    private UsuariService usuariService;
    
    @PostMapping("/registre")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nouUsuari = usuariService.registrar(usuario);
            return new ResponseEntity<>(nouUsuari, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credencials) {
        String email = credencials.get("email");
        String contrasenya = credencials.get("contrasenya");
        
        try {
            Usuario usuari = usuariService.login(email, contrasenya);
            return ResponseEntity.ok(usuari);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenirTots() {
        List<Usuario> usuaris = usuariService.obtenirTots();
        return ResponseEntity.ok(usuaris);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenirPerId(@PathVariable Long id) {
        try {
            Usuario usuari = usuariService.obtenirPerId(id);
            return ResponseEntity.ok(usuari);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}