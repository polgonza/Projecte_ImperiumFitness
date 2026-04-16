package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
    // Troba totes les classes d'un gimnas concret
    List<Classe> findByGimnasId(Long gimnasId);
}