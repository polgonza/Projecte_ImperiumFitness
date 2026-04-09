package com.imperium.fitness.controller;

import com.imperium.fitness.entitat.Contacte;
import com.imperium.fitness.servei.ContacteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contacte")
@CrossOrigin(origins = "*")
public class ContacteController {
    
    @Autowired
    private ContacteService contacteService;

    @PostMapping
    public ResponseEntity<?> enviarMissatge(@RequestBody Map<String, String> dades) {
        try {
            String nom = dades.get("nom");
            String email = dades.get("email");
            String missatge = dades.get("missatge");
            
            if (nom == null || nom.isEmpty() ||
                email == null || email.isEmpty() ||
                missatge == null || missatge.isEmpty()) {
                
                Map<String, String> error = new HashMap<>();
                error.put("error", "Tots els camps són obligatoris");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            
            Contacte nouContacte = new Contacte(nom, email, missatge);
            Contacte guardat = contacteService.guardar(nouContacte);
            
            Map<String, String> resposta = new HashMap<>();
            resposta.put("missatge", "Missatge enviat correctament");
            resposta.put("id", guardat.getId().toString());
            
            return new ResponseEntity<>(resposta, HttpStatus.CREATED);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al enviar el missatge: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Contacte>> obtenirTots() {
        List<Contacte> contactes = contacteService.obtenirTots();
        return ResponseEntity.ok(contactes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenirPerId(@PathVariable Long id) {
        try {
            Contacte contacte = contacteService.obtenirPerId(id);
            return ResponseEntity.ok(contacte);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            contacteService.eliminar(id);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("missatge", "Missatge eliminat correctament");
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}