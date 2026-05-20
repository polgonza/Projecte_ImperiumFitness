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

    List<Venda> findByUsuariId(Long usuariId);

    List<Venda> findByProducteId(Long producteId);

@Query("SELECT p.nom, SUM(v.quantitat) as total FROM Venda v " +
       "JOIN v.producte p WHERE v.dataVenda >= :des " +
       "GROUP BY p.nom ORDER BY total DESC")
List<Object[]> findTopProductesByMes(@Param("des") LocalDateTime des);

@Query("SELECT COUNT(v) FROM Venda v WHERE v.dataVenda >= :des")
long countVendesByMes(@Param("des") LocalDateTime des);

@Query("SELECT FUNCTION('YEAR', v.dataVenda), FUNCTION('MONTH', v.dataVenda), " +
       "COUNT(v), SUM(v.quantitat * v.producte.preu) " +
       "FROM Venda v WHERE v.dataVenda >= :des " +
       "GROUP BY FUNCTION('YEAR', v.dataVenda), FUNCTION('MONTH', v.dataVenda) " +
       "ORDER BY FUNCTION('YEAR', v.dataVenda), FUNCTION('MONTH', v.dataVenda)")
List<Object[]> findVendesMensuals(@Param("des") LocalDateTime des);

@Query("SELECT v FROM Venda v ORDER BY v.dataVenda DESC")
List<Venda> findUltimesVendes(org.springframework.data.domain.Pageable pageable);
}