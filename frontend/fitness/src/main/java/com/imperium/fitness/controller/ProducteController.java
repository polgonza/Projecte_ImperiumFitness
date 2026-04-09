package com.imperium.fitness.controller;

import com.imperium.fitness.entitat.Producte;
import com.imperium.fitness.servei.ProducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productes")
@CrossOrigin(origins = "*")
public class ProducteController {
    
    @Autowired
    private ProducteService producteService;
    
    @GetMapping
    public ResponseEntity<List<Producte>> obtenirTots() {
        List<Producte> productes = producteService.obtenirTots();
        return ResponseEntity.ok(productes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenirPerId(@PathVariable Long id) {
        try {
            Producte producte = producteService.obtenirPerId(id);
            return ResponseEntity.ok(producte);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producte>> obtenirPerCategoria(@PathVariable String categoria) {
        List<Producte> productes = producteService.obtenirPerCategoria(categoria);
        return ResponseEntity.ok(productes);
    }
    
    @GetMapping("/stock")
    public ResponseEntity<List<Producte>> obtenirEnStock() {
        List<Producte> productes = producteService.obtenirEnStock();
        return ResponseEntity.ok(productes);
    }
    
    @GetMapping("/cercar")
    public ResponseEntity<List<Producte>> cercarPerNom(@RequestParam String nom) {
        List<Producte> productes = producteService.cercarPerNom(nom);
        return ResponseEntity.ok(productes);
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producte producte) {
        try {
            Producte nouProducte = producteService.crear(producte);
            return new ResponseEntity<>(nouProducte, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el producto: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producte producte) {
        try {
            Producte producteActualitzat = producteService.actualizar(id, producte);
            return ResponseEntity.ok(producteActualitzat);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            producteService.eliminar(id);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("missatge", "Producte eliminat correctament");
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> reduirStock(@PathVariable Long id, @RequestBody Map<String, Integer> dades) {
        try {
            Integer quantitat = dades.get("quantitat");
            if (quantitat == null || quantitat <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "La quantitat ha de ser major que 0");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            
            Producte producte = producteService.reduirStock(id, quantitat);
            return ResponseEntity.ok(producte);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}