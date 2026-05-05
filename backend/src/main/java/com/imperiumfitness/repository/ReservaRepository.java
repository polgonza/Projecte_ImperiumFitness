package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuariId(Long usuariId);
    List<Reserva> findByClasseId(Long classeId);

    // Comprova duplicat independentment de la data (conservem per compatibilitat)
    boolean existsByUsuariIdAndClasseId(Long usuariId, Long classeId);

    // Compta reserves futures
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.usuari.id = :usuariId " +
           "AND r.dataReserva >= :ara")
    long countReservesFuturesByUsuari(
        @Param("usuariId") Long usuariId,
        @Param("ara") java.time.LocalDateTime ara
    );

    // ✅ Comprova duplicat NOMÉS per reserves futures
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.usuari.id = :usuariId " +
           "AND r.classe.id = :classeId " +
           "AND r.dataReserva >= :ara")
    boolean existsByUsuariIdAndClasseIdAndFutura(
        @Param("usuariId") Long usuariId,
        @Param("classeId") Long classeId,
        @Param("ara") java.time.LocalDateTime ara
    );
}
