package com.imperium.fitness.servei;

import com.imperium.fitness.entitat.Producte;
import com.imperium.fitness.repositori.ProducteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProducteService {
    
    @Autowired
    private ProducteRepository producteRepository;
    
    public List<Producte> obtenirTots() {
        return producteRepository.findAll();
    }
    
    public Producte obtenirPerId(Long id) {
        return producteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }
    
    public List<Producte> obtenirPerCategoria(String categoria) {
        return producteRepository.findByCategoria(categoria);
    }
    
    public List<Producte> obtenirEnStock() {
        return producteRepository.findByEstocGreaterThan(0);
    }
    
    public List<Producte> cercarPerNom(String nom) {
        return producteRepository.findByNomContainingIgnoreCase(nom);
    }

    public Producte crear(Producte producte) {
        return producteRepository.save(producte);
    }

    public Producte actualizar(Long id, Producte producteActualitzat) {
        Producte producteExistent = obtenirPerId(id);
        
        producteExistent.setNom(producteActualitzat.getNom());
        producteExistent.setDescripcio(producteActualitzat.getDescripcio());
        producteExistent.setPreu(producteActualitzat.getPreu());
        producteExistent.setCategoria(producteActualitzat.getCategoria());
        producteExistent.setEstoc(producteActualitzat.getEstoc());
        
        return producteRepository.save(producteExistent);
    }
    
    public void eliminar(Long id) {
        if (!producteRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        producteRepository.deleteById(id);
    }

    public Producte reduirStock(Long id, Integer quantitat) {
        Producte producte = obtenirPerId(id);
        
        if (producte.getEstoc() < quantitat) {
            throw new RuntimeException("Stock insuficiente. Solo quedan " + producte.getEstoc() + " unidades");
        }
        
        producte.setEstoc(producte.getEstoc() - quantitat);
        return producteRepository.save(producte);
    }
}