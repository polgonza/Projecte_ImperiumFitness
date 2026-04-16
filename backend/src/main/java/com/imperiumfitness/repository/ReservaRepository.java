package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
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
}