/* =====================================================
   IMPERIUM FITNESS — actividades.js
   Sistema de Calendario + Reservas de Clases
   -------------------------------------------------------
   Este archivo se carga DESPUÉS de app.js, por lo que
   puede usar Auth, showToast y las utilidades globales.

   ESTRUCTURA:
     1. Datos — horario semanal fijo + imágenes por categoría
     2. Estado — variables que guardan lo que el usuario ha elegido
     3. Calendario — dibuja el mes, gestiona navegación y selección
     4. Clases — filtra y pinta las tarjetas del día seleccionado
     5. Modal — confirmación antes de reservar
     6. Reservas — guarda/cancela en localStorage y refresca UI
     7. Arranque — conecta todo cuando la página carga
   ===================================================== */


/* =====================================================
   1. DATOS
   ===================================================== */

/*
  HORARIO_SEMANAL es un objeto cuyas claves son los días
  de la semana en español (como los devuelve Date).
  Cada día tiene un array de clases.

  Cada clase tiene:
    id          → identificador único (para guardar en localStorage)
    name        → nombre de la clase
    category    → usada para filtrar y para el color de la banda
    instructor  → nombre del instructor
    time        → hora de inicio  "HH:MM"
    duration    → duración en minutos (string)
    spots       → plazas totales
*/
/* =====================================================
   1. DADES — carregades des del backend
   HORARIO_SEMANAL ja no és estàtic: es construeix
   dinàmicament amb les classes que retorna l'API.
   ===================================================== */

/* Mapeja el backend al format que espera el calendari:
   { "Lunes": [{ id, name, category, instructor... }] }
   El backend retorna: { id, nom, descripcio, horari, capacitat, gimnasId } */
function classeBackendToLocal(c) {
  const data   = new Date(c.horari);
  const dayIdx = data.getDay(); // 0=Diumenge … 6=Dissabte
  const dayName = NOMBRE_DIA[dayIdx];
  const hh = String(data.getHours()).padStart(2, "0");
  const mm = String(data.getMinutes()).padStart(2, "0");

  return {
    id:         String(c.id),
    name:       c.nom,
    // Intentem deduir la categoria del nom si no ve del backend
    category:   detectaCategoria(c.nom),
    instructor: "—",           // el backend no té instructor per ara
    time:       `${hh}:${mm}`,
    duration:   "60 min",
    spots:      c.capacitat,
    dayName,
    horariComplet: c.horari    // guardem la data completa per filtrar
  };
}

/* Dedueix la categoria a partir del nom de la classe */
function detectaCategoria(nom) {
  const n = nom.toLowerCase();
  if (n.includes("spinning") || n.includes("cicl")) return "spinning";
  if (n.includes("yoga"))                            return "yoga";
  if (n.includes("pilates"))                         return "pilates";
  if (n.includes("hiit"))                            return "hiit";
  if (n.includes("crossfit") || n.includes("cross")) return "crossfit";
  if (n.includes("box"))                             return "boxeo";
  if (n.includes("zumba") || n.includes("ball"))     return "funcional";
  if (n.includes("pump") || n.includes("body"))      return "funcional";
  return "funcional";
}

/* Construeix el HORARIO_SEMANAL a partir de les classes del backend */
function construeixHorari(classes) {
  const horari = {};
  classes.forEach(c => {
    const local = classeBackendToLocal(c);
    if (!horari[local.dayName]) horari[local.dayName] = [];
    horari[local.dayName].push(local);
  });
  return horari;
}

/* Variable global que s'omplirà quan carregui la pàgina */
let HORARIO_SEMANAL = {};

/*
  Imágenes de Unsplash para cada categoría.
  Son URLs públicas, no requieren API key.
  En producción se usarían imágenes propias.
*/
const CATEGORY_IMAGES = {
  crossfit:  "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=120&h=120&fit=crop",
  yoga:      "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=120&h=120&fit=crop",
  spinning:  "https://images.unsplash.com/photo-1571902943202-507ec2618e8f?w=120&h=120&fit=crop",
  hiit:      "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=120&h=120&fit=crop",
  pilates:   "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=120&h=120&fit=crop",
  funcional: "https://images.unsplash.com/photo-1549060279-7e168fcee0c2?w=120&h=120&fit=crop",
  boxeo:     "https://images.unsplash.com/photo-1555597673-b21d5c935865?w=120&h=120&fit=crop"
};

/* Nombres de los días según el índice de Date.getDay()
   0 = Domingo, 1 = Lunes … 6 = Sábado */
const NOMBRE_DIA = ["Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"];

/* Nombres de los meses en español */
const NOMBRE_MES = [
  "Enero","Febrero","Marzo","Abril","Mayo","Junio",
  "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
];


/* =====================================================
   2. ESTADO DE LA PÁGINA
   Variables que guardan qué tiene seleccionado el usuario
   ===================================================== */

// Fecha "mostrada" en el calendario (puede ser distinta a la de hoy)
let calYear  = new Date().getFullYear();
let calMonth = new Date().getMonth();      // 0–11

// Fecha seleccionada por el usuario (empieza con hoy)
let selectedDate = new Date();

// Filtro de categoría activo
let activeFilter = "all";

// Clase que está a punto de reservarse (pendiente de confirmar en el modal)
let pendingClass  = null;   // objeto de HORARIO_SEMANAL
let pendingDateKey = null;  // "YYYY-MM-DD" para identificar el día


/* =====================================================
   3. CALENDARIO
   Dibuja el mes, gestiona la navegación y los clics
   ===================================================== */

/* Genera la cadena "YYYY-MM-DD" de una fecha */
function toDateKey(year, month, day) {
  const mm = String(month + 1).padStart(2, "0");
  const dd = String(day).padStart(2, "0");
  return `${year}-${mm}-${dd}`;
}

/*
  renderCalendar() — pinta el mes actual en el DOM.
  Lógica paso a paso:
    1. Calcula el primer y último día del mes
    2. Determina en qué columna (L–D) empieza el mes
    3. Añade celdas vacías al principio
    4. Por cada día decide si es: hoy / pasado / domingo / normal / seleccionado
    5. Los días con clases llevan un punto dorado
*/
function renderCalendar() {
  const grid      = document.getElementById("calendar-grid");
  const label     = document.getElementById("cal-month-label");
  const today     = new Date();
  const todayKey  = toDateKey(today.getFullYear(), today.getMonth(), today.getDate());
  const selKey    = toDateKey(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate());

  // Actualiza el título: "Abril 2026"
  label.textContent = `${NOMBRE_MES[calMonth]} ${calYear}`;

  // Día de la semana del 1 del mes (0=Dom, 1=Lun…)
  const firstDayRaw = new Date(calYear, calMonth, 1).getDay();
  // Ajustamos para que Lunes sea 0 (nuestra cuadrícula empieza en Lunes)
  const firstDayLun = (firstDayRaw + 6) % 7;

  // Cuántos días tiene el mes
  const daysInMonth = new Date(calYear, calMonth + 1, 0).getDate();

  let html = "";

  // Celdas vacías antes del día 1
  for (let i = 0; i < firstDayLun; i++) {
    html += `<div class="cal-day empty"></div>`;
  }

  // Días del mes
  for (let d = 1; d <= daysInMonth; d++) {
    const dateKey  = toDateKey(calYear, calMonth, d);
    const dateObj  = new Date(calYear, calMonth, d);
    const dayIndex = dateObj.getDay();       // 0=Dom…6=Sab
    const dayName  = NOMBRE_DIA[dayIndex];

    // Determinar clases CSS del día
    let classes = "cal-day";

    const isPast    = dateKey < todayKey;
    const isSunday  = dayIndex === 0;
    const isToday   = dateKey === todayKey;
    const isSelected = dateKey === selKey;

    if (isToday)    classes += " today";
    if (isSelected) classes += " selected";
    // Bloqueado: pasado o domingo
    if (isPast || isSunday) classes += " disabled";

    // Tiene clases ese día (para mostrar el punto)?
    const hasClasses = !isSunday && HORARIO_SEMANAL[dayName] !== undefined;

    // Punto indicador (solo en días no bloqueados y no seleccionados)
    const dot = hasClasses && !isPast && !isSunday
      ? `<span class="dot"></span>`
      : "";

    // Atributo onclick solo si el día no está bloqueado
    const click = (!isPast && !isSunday)
      ? `onclick="selectDay(${calYear}, ${calMonth}, ${d})"`
      : "";

    html += `<div class="${classes}" ${click}>${d}${dot}</div>`;
  }

  grid.innerHTML = html;
}

/* selectDay — se llama al clicar un día del calendario */
function selectDay(year, month, day) {
  // Guarda la fecha seleccionada
  selectedDate = new Date(year, month, day);

  // Actualiza el estado y re-pinta el calendario para reflejar la selección
  renderCalendar();

  // Actualiza el texto informativo debajo del calendario
  updateSelectedDayInfo();

  // Muestra las clases del día en la columna derecha
  renderClasesDelDia();
}

/* Actualiza el bloque de información del día seleccionado */
function updateSelectedDayInfo() {
  const info     = document.getElementById("selected-day-info");
  const dayIndex = selectedDate.getDay();
  const dayName  = NOMBRE_DIA[dayIndex];
  const dd       = selectedDate.getDate();
  const mm       = NOMBRE_MES[selectedDate.getMonth()];
  const yyyy     = selectedDate.getFullYear();

  if (dayIndex === 0) {
    // Domingo: sin clases
    info.innerHTML = `<strong>${dayName} ${dd} de ${mm}</strong>Sin actividades este día`;
  } else {
    const clases = HORARIO_SEMANAL[dayName] || [];
    info.innerHTML = `
      <strong>${dayName} ${dd} de ${mm} de ${yyyy}</strong>
      ${clases.length} clase${clases.length !== 1 ? "s" : ""} disponible${clases.length !== 1 ? "s" : ""}
    `;
  }
}


/* =====================================================
   4. CLASES — pintado de tarjetas
   ===================================================== */

/*
  renderClasesDelDia() — lee el día seleccionado, obtiene
  las clases del horario, aplica el filtro activo y pinta
  las tarjetas en #act-classes-list.
*/
function renderClasesDelDia() {
  const container  = document.getElementById("act-classes-list");
  const titleEl    = document.getElementById("col-day-title");
  const countEl    = document.getElementById("classes-count");

  const dayIndex   = selectedDate.getDay();
  const dayName    = NOMBRE_DIA[dayIndex];
  const dd         = selectedDate.getDate();
  const mm         = NOMBRE_MES[selectedDate.getMonth()];

  // Actualiza el título de la columna derecha
  titleEl.textContent = `${dayName} ${dd} de ${mm}`;

  // Domingo: sin actividades
  if (dayIndex === 0) {
    container.innerHTML = `
      <div class="no-classes-msg">
        <strong>Sin actividades</strong>
        Los domingos no hay clases programadas. Disfruta del descanso.
      </div>`;
    countEl.textContent = "";
    return;
  }

  // Obtiene las clases del día (puede ser undefined si el día no tiene)
  let clases = HORARIO_SEMANAL[dayName] || [];

  // Aplica el filtro de categoría
  if (activeFilter !== "all") {
    clases = clases.filter(c => c.category === activeFilter);
  }

  // Si no hay clases con ese filtro
  if (clases.length === 0) {
    container.innerHTML = `
      <div class="no-classes-msg">
        <strong>Sin resultados</strong>
        No hay clases de esta categoria para el dia seleccionado.
      </div>`;
    countEl.textContent = "";
    return;
  }

  // Ordena por hora (ascendente)
  clases = clases.slice().sort((a, b) => a.time.localeCompare(b.time));

  countEl.textContent = `${clases.length} clase${clases.length !== 1 ? "s" : ""}`;

  // Genera el HTML de cada tarjeta
  container.innerHTML = clases.map(c => buildClassCard(c)).join("");
}

/*
  buildClassCard(clase) — devuelve el HTML de una tarjeta.
  Determina si el usuario ya tiene reservada esta clase en este día.
*/
function buildClassCard(clase) {
  const dateKey    = toDateKey(
    selectedDate.getFullYear(),
    selectedDate.getMonth(),
    selectedDate.getDate()
  );
  const reservaId  = `${dateKey}_${clase.id}`;

  const reservasHoy    = getReservasDelDia(dateKey, clase.id);
  const plazasOcupadas = reservasHoy;
  const disponibles    = clase.spots - plazasOcupadas;
  const pct            = Math.round((plazasOcupadas / clase.spots) * 100);
  const estaLlena      = disponibles <= 0;
  const yaReservada    = userHasReservation(reservaId);

  const imgSrc = CATEGORY_IMAGES[clase.category] || "";

  // Botó amb dataset en lloc d'onclick inline per evitar problemes amb cometes
  let btnHtml;
  if (yaReservada) {
    btnHtml = `<button class="btn-reservar reservada" disabled>Reservada</button>`;
  } else if (estaLlena) {
    btnHtml = `<button class="btn-reservar completo" disabled>Completo</button>`;
  } else {
    btnHtml = `
      <button
        class="btn-reservar disponible"
        data-class-id="${clase.id}"
        data-date-key="${dateKey}"
      >
        Reservar
      </button>`;
  }

  return `
    <div class="act-card">
      <div class="act-card-color ${clase.category}"></div>
      <img
        class="act-card-img"
        src="${imgSrc}"
        alt="${clase.category}"
        loading="lazy"
        onerror="this.style.display='none'"
      >
      <div class="act-card-body">
        <div class="act-card-top">
          <span class="act-card-name">${clase.name}</span>
          <span class="act-card-time">${clase.time}</span>
        </div>
        <div class="act-card-meta">
          <span>Instructor: ${clase.instructor}</span>
          <span>${clase.duration}</span>
        </div>
        <div class="act-spots-row">
          <div class="act-spots-bar">
            <div
              class="act-spots-bar-fill ${pct >= 80 ? "full" : ""}"
              style="width: ${pct}%"
            ></div>
          </div>
          <span class="act-spots-text">
            ${estaLlena ? "Sin plazas" : `${disponibles} / ${clase.spots} plazas`}
          </span>
        </div>
        <div class="act-card-footer">
          <span class="act-category-tag ${clase.category}">${clase.category}</span>
          ${btnHtml}
        </div>
      </div>
    </div>
  `;
}


/* =====================================================
   5. MODAL DE CONFIRMACION
   ===================================================== */

function openReservationModal(classId, dateKey) {
  const user = Auth.getUser();

  if (!Auth.isLoggedIn()) {
    showToast("Debes iniciar sesión para reservar clases.", "error");
    setTimeout(() => { window.location.href = "login.html"; }, 1500);
    return;
  }

  const dayIndex = selectedDate.getDay();
  const dayName  = NOMBRE_DIA[dayIndex];
  const clases   = HORARIO_SEMANAL[dayName] || [];
  const clase    = clases.find(c => String(c.id) === String(classId));

  // LOG TEMPORAL: comprova que troba la classe
  console.log("classId rebut:", classId);
  console.log("clases del dia:", clases);
  console.log("classe trobada:", clase);

  if (!clase) {
    showToast("No se encontró la clase.", "error");
    return;
  }

  pendingClass   = clase;
  pendingDateKey = dateKey;

  const msg = document.getElementById("modal-message");
  if (msg) {
    msg.innerHTML = `
      Quieres reservar <strong>${clase.name}</strong>
      el día <strong>${formatDateKey(dateKey)}</strong>
      a las <strong>${clase.time}</strong>?
    `;
  }

  document.getElementById("reservation-modal").classList.add("open");
}
/* Cierra el modal y limpia el estado pendiente */
function closeModal() {
  document.getElementById("reservation-modal").classList.remove("open");
  pendingClass   = null;
  pendingDateKey = null;
}

/* Convierte "2026-04-14" a "14/04/2026" para mostrar en el modal */
function formatDateKey(dateKey) {
  const [y, m, d] = dateKey.split("-");
  return `${d}/${m}/${y}`;
}


/* =====================================================
   6. SISTEMA DE RESERVES — connectat al backend real
   ===================================================== */

async function confirmReservation() {
  if (!pendingClass || !pendingDateKey) return;

  const user = Auth.getUser();
  if (!user) {
    showToast("Debes iniciar sesión para reservar.", "error");
    setTimeout(() => window.location.href = "login.html", 1500);
    return;
  }

  const btnConfirm = document.getElementById("modal-confirm-btn");
  if (btnConfirm) {
    btnConfirm.disabled = true;
    btnConfirm.textContent = "Reservando...";
  }

  // Guardem referències locals abans de netejar l'estat
  const claseAReservar = pendingClass;
  const dateKeyReserva = pendingDateKey;

  try {
    const result = await ApiClasses.reservar(
      parseInt(user.id),
      parseInt(claseAReservar.id)
    );

    // Tanquem el modal SEMPRE abans de mostrar el toast
    closeModal();

    if (result.ok) {
      // Guardem localment per refresc immediat de la UI
      addReservacioLocal(claseAReservar, dateKeyReserva);
      renderClasesDelDia();
      renderMisReservas();
      showToast(`Reserva de "${claseAReservar.name}" confirmada. 🎉`, "success");
    } else {
      showToast(result.error || "No se pudo completar la reserva.", "error");
    }

  } catch (e) {
    console.error("Error confirmReservation:", e);
    closeModal();
    showToast("Error al procesar la reserva.", "error");
  } finally {
    if (btnConfirm) {
      btnConfirm.disabled = false;
      btnConfirm.textContent = "Confirmar";
    }
  }
}

/* ── localStorage: guardem localment per refresc immediat ── */

function loadAllReservas() {
  const user = Auth.getUser();
  if (!user) return [];
  const data = localStorage.getItem(`imperium_reservas_${user.id}`);
  return data ? JSON.parse(data) : [];
}

function saveAllReservas(reservas) {
  const user = Auth.getUser();
  if (!user) return;
  localStorage.setItem(`imperium_reservas_${user.id}`, JSON.stringify(reservas));
}

function addReservacioLocal(clase, dateKey) {
  const user    = Auth.getUser();
  const reservaId = `${dateKey}_${clase.id}`;
  const nova = {
    reservaId,
    classId:    clase.id,
    dateKey,
    className:  clase.name,
    category:   clase.category,
    time:       clase.time,
    instructor: clase.instructor,
    userEmail:  user.email
  };
  const totes = loadAllReservas();
  // Evitem duplicats
  if (!totes.find(r => r.reservaId === reservaId)) {
    totes.push(nova);
    saveAllReservas(totes);
  }
}

function getReservasDelDia(dateKey, classId) {
  const totes = loadAllReservas();
  return totes.filter(r => r.dateKey === dateKey && r.classId === classId).length;
}

function userHasReservation(reservaId) {
  const totes = loadAllReservas();
  return totes.some(r => r.reservaId === reservaId);
}

async function cancelReservation(reservaId) {
  // Per ara cancel·lem localment (pots afegir l'endpoint DELETE al backend)
  let totes = loadAllReservas();
  totes = totes.filter(r => r.reservaId !== reservaId);
  saveAllReservas(totes);
  renderClasesDelDia();
  renderMisReservas();
  showToast("Reserva cancelada correctamente.", "success");
}
/* =====================================================
   RENDER — Llista "Mis Reservas"
   ===================================================== */

function renderMisReservas() {
  const lista = document.getElementById("my-reservations-list");
  if (!lista) return;

  const user = Auth.getUser();

  if (!Auth.isLoggedIn() || !user) {
    lista.innerHTML = `
      <p class="no-reservations">
        <a href="login.html">Inicia sesión</a> para ver tus reservas
      </p>`;
    return;
  }

  const totes      = loadAllReservas();
  const misReservas = totes.filter(r => r.userEmail === user.email);

  if (misReservas.length === 0) {
    lista.innerHTML = `
      <p class="no-reservations">No tienes ninguna reserva aún.</p>`;
    return;
  }

  // Ordena per data i hora
  misReservas.sort((a, b) => {
    if (a.dateKey !== b.dateKey) return a.dateKey.localeCompare(b.dateKey);
    return a.time.localeCompare(b.time);
  });

  lista.innerHTML = misReservas.map(r => `
    <div class="my-res-item">
      <div class="res-info">
        <div class="res-name">${r.className}</div>
        <div class="res-meta">${formatDateKey(r.dateKey)} · ${r.time}</div>
      </div>
      <button
        class="btn-cancel-res"
        title="Cancelar reserva"
        onclick="cancelReservation('${r.reservaId}')"
      >✕</button>
    </div>
  `).join("");
}

/* =====================================================
   7. ARRANQUE — càrrega asíncrona des del backend
   ===================================================== */

document.addEventListener("DOMContentLoaded", async function () {

  if (!document.getElementById("calendar-grid")) return;

  // Mostrem un indicador de càrrega mentre esperem el backend
  const container = document.getElementById("act-classes-list");
  if (container) {
    container.innerHTML = `
      <div style="text-align:center;padding:3rem;color:var(--text-muted)">
        ⏳ Cargando clases...
      </div>`;
  }

  // Carreguem les classes des del backend
  const classes = await ApiClasses.getAll();
  HORARIO_SEMANAL = construeixHorari(classes);

  // Dibuixem el calendari i les classes del dia actual
  renderCalendar();
  updateSelectedDayInfo();
  renderClasesDelDia();
  renderMisReservas();

  // Delegació d'esdeveniments pels botons de reserva
// Millor que onclick inline: funciona amb contingut generat dinàmicament
document.getElementById("act-classes-list").addEventListener("click", function(e) {
  const btn = e.target.closest(".btn-reservar.disponible");
  if (!btn) return;
  const classId = btn.dataset.classId;
  const dateKey = btn.dataset.dateKey;
  openReservationModal(classId, dateKey);
});

  // Navegació del calendari
  document.getElementById("cal-prev").addEventListener("click", function () {
    calMonth--;
    if (calMonth < 0) { calMonth = 11; calYear--; }
    renderCalendar();
  });

  document.getElementById("cal-next").addEventListener("click", function () {
    calMonth++;
    if (calMonth > 11) { calMonth = 0; calYear++; }
    renderCalendar();
  });

  // Filtres de categoria
  document.querySelectorAll("#act-filters .filter-tab").forEach(function (btn) {
    btn.addEventListener("click", function () {
      document.querySelectorAll("#act-filters .filter-tab")
              .forEach(b => b.classList.remove("active"));
      this.classList.add("active");
      activeFilter = this.dataset.filter;
      renderClasesDelDia();
    });
  });

  // Modal
  document.getElementById("modal-cancel-btn").addEventListener("click", closeModal);
  document.getElementById("modal-confirm-btn").addEventListener("click", confirmReservation);
  document.getElementById("reservation-modal").addEventListener("click", function (e) {
    if (e.target === this) closeModal();
  });
  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape") closeModal();
  });
});