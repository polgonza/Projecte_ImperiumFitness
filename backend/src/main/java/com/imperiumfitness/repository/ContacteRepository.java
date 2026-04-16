package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Contacte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContacteRepository extends JpaRepository<Contacte, Long> {
}