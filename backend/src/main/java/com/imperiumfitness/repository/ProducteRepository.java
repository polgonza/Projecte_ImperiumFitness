package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Producte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProducteRepository extends JpaRepository<Producte, Long> {
    // Productes per categoria (ex: "suplement", "roba"...)
    List<Producte> findByCategoria(String categoria);
    // Productes amb estoc disponible
    List<Producte> findByEstocGreaterThan(Integer estoc);
}