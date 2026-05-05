package com.imperiumfitness.controller;

import com.imperiumfitness.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@PreAuthorize("hasRole('ADMIN')")  // només ADMIN pot veure estadístiques
public class StatsController {

    private final UsuariRepository usuariRepo;
    private final ReservaRepository reservaRepo;
    private final ClasseRepository classeRepo;
    private final VendaRepository vendaRepo;
    private final ProducteRepository producteRepo;

    public StatsController(UsuariRepository usuariRepo,
                           ReservaRepository reservaRepo,
                           ClasseRepository classeRepo,
                           VendaRepository vendaRepo,
                           ProducteRepository producteRepo) {
        this.usuariRepo  = usuariRepo;
        this.reservaRepo = reservaRepo;
        this.classeRepo  = classeRepo;
        this.vendaRepo   = vendaRepo;
        this.producteRepo = producteRepo;
    }

    /* ── Resum general ───────────────────────────────── */
    @GetMapping("/resum")
    public ResponseEntity<Map<String, Object>> getResum() {
        Map<String, Object> stats = new HashMap<>();

        // Total usuaris
        stats.put("totalUsuaris", usuariRepo.count());

        // Usuaris nous aquest mes
        LocalDateTime iniciMes = LocalDateTime.now()
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        stats.put("usuarisNousMes", usuariRepo.countByDataRegistreAfter(iniciMes));

        // Total reserves actives (futures)
        stats.put("totalReservesActives",
                reservaRepo.countReservesFuturesByUsuari(0L, LocalDateTime.now()) +
                reservaRepo.countAllFuturesReserves(LocalDateTime.now()));

        // Classe més reservada
        List<Object[]> classesTop = reservaRepo.findTopClasses();
        if (!classesTop.isEmpty()) {
            Object[] top = classesTop.get(0);
            stats.put("classeMesReservada", top[0]);
            stats.put("classeMesReservadaCount", top[1]);
        }

        return ResponseEntity.ok(stats);
    }

    /* ── Estadístiques de productes ──────────────────── */
    @GetMapping("/productes")
    public ResponseEntity<Map<String, Object>> getProductes() {
        Map<String, Object> stats = new HashMap<>();

        // Producte més venut del mes
        LocalDateTime iniciMes = LocalDateTime.now()
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        List<Object[]> topMes = vendaRepo.findTopProductesByMes(iniciMes);
        if (!topMes.isEmpty()) {
            stats.put("topProducteMes", topMes.get(0)[0]);
            stats.put("topProducteMesUnitats", topMes.get(0)[1]);
        }

        // Producte més venut de l'any
        LocalDateTime iniciAny = LocalDateTime.now()
                .withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        List<Object[]> topAny = vendaRepo.findTopProductesByMes(iniciAny);
        if (!topAny.isEmpty()) {
            stats.put("topProducteAny", topAny.get(0)[0]);
            stats.put("topProducteAnyUnitats", topAny.get(0)[1]);
        }

        // Producte amb menys estoc
        producteRepo.findTopByOrderByEstocAsc().ifPresent(p -> {
            stats.put("menysEstocNom", p.getNom());
            stats.put("menysEstocUnitats", p.getEstoc());
        });

        // Total vendes del mes
        stats.put("totalVendesMes", vendaRepo.countVendesByMes(iniciMes));

        return ResponseEntity.ok(stats);
    }
}