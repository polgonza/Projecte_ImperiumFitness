package com.imperiumfitness.service;

import com.imperiumfitness.dto.NoticiaDTO;
import com.imperiumfitness.model.entity.Noticia;
import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.repository.NoticiaRepository;
import com.imperiumfitness.repository.UsuariRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticiaService {

    private final NoticiaRepository repo;
    private final UsuariRepository usuariRepo;

    public NoticiaService(NoticiaRepository repo, UsuariRepository usuariRepo) {
        this.repo = repo;
        this.usuariRepo = usuariRepo;
    }

    public List<NoticiaDTO> getAll() {
        // Retornem ordenades de més nova a més antiga
        return repo.findAllByOrderByDataPublicacioDesc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public NoticiaDTO getById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notícia no trobada")));
    }

    public NoticiaDTO save(NoticiaDTO dto) {
        Noticia n = toEntity(dto);
        n.setDataPublicacio(LocalDateTime.now());
        return toDTO(repo.save(n));
    }

    public NoticiaDTO update(Long id, NoticiaDTO dto) {
        Noticia n = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notícia no trobada"));
        n.setTitol(dto.getTitol());
        n.setContingut(dto.getContingut());
        return toDTO(repo.save(n));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notícia no trobada");
        repo.deleteById(id);
    }

    private NoticiaDTO toDTO(Noticia n) {
        return new NoticiaDTO(
                n.getId(), n.getTitol(), n.getContingut(),
                n.getDataPublicacio(),
                n.getAutor() != null ? n.getAutor().getId() : null
        );
    }

    private Noticia toEntity(NoticiaDTO dto) {
        Noticia n = new Noticia();
        n.setTitol(dto.getTitol());
        n.setContingut(dto.getContingut());
        if (dto.getAutorId() != null) {
            Usuari autor = usuariRepo.findById(dto.getAutorId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Autor no trobat"));
            n.setAutor(autor);
        }
        return n;
    }
}