package com.imperiumfitness.service;

import com.imperiumfitness.dto.VendaDTO;
import com.imperiumfitness.model.entity.Producte;
import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.model.entity.Venda;
import com.imperiumfitness.repository.ProducteRepository;
import com.imperiumfitness.repository.UsuariRepository;
import com.imperiumfitness.repository.VendaRepository;
import com.imperiumfitness.service.VendaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock private VendaRepository    vendaRepo;
    @Mock private UsuariRepository   usuariRepo;
    @Mock private ProducteRepository producteRepo;

    @InjectMocks
    private VendaService vendaService;

    private Producte producte;
    private Usuari   usuari;

    @BeforeEach
    void setUp() {
        producte = new Producte();
        producte.setId(1L);
        producte.setNom("Proteïna Whey");
        producte.setEstoc(10);

        usuari = new Usuari();
        usuari.setId(1L);
        usuari.setNom("Test");
    }

    // Test 1: Compra amb estoc suficient → decrementa estoc i guarda venda
    @Test
    void save_estocSuficient_decrementaEstoc() {
        VendaDTO dto = new VendaDTO();
        dto.setProducteId(1L);
        dto.setUsuariId(1L);
        dto.setQuantitat(3);

        when(producteRepo.findById(1L)).thenReturn(Optional.of(producte));
        when(usuariRepo.findById(1L)).thenReturn(Optional.of(usuari));
        when(vendaRepo.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> vendaService.save(dto));

        // Comprova que l'estoc s'ha decrementat de 10 a 7
        assertEquals(7, producte.getEstoc());
        verify(producteRepo).save(producte);
    }

    // Test 2: Compra amb estoc insuficient → 409
    @Test
    void save_estocInsuficient_llaçaConflict() {
        producte.setEstoc(2);

        VendaDTO dto = new VendaDTO();
        dto.setProducteId(1L);
        dto.setUsuariId(1L);
        dto.setQuantitat(5); // vol 5, només hi ha 2

        when(producteRepo.findById(1L)).thenReturn(Optional.of(producte));

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> vendaService.save(dto)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(vendaRepo, never()).save(any());
        verify(producteRepo, never()).save(any()); // l'estoc no s'ha de modificar
    }

    // Test 3: Producte inexistent → 404
    @Test
    void save_producteInexistent_llaça404() {
        VendaDTO dto = new VendaDTO();
        dto.setProducteId(99L);
        dto.setUsuariId(1L);
        dto.setQuantitat(1);

        when(producteRepo.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> vendaService.save(dto)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}