package com.imperiumfitness.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imperiumfitness.model.entity.Usuari;

@Repository
public interface UsuariRepository extends JpaRepository<Usuari, Long> {
    Optional<Usuari> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByDataRegistreAfter(LocalDateTime data);

   // Fragment suggerit per assistent IA - revisar i adaptar
// Usuaris nous per mes (últims 6 mesos)
@Query("SELECT FUNCTION('YEAR', u.dataRegistre), FUNCTION('MONTH', u.dataRegistre), COUNT(u) " +
       "FROM Usuari u WHERE u.dataRegistre >= :des " +
       "GROUP BY FUNCTION('YEAR', u.dataRegistre), FUNCTION('MONTH', u.dataRegistre) " +
       "ORDER BY FUNCTION('YEAR', u.dataRegistre), FUNCTION('MONTH', u.dataRegistre)")
List<Object[]> findUsuarisMensuals(@Param("des") LocalDateTime des);

// Distribució de tarifes
@Query("SELECT COALESCE(t.nom, 'Sense tarifa'), COUNT(u) " +
       "FROM Usuari u LEFT JOIN u.tarifa t GROUP BY t.nom")
List<Object[]> findDistribucioTarifes(); 
    
}
