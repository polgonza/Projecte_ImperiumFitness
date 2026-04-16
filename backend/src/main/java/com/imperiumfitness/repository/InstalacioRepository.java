package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Instalacio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InstalacioRepository extends JpaRepository<Instalacio, Long> {
    List<Instalacio> findByGimnasId(Long gimnasId);
}