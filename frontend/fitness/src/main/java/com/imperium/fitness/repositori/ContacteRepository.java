package com.imperium.fitness.repositori;

import com.imperium.fitness.entitat.Contacte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContacteRepository extends JpaRepository<Contacte, Long> {
    
    List<Contacte> findAllByOrderByDataEnviamentDesc();
}