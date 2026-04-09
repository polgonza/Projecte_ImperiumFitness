package com.imperium.fitness.repositori;

import com.imperium.fitness.entitat.Producte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProducteRepository extends JpaRepository<Producte, Long> {
    
    List<Producte> findByCategoria(String categoria);
    
    List<Producte> findByEstocGreaterThan(Integer estoc);
    
    List<Producte> findByNomContainingIgnoreCase(String nom);
}