package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Gimnas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GimnasRepository extends JpaRepository<Gimnas, Long> {
}