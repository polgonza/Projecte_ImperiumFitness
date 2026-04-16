package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {
    List<Estadistica> findByGimnasId(Long gimnasId);
    List<Estadistica> findByTipus(String tipus);
}