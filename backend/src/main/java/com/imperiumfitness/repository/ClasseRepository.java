package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    List<Classe> findByGimnasId(Long gimnasId);
    List<Classe> findByHorariAfterOrderByHorariAsc(LocalDateTime horari);
}