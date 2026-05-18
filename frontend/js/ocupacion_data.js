/* =====================================================
   ocupacion_data.js — Dataset estadístic simulat
   IMPERIUM FITNESS

   Dades d'ocupació horària per gimnàs i dia.
   Cada gimnàs té un perfil de demanda diferent i realista.

   Hores: 06:00 a 22:00 → 17 punts per dia
   Índex 0 = 06:00 | Índex 16 = 22:00
   Valors: percentatge d'ocupació (0–100)
   ===================================================== */

// Fragment suggerit per assistent IA - revisar i adaptar
const OCUPACION_STATS = {

  /* ── Gym 1 — Imperium Centre ──────────────────────────
     Zona d'oficines al centre. Molt alta demanda dilluns i dimecres.
     Divendres baixa perquè la gent marxa aviat.
     Dissabte fluix — la gent prefereix altres gimnasos.          */
  gym1: {
    horario: {
      lunes:     [52,68,75,62,48,51,57,66,74,83,91,98,99,97,92,81,59],
      martes:    [40,55,63,50,37,40,46,54,61,70,78,85,90,88,83,72,50],
      miercoles: [48,64,72,59,45,48,54,63,70,79,88,95,98,96,91,80,58],
      jueves:    [38,52,60,47,34,37,43,51,58,66,74,81,86,84,79,69,47],
      viernes:   [35,48,55,43,31,34,39,47,53,60,67,73,76,71,62,51,36],
      sabado:    [28,38,48,52,50,46,41,37,33,30,28,26,25,24,22,20,18]
    }
  },

  /* ── Gym 2 — Imperium Nord ────────────────────────────
     Barri d'oficines. Pics marcats dimarts i dijous.
     Dissabte molt baix — treballadors no van el cap de setmana.   */
  gym2: {
    horario: {
      lunes:     [35,48,58,46,33,36,41,49,57,66,74,81,86,84,79,69,48],
      martes:    [44,59,68,55,42,45,51,60,68,77,86,93,97,95,90,79,57],
      miercoles: [38,51,61,49,36,39,44,52,60,69,77,84,89,87,82,72,50],
      jueves:    [46,62,71,58,44,47,53,62,70,79,88,95,98,96,91,80,58],
      viernes:   [32,44,53,41,29,32,37,45,52,59,66,72,75,70,62,52,36],
      sabado:    [18,26,34,38,37,34,30,27,24,22,20,19,18,17,16,15,13]
    }
  },

  /* ── Gym 3 — Imperium Sud ─────────────────────────────
     Barri residencial. Dissabte és el dia FORT.
     Entre setmana demanda moderada i homogènia.                   */
  gym3: {
    horario: {
      lunes:     [30,42,52,41,29,32,37,44,51,60,68,75,80,78,73,63,43],
      martes:    [28,40,50,39,28,31,36,43,50,58,66,73,78,76,71,61,41],
      miercoles: [32,44,54,43,31,34,39,46,53,62,70,77,82,80,75,65,45],
      jueves:    [29,41,51,40,29,32,37,44,51,59,67,74,79,77,72,62,42],
      viernes:   [34,47,57,46,34,37,42,50,57,66,74,81,85,82,76,66,46],
      sabado:    [52,68,79,84,82,77,71,65,59,54,51,49,47,45,43,41,38]
    }
  },

  /* ── Gym 4 — Imperium Est ─────────────────────────────
     Zona universitària. Molt homogeni tota la setmana.
     Pic a primera hora i a última. Baixa a mig matí.              */
  gym4: {
    horario: {
      lunes:     [58,65,55,42,35,38,44,54,63,68,65,71,78,82,79,72,55],
      martes:    [55,62,52,40,33,36,42,52,60,65,62,68,75,79,76,69,52],
      miercoles: [57,64,54,41,34,37,43,53,62,67,64,70,77,81,78,71,54],
      jueves:    [54,61,51,39,33,36,42,51,59,64,61,67,74,78,75,68,51],
      viernes:   [50,57,47,36,30,33,38,47,55,60,57,63,70,73,69,62,46],
      sabado:    [40,52,62,68,65,60,54,49,44,40,38,36,35,34,32,30,28]
    }
  },

  /* ── Gym 5 — Imperium Oest ────────────────────────────
     Gimnàs de barri. Dijous i divendres forts (gent es prepara).
     Dimarts i dimecres més baixos.
     Dissabte matí fort, tarda buida.                              */
  gym5: {
    horario: {
      lunes:     [22,32,42,33,22,24,28,35,42,50,58,64,68,66,61,52,35],
      martes:    [16,24,32,25,17,19,23,29,35,42,49,54,58,56,52,44,29],
      miercoles: [18,27,36,28,19,21,25,31,38,45,52,58,62,60,55,47,31],
      jueves:    [28,40,51,41,30,33,38,46,54,63,71,78,82,80,75,65,44],
      viernes:   [32,45,56,45,33,36,42,51,59,68,76,83,87,84,78,67,46],
      sabado:    [42,58,70,76,74,69,62,55,48,42,38,35,32,30,28,26,23]
    }
  }
};

/* Etiquetes d'hora per a l'eix X de les gràfiques */
const HORAS_LABELS = [
  "06:00","07:00","08:00","09:00","10:00","11:00","12:00",
  "13:00","14:00","15:00","16:00","17:00","18:00","19:00",
  "20:00","21:00","22:00"
];

/* Dies de la setmana — mateix ordre que Date.getDay() */
const DIAS_SEMANA = ["domingo","lunes","martes","miercoles","jueves","viernes","sabado"];

/*
  Retorna l'array de 17 valors del dia actual per a un gimnàs.
  gymKey: "gym1" … "gym5"
*/
function getOcupacionHoy(gymKey) {
  const hoy = DIAS_SEMANA[new Date().getDay()];
  return OCUPACION_STATS[gymKey]?.horario[hoy] || new Array(17).fill(0);
}

/*
  Retorna el percentatge d'ocupació a l'hora actual.
  Si és abans de les 06:00 o després de les 22:00 retorna 0.
*/
function getOcupacionAhora(gymKey) {
  const h   = new Date().getHours();
  const idx = h - 6;
  if (idx < 0 || idx > 16) return 0;
  return getOcupacionHoy(gymKey)[idx];
}