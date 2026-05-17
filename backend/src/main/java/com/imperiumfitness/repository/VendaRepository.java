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

// Fragment suggerit per assistent IA - revisar i adaptar
// Vendes agrupades per mes (últims 6 mesos)
@Query("SELECT FUNCTION('YEAR', v.dataVenda), FUNCTION('MONTH', v.dataVenda), " +
       "COUNT(v), SUM(v.quantitat * v.producte.preu) " +
       "FROM Venda v WHERE v.dataVenda >= :des " +
       "GROUP BY FUNCTION('YEAR', v.dataVenda), FUNCTION('MONTH', v.dataVenda) " +
       "ORDER BY FUNCTION('YEAR', v.dataVenda), FUNCTION('MONTH', v.dataVenda)")
List<Object[]> findVendesMensuals(@Param("des") LocalDateTime des);

// Últimes N vendes (activitat recent)
@Query("SELECT v FROM Venda v ORDER BY v.dataVenda DESC")
List<Venda> findUltimesVendes(org.springframework.data.domain.Pageable pageable);
}