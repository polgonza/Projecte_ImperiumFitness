package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Pagament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagamentRepository extends JpaRepository<Pagament, Long> {
    // Tots els pagaments d'un usuari
    List<Pagament> findByUsuariId(Long usuariId);
}