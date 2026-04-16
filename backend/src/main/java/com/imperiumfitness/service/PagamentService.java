package com.imperiumfitness.service;

import com.imperiumfitness.dto.PagamentDTO;
import com.imperiumfitness.model.entity.Pagament;
import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.repository.PagamentRepository;
import com.imperiumfitness.repository.UsuariRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagamentService {

    private final PagamentRepository repo;
    private final UsuariRepository usuariRepo;

    public PagamentService(PagamentRepository repo, UsuariRepository usuariRepo) {
        this.repo = repo;
        this.usuariRepo = usuariRepo;
    }

    public List<PagamentDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PagamentDTO> getByUsuari(Long usuariId) {
        return repo.findByUsuariId(usuariId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PagamentDTO getById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pagament no trobat")));
    }

    public PagamentDTO save(PagamentDTO dto) {
        Pagament p = toEntity(dto);
        p.setDataPagament(LocalDateTime.now());
        return toDTO(repo.save(p));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagament no trobat");
        repo.deleteById(id);
    }

    private PagamentDTO toDTO(Pagament p) {
        return new PagamentDTO(
                p.getId(),
                p.getUsuari() != null ? p.getUsuari().getId() : null,
                p.getImportTotal(), p.getDataPagament(), p.getMetodePagament()
        );
    }

    private Pagament toEntity(PagamentDTO dto) {
        Pagament p = new Pagament();
        Usuari u = usuariRepo.findById(dto.getUsuariId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuari no trobat"));
        p.setUsuari(u);
        p.setImportTotal(dto.getImportTotal());
        p.setMetodePagament(dto.getMetodePagament());
        return p;
    }
}