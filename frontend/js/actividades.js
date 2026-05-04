/* =====================================================
   IMPERIUM FITNESS — actividades.js
   Sistema de Calendario + Reservas de Clases
   -------------------------------------------------------
   Este archivo se carga DESPUÉS de app.js, por lo que
   puede usar Auth, showToast y las utilidades globales.

   ESTRUCTURA:
     1. Datos — horario semanal + imágenes por categoría
     2. Estado — variables que guardan lo que el usuario ha elegido
     3. Calendario — dibuja el mes, gestiona navegación y selección
     4. Clases — filtra y pinta las tarjetas del día seleccionado
     5. Modal — confirmación antes de reservar
     6. Reservas — guarda/cancela en localStorage y refresca UI
     7. Arranque — conecta todo cuando la página carga
   ===================================================== */


/* =====================================================
   1. DADES — carregades des del backend
   ===================================================== */

function classeBackendToLocal(c) {
  const data   = new Date(c.horari);
  const dayIdx = data.getDay();
  const dayName = NOMBRE_DIA[dayIdx];
  const hh = String(data.getHours()).padStart(2, "0");
  const mm = String(data.getMinutes()).padStart(2, "0");

  return {
    id:            String(c.id),
    name:          c.nom,
    category:      detectaCategoria(c.nom),
    instructor:    "—",
    time:          `${hh}:${mm}`,
    duration:      "60 min",
    spots:         c.capacitat,
    dayName,
    horariComplet: c.horari
  };
}

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

function construeixHorari(classes) {
  const horari = {};
  classes.forEach(c => {
    const local = classeBackendToLocal(c);
    if (!horari[local.dayName]) horari[local.dayName] = [];
    horari[local.dayName].push(local);
  });
  return horari;
}

let HORARIO_SEMANAL = {};

const CATEGORY_IMAGES = {
  crossfit:  "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=120&h=120&fit=crop",
  yoga:      "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=120&h=120&fit=crop",
  spinning:  "https://images.unsplash.com/photo-1571902943202-507ec2618e8f?w=120&h=120&fit=crop",
  hiit:      "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=120&h=120&fit=crop",
  pilates:   "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=120&h=120&fit=crop",
  funcional: "https://images.unsplash.com/photo-1549060279-7e168fcee0c2?w=120&h=120&fit=crop",
  boxeo:     "https://images.unsplash.com/photo-1555597673-b21d5c935865?w=120&h=120&fit=crop"
};

const NOMBRE_DIA = ["Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"];

const NOMBRE_MES = [
  "Enero","Febrero","Marzo","Abril","Mayo","Junio",
  "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
];


/* =====================================================
   2. ESTADO DE LA PÁGINA
   ===================================================== */

let calYear  = new Date().getFullYear();
let calMonth = new Date().getMonth();
let selectedDate = new Date();
let activeFilter = "all";
let pendingClass  = null;
let pendingDateKey = null;


/* =====================================================
   3. CALENDARIO
   ===================================================== */

function toDateKey(year, month, day) {
  const mm = String(month + 1).padStart(2, "0");
  const dd = String(day).padStart(2, "0");
  return `${year}-${mm}-${dd}`;
}

/* Calcula la data màxima permesa (2 mesos des d'avui) */
function getMaxDateKey() {
  const today  = new Date();
  const maxDate = new Date(today.getFullYear(), today.getMonth() + 2, today.getDate());
  return toDateKey(maxDate.getFullYear(), maxDate.getMonth(), maxDate.getDate());
}

function renderCalendar() {
  const grid      = document.getElementById("calendar-grid");
  const label     = document.getElementById("cal-month-label");
  const today     = new Date();
  const todayKey  = toDateKey(today.getFullYear(), today.getMonth(), today.getDate());
  const selKey    = toDateKey(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate());
  const maxKey    = getMaxDateKey(); // ← límit de 2 mesos

  label.textContent = `${NOMBRE_MES[calMonth]} ${calYear}`;

  const firstDayRaw = new Date(calYear, calMonth, 1).getDay();
  const firstDayLun = (firstDayRaw + 6) % 7;
  const daysInMonth = new Date(calYear, calMonth + 1, 0).getDate();

  let html = "";

  // Celdas vacías
  for (let i = 0; i < firstDayLun; i++) {
    html += `<div class="cal-day empty"></div>`;
  }

  // Dies del mes
  for (let d = 1; d <= daysInMonth; d++) {
    const dateKey  = toDateKey(calYear, calMonth, d);
    const dateObj  = new Date(calYear, calMonth, d);
    const dayIndex = dateObj.getDay();
    const dayName  = NOMBRE_DIA[dayIndex];

    const isPast   = dateKey < todayKey;
    const isSunday = dayIndex === 0;
    const isTooFar = dateKey > maxKey;  // ← bloqueja dies fora del rang
    const isToday  = dateKey === todayKey;
    const isSelected = dateKey === selKey;

    let classes = "cal-day";
    if (isToday)    classes += " today";
    if (isSelected) classes += " selected";
    if (isPast || isSunday || isTooFar) classes += " disabled";

    // Punt indicador — només dies disponibles amb classes
    const hasClasses = !isSunday && !isTooFar && HORARIO_SEMANAL[dayName] !== undefined;
    const dot = hasClasses && !isPast
      ? `<span class="dot"></span>`
      : "";

    // Click — només dies no bloquejats
    const click = (!isPast && !isSunday && !isTooFar)
      ? `onclick="selectDay(${calYear}, ${calMonth}, ${d})"`
      : "";

    html += `<div class="${classes}" ${click}>${d}${dot}</div>`;
  }

  grid.innerHTML = html;
}

function selectDay(year, month, day) {
  selectedDate = new Date(year, month, day);
  renderCalendar();
  updateSelectedDayInfo();
  renderClasesDelDia();
}

function updateSelectedDayInfo() {
  const info     = document.getElementById("selected-day-info");
  const dayIndex = selectedDate.getDay();
  const dayName  = NOMBRE_DIA[dayIndex];
  const dd       = selectedDate.getDate();
  const mm       = NOMBRE_MES[selectedDate.getMonth()];
  const yyyy     = selectedDate.getFullYear();

  if (dayIndex === 0) {
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

function renderClasesDelDia() {
  const container  = document.getElementById("act-classes-list");
  const titleEl    = document.getElementById("col-day-title");
  const countEl    = document.getElementById("classes-count");

  const dayIndex   = selectedDate.getDay();
  const dayName    = NOMBRE_DIA[dayIndex];
  const dd         = selectedDate.getDate();
  const mm         = NOMBRE_MES[selectedDate.getMonth()];

  titleEl.textContent = `${dayName} ${dd} de ${mm}`;

  if (dayIndex === 0) {
    container.innerHTML = `
      <div class="no-classes-msg">
        <strong>Sin actividades</strong>
        Los domingos no hay clases programadas. Disfruta del descanso.
      </div>`;
    countEl.textContent = "";
    return;
  }

  let clases = HORARIO_SEMANAL[dayName] || [];

  if (activeFilter !== "all") {
    clases = clases.filter(c => c.category === activeFilter);
  }

  if (clases.length === 0) {
    container.innerHTML = `
      <div class="no-classes-msg">
        <strong>Sin resultados</strong>
        No hay clases de esta categoria para el dia seleccionado.
      </div>`;
    countEl.textContent = "";
    return;
  }

  clases = clases.slice().sort((a, b) => a.time.localeCompare(b.time));
  countEl.textContent = `${clases.length} clase${clases.length !== 1 ? "s" : ""}`;
  container.innerHTML = clases.map(c => buildClassCard(c)).join("");
}

function buildClassCard(clase) {
  const dateKey   = toDateKey(
    selectedDate.getFullYear(),
    selectedDate.getMonth(),
    selectedDate.getDate()
  );
  const reservaId = `${dateKey}_${clase.id}`;

  const reservasHoy    = getReservasDelDia(dateKey, clase.id);
  const disponibles    = clase.spots - reservasHoy;
  const pct            = Math.round((reservasHoy / clase.spots) * 100);
  const estaLlena      = disponibles <= 0;
  const yaReservada    = userHasReservation(reservaId);
  const imgSrc         = CATEGORY_IMAGES[clase.category] || "";

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
  if (!Auth.isLoggedIn()) {
    showToast("Debes iniciar sesión para reservar clases.", "error");
    setTimeout(() => { window.location.href = "login.html"; }, 1500);
    return;
  }

  const dayIndex = selectedDate.getDay();
  const dayName  = NOMBRE_DIA[dayIndex];
  const clases   = HORARIO_SEMANAL[dayName] || [];
  const clase    = clases.find(c => String(c.id) === String(classId));

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

function closeModal() {
  document.getElementById("reservation-modal").classList.remove("open");
  pendingClass   = null;
  pendingDateKey = null;
}

function formatDateKey(dateKey) {
  if (!dateKey) return "—";

  // Format amb guions: "2026-04-21" → "21/04/2026"
  if (dateKey.includes("-")) {
    const [y, m, d] = dateKey.split("-");
    return `${d}/${m}/${y}`;
  }

  // Format sense guions: "20260421" → "21/04/2026"
  const y = dateKey.substring(0, 4);
  const m = dateKey.substring(4, 6);
  const d = dateKey.substring(6, 8);
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

  const claseAReservar = pendingClass;
  const dateKeyReserva = pendingDateKey;

  try {
    const result = await ApiClasses.reservar(
      parseInt(user.id),
      parseInt(claseAReservar.id)
    );

    closeModal();

    if (result.ok) {
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

/* ── localStorage ── */

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
  const user      = Auth.getUser();
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
  if (!totes.find(r => r.reservaId === reservaId)) {
    totes.push(nova);
    saveAllReservas(totes);
  }
}

function getReservasDelDia(dateKey, classId) {
  const totes = loadAllReservas();
  return totes.filter(r => r.dateKey === dateKey && String(r.classId) === String(classId)).length;
}

function userHasReservation(reservaId) {
  const totes = loadAllReservas();
  return totes.some(r => r.reservaId === reservaId);
}

async function cancelReservation(reservaId) {
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

  const totes = loadAllReservas();
  const avui  = toDateKey(
    new Date().getFullYear(),
    new Date().getMonth(),
    new Date().getDate()
  );

  // ── Filtrem reserves passades i les eliminem del localStorage ──
  const vigents = totes.filter(r => r.dateKey >= avui);

  // Si hi ha reserves passades les eliminem del localStorage
  if (vigents.length !== totes.length) {
    saveAllReservas(vigents);
  }

  const misReservas = vigents.filter(r => r.userEmail === user.email);

  if (misReservas.length === 0) {
    lista.innerHTML = `<p class="no-reservations">No tienes ninguna reserva próxima.</p>`;
    return;
  }

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
async function sincronitzaReserves() {
  const user = Auth.getUser();
  if (!user || !Auth.isLoggedIn()) return;

  try {
    const reserves = await ApiUsuari.getReserves(user.id);
    if (!reserves || reserves.length === 0) return;

    const totes = loadAllReservas();

    reserves.forEach(r => {
      // ✅ Format correcte: "2026-04-21" (amb guions, com toDateKey())
      let dateKey;
      if (r.dataReserva) {
        dateKey = r.dataReserva.substring(0, 10); // "2026-04-21"
      } else {
        const ara = new Date();
        dateKey = toDateKey(ara.getFullYear(), ara.getMonth(), ara.getDate());
      }

      const reservaId = `${dateKey}_${r.classeId}`;

      if (!totes.find(x => x.reservaId === reservaId)) {
        totes.push({
          reservaId,
          classId:    String(r.classeId),
          dateKey,
          className:  `Clase #${r.classeId}`,
          category:   "funcional",
          time:       "—",
          instructor: "—",
          userEmail:  user.email
        });
      }
    });

    saveAllReservas(totes);
    milloraNomsReserves();

  } catch (e) {
    console.error("Error sincronitzant reserves:", e);
  }
}

function milloraNomsReserves() {
  const user = Auth.getUser();
  if (!user) return;

  const totes = loadAllReservas();
  let modificat = false;

  totes.forEach(r => {
    if (r.className && r.className.startsWith("Clase #")) {
      Object.values(HORARIO_SEMANAL).forEach(classes => {
        const classe = classes.find(c => String(c.id) === String(r.classId));
        if (classe) {
          r.className  = classe.name;
          r.category   = classe.category;
          r.time       = classe.time;
          r.instructor = classe.instructor;
          modificat    = true;
        }
      });
    }
  });

  if (modificat) saveAllReservas(totes);
}

/* =====================================================
   7. ARRANQUE — càrrega asíncrona des del backend
   ===================================================== */

document.addEventListener("DOMContentLoaded", async function () {

  if (!document.getElementById("calendar-grid")) return;

  const container = document.getElementById("act-classes-list");
  if (container) {
    container.innerHTML = `
      <div style="text-align:center;padding:3rem;color:var(--text-muted)">
        ⏳ Cargando clases...
      </div>`;
  }

  // Carreguem les classes del backend
  const classes = await ApiClasses.getAll();
  HORARIO_SEMANAL = construeixHorari(classes);

  // Sincronitzem les reserves de la BD amb el localStorage
  // Això evita que el botó mostri "Reservar" quan ja tens la classe reservada
  await sincronitzaReserves();

  // Renderitzem
  renderCalendar();
  updateSelectedDayInfo();
  renderClasesDelDia();
  renderMisReservas();

  // Delegació d'esdeveniments pels botons de reserva
  document.getElementById("act-classes-list").addEventListener("click", function(e) {
    const btn = e.target.closest(".btn-reservar.disponible");
    if (!btn) return;
    openReservationModal(btn.dataset.classId, btn.dataset.dateKey);
  });

  // Navegació del calendari — amb límit de 2 mesos
  document.getElementById("cal-prev").addEventListener("click", function () {
    calMonth--;
    if (calMonth < 0) { calMonth = 11; calYear--; }
    renderCalendar();
  });

  document.getElementById("cal-next").addEventListener("click", function () {
    // Calculem el mes màxim permès (2 mesos des d'avui)
    const today    = new Date();
    const maxYear  = today.getMonth() >= 10
      ? today.getFullYear() + 1
      : today.getFullYear();
    const maxMonth = (today.getMonth() + 2) % 12;

    // Bloquejem si ja estem al mes màxim
    if (calYear === maxYear && calMonth === maxMonth) {
      showToast("No puedes ver clases más allá de 2 meses.", "error");
      return;
    }

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
  document.getElementById("modal-confirm-btn").addEventListener("click", async function() {
    await confirmReservation();
  });
  document.getElementById("reservation-modal").addEventListener("click", function (e) {
    if (e.target === this) closeModal();
  });
  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape") closeModal();
  });
});