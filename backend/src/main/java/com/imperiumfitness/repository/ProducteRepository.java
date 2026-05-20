package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Producte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProducteRepository extends JpaRepository<Producte, Long> {

    List<Producte> findByCategoria(String categoria);

    List<Producte> findByEstocGreaterThan(Integer estoc);
    Optional<Producte> findTopByOrderByEstocAsc();

List<Producte> findByEstocLessThanEqualOrderByEstocAsc(Integer estoc);
}