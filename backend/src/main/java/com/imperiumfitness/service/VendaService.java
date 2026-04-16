package com.imperiumfitness.service;

import com.imperiumfitness.dto.VendaDTO;
import com.imperiumfitness.model.entity.Producte;
import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.model.entity.Venda;
import com.imperiumfitness.repository.ProducteRepository;
import com.imperiumfitness.repository.UsuariRepository;
import com.imperiumfitness.repository.VendaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository repo;
    private final UsuariRepository usuariRepo;
    private final ProducteRepository producteRepo;

    public VendaService(VendaRepository repo,
                        UsuariRepository usuariRepo,
                        ProducteRepository producteRepo) {
        this.repo = repo;
        this.usuariRepo = usuariRepo;
        this.producteRepo = producteRepo;
    }

    public List<VendaDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<VendaDTO> getByUsuari(Long usuariId) {
        return repo.findByUsuariId(usuariId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public VendaDTO save(VendaDTO dto) {
        // Regla de negoci: comprovem que hi ha estoc suficient
        Producte p = producteRepo.findById(dto.getProducteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producte no trobat"));

        if (p.getEstoc() < dto.getQuantitat()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Estoc insuficient");
        }

        // Descomptem l'estoc
        p.setEstoc(p.getEstoc() - dto.getQuantitat());
        producteRepo.save(p);

        Venda v = toEntity(dto, p);
        v.setDataVenda(LocalDateTime.now());
        return toDTO(repo.save(v));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda no trobada");
        repo.deleteById(id);
    }

    private VendaDTO toDTO(Venda v) {
        return new VendaDTO(
                v.getId(),
                v.getProducte() != null ? v.getProducte().getId() : null,
                v.getUsuari() != null ? v.getUsuari().getId() : null,
                v.getQuantitat(),
                v.getDataVenda()
        );
    }

    private Venda toEntity(VendaDTO dto, Producte producte) {
        Venda v = new Venda();
        Usuari u = usuariRepo.findById(dto.getUsuariId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuari no trobat"));
        v.setUsuari(u);
        v.setProducte(producte);
        v.setQuantitat(dto.getQuantitat());
        return v;
    }
}