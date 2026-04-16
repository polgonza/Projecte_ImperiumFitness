package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    // Historial de compres d'un usuari
    List<Venda> findByUsuariId(Long usuariId);
    // Vendes d'un producte concret
    List<Venda> findByProducteId(Long producteId);
}