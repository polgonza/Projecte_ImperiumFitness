package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Totes les reserves d'un usuari
    List<Reserva> findByUsuariId(Long usuariId);
    // Totes les reserves d'una classe
    List<Reserva> findByClasseId(Long classeId);
    // Comprovar si un usuari ja té reserva a una classe
    boolean existsByUsuariIdAndClasseId(Long usuariId, Long classeId);
    // Compta les reserves futures d'un usuari
@Query("SELECT COUNT(r) FROM Reserva r WHERE r.usuari.id = :usuariId " +
       "AND r.dataReserva >= :ara")
long countReservesFuturesByUsuari(
    @Param("usuariId") Long usuariId,
    @Param("ara") java.time.LocalDateTime ara
 );
}
