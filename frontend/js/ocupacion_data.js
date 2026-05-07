/* =====================================================
   ocupacion_data.js — Dataset estadístico simulado
   IMPERIUM FITNESS

   Datos de ocupación horaria por gimnasio y día.
   Simula un sistema RFID de control de accesos.

   Horas: 06:00 a 22:00 → 17 puntos por día
   Índice 0 = 06:00 | Índice 16 = 22:00
   Valores: porcentaje de ocupación (0–100)
   ===================================================== */

const OCUPACION_STATS = {

  /* Gym 1 — Imperium Centro  (muy alta demanda, pico 95-100%) */
  gym1: {
    horario: {
      lunes:     [48,63,71,58,44,47,52,61,69,78,87,95,99,98,94,83,61],
      martes:    [45,61,68,55,42,46,50,59,66,75,84,92,97,96,91,80,58],
      miercoles: [44,59,67,54,41,45,49,58,65,73,82,90,95,94,89,78,56],
      jueves:    [43,57,65,52,40,44,48,56,63,71,79,87,92,91,86,75,53],
      viernes:   [42,55,62,50,40,43,47,54,60,68,75,82,86,83,76,65,47],
      sabado:    [41,58,70,75,72,68,62,57,51,46,43,41,40,38,36,34,32]
    }
  },

  /* Gym 2 — Imperium Norte  (alta demanda, pico 85-93%) */
  gym2: {
    horario: {
      lunes:     [42,56,65,52,38,41,47,55,63,72,81,89,93,91,87,76,54],
      martes:    [40,54,63,50,37,40,45,53,61,70,78,86,91,89,84,73,51],
      miercoles: [39,52,61,48,36,39,44,52,59,68,76,84,89,87,82,71,49],
      jueves:    [38,50,59,46,35,38,43,50,57,65,73,81,86,84,79,69,47],
      viernes:   [37,48,57,44,34,37,42,49,55,62,69,76,80,77,71,61,43],
      sabado:    [38,54,67,71,69,64,58,53,47,43,40,38,36,35,33,31,29]
    }
  },

  /* Gym 3 — Imperium Sur  (demanda estable, pico 88-94%) */
  gym3: {
    horario: {
      lunes:     [40,53,62,55,44,47,52,58,65,74,82,88,94,92,88,77,55],
      martes:    [38,51,60,53,42,45,50,56,63,72,80,86,92,90,85,74,52],
      miercoles: [37,50,59,52,41,44,49,55,62,70,78,84,90,88,83,72,50],
      jueves:    [36,48,57,50,40,43,48,53,60,68,76,82,87,85,80,70,48],
      viernes:   [35,47,55,48,39,42,47,52,58,65,72,78,82,79,73,63,44],
      sabado:    [36,51,64,70,68,63,57,52,47,43,41,39,37,36,34,32,30]
    }
  },

  /* Gym 4 — Imperium Este  (media-alta, pico 75-87%) */
  gym4: {
    horario: {
      lunes:     [33,46,56,44,31,34,39,47,55,64,73,81,87,85,80,70,49],
      martes:    [31,44,54,42,30,33,38,45,53,62,70,78,84,82,77,67,46],
      miercoles: [30,42,52,40,29,32,37,44,51,60,68,76,82,80,74,64,44],
      jueves:    [29,41,50,38,28,31,36,43,49,58,66,74,79,77,72,62,42],
      viernes:   [28,39,48,37,27,30,35,42,48,55,62,69,73,70,64,55,38],
      sabado:    [30,44,56,63,61,56,50,45,40,36,34,32,31,29,27,26,24]
    }
  },

  /* Gym 5 — Imperium Oeste  (demanda media, pico 60-70%) */
  gym5: {
    horario: {
      lunes:     [18,28,38,27,16,18,22,29,37,46,56,64,70,68,63,53,34],
      martes:    [17,27,36,25,15,17,21,28,35,44,53,61,67,65,60,50,31],
      miercoles: [16,26,35,24,14,16,20,27,34,43,51,59,65,63,58,48,30],
      jueves:    [15,25,33,22,13,15,19,26,32,41,49,57,62,60,55,46,28],
      viernes:   [14,23,31,21,12,14,18,25,31,38,45,52,56,54,49,40,25],
      sabado:    [15,24,34,42,41,37,32,28,24,21,19,18,17,16,15,14,13]
    }
  }
};

/* Etiquetas de hora para el eje X de las gráficas */
const HORAS_LABELS = [
  "06:00","07:00","08:00","09:00","10:00","11:00","12:00",
  "13:00","14:00","15:00","16:00","17:00","18:00","19:00",
  "20:00","21:00","22:00"
];

/* Días de la semana — mismo orden que Date.getDay() */
const DIAS_SEMANA = ["domingo","lunes","martes","miercoles","jueves","viernes","sabado"];

/*
  Devuelve el array de 17 valores del día actual para un gimnasio.
  gymKey: "gym1" … "gym5"
*/
function getOcupacionHoy(gymKey) {
  const hoy = DIAS_SEMANA[new Date().getDay()];
  return OCUPACION_STATS[gymKey]?.horario[hoy] || new Array(17).fill(0);
}

/*
  Devuelve el porcentaje de ocupación en la hora actual.
  Si es antes de las 06:00 o después de las 22:00 devuelve 0.
*/
function getOcupacionAhora(gymKey) {
  const h   = new Date().getHours();
  const idx = h - 6;
  if (idx < 0 || idx > 16) return 0;
  return getOcupacionHoy(gymKey)[idx];
}