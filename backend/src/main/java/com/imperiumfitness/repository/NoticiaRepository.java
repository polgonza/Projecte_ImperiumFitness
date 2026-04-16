package com.imperiumfitness.repository;

import com.imperiumfitness.model.entity.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long> {
    // Notícies ordenades de més nova a més antiga
    List<Noticia> findAllByOrderByDataPublicacioDesc();
}