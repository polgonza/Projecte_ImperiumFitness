package com.imperiumfitness.service;

import com.imperiumfitness.dto.ProducteDTO;
import com.imperiumfitness.model.entity.Producte;
import com.imperiumfitness.repository.ProducteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProducteService {

    private final ProducteRepository repo;

    public ProducteService(ProducteRepository repo) {
        this.repo = repo;
    }

    public List<ProducteDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProducteDTO getById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producte no trobat")));
    }

    public ProducteDTO save(ProducteDTO dto) {
        return toDTO(repo.save(toEntity(dto)));
    }

    public ProducteDTO update(Long id, ProducteDTO dto) {
        Producte p = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producte no trobat"));
        p.setNom(dto.getNom());
        p.setDescripcio(dto.getDescripcio());
        p.setPreu(dto.getPreu());
        p.setCategoria(dto.getCategoria());
        p.setEstoc(dto.getEstoc());
        return toDTO(repo.save(p));
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producte no trobat");
        repo.deleteById(id);
    }

    private ProducteDTO toDTO(Producte p) {
        return new ProducteDTO(p.getId(), p.getNom(), p.getDescripcio(),
                p.getPreu(), p.getCategoria(), p.getEstoc());
    }

    private Producte toEntity(ProducteDTO dto) {
        Producte p = new Producte();
        p.setNom(dto.getNom());
        p.setDescripcio(dto.getDescripcio());
        p.setPreu(dto.getPreu());
        p.setCategoria(dto.getCategoria());
        p.setEstoc(dto.getEstoc() != null ? dto.getEstoc() : 0);
        return p;
    }
}