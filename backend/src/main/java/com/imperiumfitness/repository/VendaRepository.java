package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    // Historial de compres d'un usuari
    List<Venda> findByUsuariId(Long usuariId);
    // Vendes d'un producte concret
    List<Venda> findByProducteId(Long producteId);
    // Producte més venut des d'una data
@Query("SELECT p.nom, SUM(v.quantitat) as total FROM Venda v " +
       "JOIN v.producte p WHERE v.dataVenda >= :des " +
       "GROUP BY p.nom ORDER BY total DESC")
List<Object[]> findTopProductesByMes(@Param("des") LocalDateTime des);

// Total vendes del mes
@Query("SELECT COUNT(v) FROM Venda v WHERE v.dataVenda >= :des")
long countVendesByMes(@Param("des") LocalDateTime des);
}