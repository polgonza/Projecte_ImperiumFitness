// Fragment suggerit per assistent IA - revisar i adaptar
package com.imperiumfitness.controller;

import com.imperiumfitness.model.entity.Reserva;
import com.imperiumfitness.model.entity.Venda;
import com.imperiumfitness.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@RestController
@RequestMapping("/api/stats")
@PreAuthorize("hasRole('ADMIN')")
public class StatsController {

    private final UsuariRepository   usuariRepo;
    private final ReservaRepository  reservaRepo;
    private final ClasseRepository   classeRepo;
    private final VendaRepository    vendaRepo;
    private final ProducteRepository producteRepo;

    public StatsController(UsuariRepository usuariRepo, ReservaRepository reservaRepo,
                           ClasseRepository classeRepo, VendaRepository vendaRepo,
                           ProducteRepository producteRepo) {
        this.usuariRepo   = usuariRepo;
        this.reservaRepo  = reservaRepo;
        this.classeRepo   = classeRepo;
        this.vendaRepo    = vendaRepo;
        this.producteRepo = producteRepo;
    }

    /* ── Resum general ───────────────────────────────── */
    @GetMapping("/resum")
    public ResponseEntity<Map<String, Object>> getResum() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsuaris", usuariRepo.count());

        LocalDateTime iniciMes = LocalDateTime.now()
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        stats.put("usuarisNousMes", usuariRepo.countByDataRegistreAfter(iniciMes));

        // Fix: compta reserves on la CLASSE és futura, no la data de reserva
        stats.put("totalReservesActives",
                reservaRepo.countReservesAmbClasseFutura(LocalDateTime.now()));

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

        LocalDateTime iniciMes = iniciMes();
        LocalDateTime iniciAny = LocalDateTime.now()
                .withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<Object[]> topMes = vendaRepo.findTopProductesByMes(iniciMes);
        if (!topMes.isEmpty()) {
            stats.put("topProducteMes",       topMes.get(0)[0]);
            stats.put("topProducteMesUnitats", topMes.get(0)[1]);
        }

        List<Object[]> topAny = vendaRepo.findTopProductesByMes(iniciAny);
        if (!topAny.isEmpty()) {
            stats.put("topProducteAny",       topAny.get(0)[0]);
            stats.put("topProducteAnyUnitats", topAny.get(0)[1]);
        }

        producteRepo.findTopByOrderByEstocAsc().ifPresent(p -> {
            stats.put("menysEstocNom",    p.getNom());
            stats.put("menysEstocUnitats", p.getEstoc());
        });

        stats.put("totalVendesMes", vendaRepo.countVendesByMes(iniciMes));

        return ResponseEntity.ok(stats);
    }

    /* ── Top classes per reserves ─────────────────────── */
    @GetMapping("/classes")
    public ResponseEntity<List<Map<String, Object>>> getClasses() {
        List<Object[]> top = reservaRepo.findTopClasses();
        List<Map<String, Object>> resultat = new ArrayList<>();

        for (Object[] fila : top) {
            String nom     = (String) fila[0];
            long   reserves = ((Number) fila[1]).longValue();
            long   capacitat = classeRepo.findAll().stream()
                .filter(c -> nom.equals(c.getNom()))
                .mapToLong(c -> c.getCapacitat() != null ? c.getCapacitat() : 1)
                .findFirst().orElse(1);

            Map<String, Object> item = new HashMap<>();
            item.put("nom",      nom);
            item.put("reserves", reserves);
            item.put("capacitat", capacitat);
            item.put("pct",      Math.min(100, Math.round(reserves * 100.0 / capacitat)));
            resultat.add(item);
        }

        return ResponseEntity.ok(resultat);
    }

    /* ── Vendes mensuals (últims 6 mesos) ────────────── */
    @GetMapping("/vendes-mensuals")
    public ResponseEntity<List<Map<String, Object>>> getVendesMensuals() {
        LocalDateTime des = LocalDateTime.now().minusMonths(6)
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<Object[]> dades = vendaRepo.findVendesMensuals(des);
        List<Map<String, Object>> resultat = new ArrayList<>();

        for (Object[] fila : dades) {
            int any   = ((Number) fila[0]).intValue();
            int mes   = ((Number) fila[1]).intValue();
            long count = ((Number) fila[2]).longValue();
            double ingressos = fila[3] != null ? ((Number) fila[3]).doubleValue() : 0.0;

            String nomMes = java.time.Month.of(mes)
                    .getDisplayName(TextStyle.SHORT, new Locale("ca"));

            Map<String, Object> item = new HashMap<>();
            item.put("any",       any);
            item.put("mes",       mes);
            item.put("nomMes",    nomMes);
            item.put("vendes",    count);
            item.put("ingressos", Math.round(ingressos * 100.0) / 100.0);
            resultat.add(item);
        }

        return ResponseEntity.ok(resultat);
    }

    /* ── Nous usuaris per mes (últims 6 mesos) ───────── */
    @GetMapping("/usuaris-mensuals")
    public ResponseEntity<List<Map<String, Object>>> getUsuarisMensuals() {
        LocalDateTime des = LocalDateTime.now().minusMonths(6)
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<Object[]> dades = usuariRepo.findUsuarisMensuals(des);
        List<Map<String, Object>> resultat = new ArrayList<>();

        for (Object[] fila : dades) {
            int any   = ((Number) fila[0]).intValue();
            int mes   = ((Number) fila[1]).intValue();
            long count = ((Number) fila[2]).longValue();

            String nomMes = java.time.Month.of(mes)
                    .getDisplayName(TextStyle.SHORT, new Locale("ca"));

            Map<String, Object> item = new HashMap<>();
            item.put("any",    any);
            item.put("mes",    mes);
            item.put("nomMes", nomMes);
            item.put("nous",   count);
            resultat.add(item);
        }

        return ResponseEntity.ok(resultat);
    }

    /* ── Distribució de tarifes ──────────────────────── */
    @GetMapping("/distribucio-tarifes")
    public ResponseEntity<List<Map<String, Object>>> getDistribucioTarifes() {
        List<Object[]> dades = usuariRepo.findDistribucioTarifes();
        List<Map<String, Object>> resultat = new ArrayList<>();

        for (Object[] fila : dades) {
            Map<String, Object> item = new HashMap<>();
            item.put("tarifa", fila[0] != null ? fila[0] : "Sense tarifa");
            item.put("count",  ((Number) fila[1]).longValue());
            resultat.add(item);
        }

        return ResponseEntity.ok(resultat);
    }

    /* ── Estoc baix (≤ 5 unitats) ────────────────────── */
    @GetMapping("/estoc-baix")
    public ResponseEntity<List<Map<String, Object>>> getEstocBaix() {
        var productes = producteRepo.findByEstocLessThanEqualOrderByEstocAsc(5);
        List<Map<String, Object>> resultat = new ArrayList<>();

        for (var p : productes) {
            Map<String, Object> item = new HashMap<>();
            item.put("id",        p.getId());
            item.put("nom",       p.getNom());
            item.put("estoc",     p.getEstoc());
            item.put("categoria", p.getCategoria());
            resultat.add(item);
        }

        return ResponseEntity.ok(resultat);
    }

    /* ── Activitat recent ────────────────────────────── */
    @GetMapping("/activitat-recent")
    public ResponseEntity<Map<String, Object>> getActivitatRecent() {
        Map<String, Object> resultat = new HashMap<>();

        // Últimes 5 reserves
        List<Reserva> reserves = reservaRepo.findUltimesReserves(PageRequest.of(0, 5));
        List<Map<String, Object>> reservesDTO = new ArrayList<>();
        for (Reserva r : reserves) {
            Map<String, Object> item = new HashMap<>();
            item.put("usuari",     r.getUsuari() != null ? r.getUsuari().getNom() : "—");
            item.put("classe",     r.getClasse()  != null ? r.getClasse().getNom()  : "—");
            item.put("dataReserva", r.getDataReserva() != null ? r.getDataReserva().toString() : "—");
            reservesDTO.add(item);
        }
        resultat.put("reserves", reservesDTO);

        // Últimes 5 vendes
        List<Venda> vendes = vendaRepo.findUltimesVendes(PageRequest.of(0, 5));
        List<Map<String, Object>> vendesDTO = new ArrayList<>();
        for (Venda v : vendes) {
            Map<String, Object> item = new HashMap<>();
            item.put("usuari",    v.getUsuari()   != null ? v.getUsuari().getNom()    : "—");
            item.put("producte",  v.getProducte() != null ? v.getProducte().getNom()  : "—");
            item.put("quantitat", v.getQuantitat());
            item.put("dataVenda", v.getDataVenda() != null ? v.getDataVenda().toString() : "—");
            vendesDTO.add(item);
        }
        resultat.put("vendes", vendesDTO);

        return ResponseEntity.ok(resultat);
    }

    private LocalDateTime iniciMes() {
        return LocalDateTime.now()
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
}