package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuariId(Long usuariId);
    List<Reserva> findByClasseId(Long classeId);

    boolean existsByUsuariIdAndClasseId(Long usuariId, Long classeId);

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.usuari.id = :usuariId " +
           "AND r.dataReserva >= :ara")
    long countReservesFuturesByUsuari(
        @Param("usuariId") Long usuariId,
        @Param("ara") java.time.LocalDateTime ara
    );

    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.usuari.id = :usuariId " +
           "AND r.classe.id = :classeId " +
           "AND r.dataReserva >= :ara")
    boolean existsByUsuariIdAndClasseIdAndFutura(
        @Param("usuariId") Long usuariId,
        @Param("classeId") Long classeId,
        @Param("ara") java.time.LocalDateTime ara
    );

@Query("SELECT COUNT(r) FROM Reserva r WHERE r.classe.horari >= :iniciDia")
long countReservesAmbClasseFutura(@Param("iniciDia") LocalDateTime iniciDia);

@Query("SELECT c.nom, COUNT(r) as total FROM Reserva r " +
       "JOIN r.classe c GROUP BY c.nom ORDER BY total DESC")
List<Object[]> findTopClasses();

@Query("SELECT r FROM Reserva r ORDER BY r.dataReserva DESC")
List<Reserva> findUltimesReserves(org.springframework.data.domain.Pageable pageable);

@Transactional
void deleteByClasseId(Long classeId);

}
