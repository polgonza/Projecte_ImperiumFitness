package com.imperiumfitness.service;

import com.imperiumfitness.dto.ReservaDTO;
import com.imperiumfitness.model.entity.Classe;
import com.imperiumfitness.model.entity.Reserva;
import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.model.entity.Tarifa;
import com.imperiumfitness.repository.ClasseRepository;
import com.imperiumfitness.repository.ReservaRepository;
import com.imperiumfitness.repository.UsuariRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock private ReservaRepository reservaRepo;
    @Mock private UsuariRepository  usuariRepo;
    @Mock private ClasseRepository  classeRepo;

    @InjectMocks
    private ReservaService reservaService;

    private Usuari usuariActiu;
    private Classe classeFutura;

    @BeforeEach
    void setUp() {
        // Tarifa necessaria perque isSubscripcioActiva() comprova tarifa != null
        Tarifa tarifa = new Tarifa();
        tarifa.setId(1L);

        usuariActiu = new Usuari();
        usuariActiu.setId(1L);
        usuariActiu.setTarifa(tarifa);
        usuariActiu.setTarifaDataFi(LocalDateTime.now().plusMonths(1));
        usuariActiu.setTarifaCancellada(false);

        classeFutura = new Classe();
        classeFutura.setId(10L);
        classeFutura.setCapacitat(20);
        classeFutura.setHorari(LocalDateTime.now().plusDays(1));
    }

    // Test 1: Usuari sense subscripcio activa no pot reservar - 403
    @Test
    void save_senseSuscripcio_llaçaForbidden() {
        Usuari usuariSenseTarifa = new Usuari();
        usuariSenseTarifa.setId(2L);
        usuariSenseTarifa.setTarifaDataFi(null); // sense tarifa -> isSubscripcioActiva() = false
        usuariSenseTarifa.setTarifaCancellada(false);

        ReservaDTO dto = new ReservaDTO();
        dto.setUsuariId(2L);
        dto.setClasseId(10L);

        when(usuariRepo.findById(2L)).thenReturn(Optional.of(usuariSenseTarifa));

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> reservaService.save(dto)
        );
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    // Test 2: Usuari amb 5 reserves actives no pot fer-ne mes - 409
    @Test
    void save_limitReservesAssolit_llaçaConflict() {
        ReservaDTO dto = new ReservaDTO();
        dto.setUsuariId(1L);
        dto.setClasseId(10L);

        when(usuariRepo.findById(1L)).thenReturn(Optional.of(usuariActiu));
        when(reservaRepo.countReservesFuturesByUsuari(eq(1L), any(LocalDateTime.class)))
            .thenReturn(5L);

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> reservaService.save(dto)
        );
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    // Test 3: Classe plena no permet reservar - 409
    @Test
    void save_classePlena_llaçaConflict() {
        classeFutura.setCapacitat(1);

        ReservaDTO dto = new ReservaDTO();
        dto.setUsuariId(1L);
        dto.setClasseId(10L);

        when(usuariRepo.findById(1L)).thenReturn(Optional.of(usuariActiu));
        when(reservaRepo.countReservesFuturesByUsuari(eq(1L), any())).thenReturn(0L);
        when(reservaRepo.existsByUsuariIdAndClasseIdAndFutura(eq(1L), eq(10L), any())).thenReturn(false);
        when(classeRepo.findById(10L)).thenReturn(Optional.of(classeFutura));
        when(reservaRepo.findByClasseId(10L)).thenReturn(List.of(new Reserva()));

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> reservaService.save(dto)
        );
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    // Test 4: Reserva de classe passada no es permet - 409
    @Test
    void save_classePassada_llaçaConflict() {
        classeFutura.setHorari(LocalDateTime.now().minusDays(1));

        ReservaDTO dto = new ReservaDTO();
        dto.setUsuariId(1L);
        dto.setClasseId(10L);

        when(usuariRepo.findById(1L)).thenReturn(Optional.of(usuariActiu));
        when(reservaRepo.countReservesFuturesByUsuari(eq(1L), any())).thenReturn(0L);
        when(reservaRepo.existsByUsuariIdAndClasseIdAndFutura(eq(1L), eq(10L), any())).thenReturn(false);
        when(classeRepo.findById(10L)).thenReturn(Optional.of(classeFutura));

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> reservaService.save(dto)
        );
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }
}