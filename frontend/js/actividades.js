/* =====================================================
   IMPERIUM FITNESS — actividades.js
   Sistema de Calendario + Reservas de Clases
   ===================================================== */


/* =====================================================
   1. DADES — carregades des del backend
   ===================================================== */

// Totes les classes del backend (sense agrupar)
let TOTES_LES_CLASSES = [];

function classeBackendToLocal(c) {
  const data    = new Date(c.horari);
  const dayIdx  = data.getDay();
  const dayName = NOMBRE_DIA[I18n.idioma][dayIdx];
  const hh = String(data.getHours()).padStart(2, "0");
  const mm = String(data.getMinutes()).padStart(2, "0");
  const dateKey = toDateKey(data.getFullYear(), data.getMonth(), data.getDate());

  return {
    id:            String(c.id),
    name:          c.nom,
    category:      detectaCategoria(c.nom),
    instructor:    "—",
    time:          `${hh}:${mm}`,
    duration:      "60 min",
    spots:         c.capacitat,
    dayName,
    dateKey,
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
  TOTES_LES_CLASSES = classes.map(classeBackendToLocal);
  const horari = {};
  TOTES_LES_CLASSES.forEach(c => {
    if (!horari[c.dayName]) horari[c.dayName] = [];
    horari[c.dayName].push(c);
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

// Fragment suggerit per assistent IA - revisar i adaptar
const NOMBRE_DIA = {
  ca: ["Diumenge","Dilluns","Dimarts","Dimecres","Dijous","Divendres","Dissabte"],
  en: ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
};

const NOMBRE_MES = {
  ca: ["Gener","Febrer","Març","Abril","Maig","Juny","Juliol","Agost","Setembre","Octubre","Novembre","Desembre"],
  en: ["January","February","March","April","May","June","July","August","September","October","November","December"]
};

// Caché de reserves per classe (evita crides repetides al backend)
const _reservesCache = {};

async function getReservesClasse(classeId) {
  if (_reservesCache[classeId] !== undefined) return _reservesCache[classeId];
  try {
    const res = await apiFetch(`/api/reserves/classe/${classeId}/count`);
    const count = res && res.ok ? await res.json() : 0;
    _reservesCache[classeId] = count;
    return count;
  } catch (e) { return 0; }
}

/* =====================================================
   2. ESTAT DE LA PÀGINA
   ===================================================== */

let calYear       = new Date().getFullYear();
let calMonth      = new Date().getMonth();
let selectedDate  = new Date();
let activeFilter  = "all";
let pendingClass  = null;
let pendingDateKey = null;


/* =====================================================
   3. CALENDARI
   ===================================================== */

function toDateKey(year, month, day) {
  const mm = String(month + 1).padStart(2, "0");
  const dd = String(day).padStart(2, "0");
  return `${year}-${mm}-${dd}`;
}

function getMaxDateKey() {
  const today   = new Date();
  const maxDate = new Date(today.getFullYear(), today.getMonth() + 2, today.getDate());
  return toDateKey(maxDate.getFullYear(), maxDate.getMonth(), maxDate.getDate());
}

function renderCalendar() {
  const grid     = document.getElementById("calendar-grid");
  const label    = document.getElementById("cal-month-label");
  const today    = new Date();
  const todayKey = toDateKey(today.getFullYear(), today.getMonth(), today.getDate());
  const selKey   = toDateKey(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate());
  const maxKey   = getMaxDateKey();

  // Fragment suggerit per assistent IA - revisar i adaptar
  label.textContent = `${NOMBRE_MES[I18n.idioma][calMonth]} ${calYear}`;

  const firstDayRaw = new Date(calYear, calMonth, 1).getDay();
  const firstDayLun = (firstDayRaw + 6) % 7;
  const daysInMonth = new Date(calYear, calMonth + 1, 0).getDate();

  let html = "";

  for (let i = 0; i < firstDayLun; i++) {
    html += `<div class="cal-day empty"></div>`;
  }

  for (let d = 1; d <= daysInMonth; d++) {
    const dateKey  = toDateKey(calYear, calMonth, d);
    const dateObj  = new Date(calYear, calMonth, d);
    const dayIndex = dateObj.getDay();

    const isPast     = dateKey < todayKey;
    const isSunday   = dayIndex === 0;
    const isTooFar   = dateKey > maxKey;
    const isToday    = dateKey === todayKey;
    const isSelected = dateKey === selKey;

    let classes = "cal-day";
    if (isToday)    classes += " today";
    if (isSelected) classes += " selected";
    if (isPast || isSunday || isTooFar) classes += " disabled";

    const hasClasses = !isSunday && !isTooFar &&
      TOTES_LES_CLASSES.some(c => c.dateKey === dateKey);
    const dot = hasClasses && !isPast ? `<span class="dot"></span>` : "";

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

// Fragment suggerit per assistent IA - revisar i adaptar
function updateSelectedDayInfo() {
  const info     = document.getElementById("selected-day-info");
  const dayIndex = selectedDate.getDay();
  const dayName  = NOMBRE_DIA[I18n.idioma][dayIndex];
  const dd       = selectedDate.getDate();
  const mmNom    = NOMBRE_MES[I18n.idioma][selectedDate.getMonth()];
  const yyyy     = selectedDate.getFullYear();
  const selKey   = toDateKey(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate());
  const prep     = I18n.idioma === "ca" ? "de" : "of";

  if (dayIndex === 0) {
    info.innerHTML = `
      <strong>${dayName} ${dd} ${prep} ${mmNom}</strong>
      ${I18n.idioma === "ca" ? "Sense activitats aquest dia" : "No activities this day"}
    `;
  } else {
    const clases = TOTES_LES_CLASSES.filter(c => c.dateKey === selKey);
    info.innerHTML = `
      <strong>${dayName} ${dd} ${prep} ${mmNom} ${prep} ${yyyy}</strong>
      ${clases.length} ${t("activitats.places")}
    `;
  }
}


/* =====================================================
   4. CLASSES — pintada de targetes
   ===================================================== */

async function renderClasesDelDia() {
  const container = document.getElementById("act-classes-list");
  const titleEl   = document.getElementById("col-day-title");
  const countEl   = document.getElementById("classes-count");

  const dayIndex = selectedDate.getDay();
  const dayName  = NOMBRE_DIA[I18n.idioma][dayIndex];
  const dd       = selectedDate.getDate();
  const mmNom    = NOMBRE_MES[I18n.idioma][selectedDate.getMonth()];
  const prep     = I18n.idioma === "ca" ? "de" : "of";

  titleEl.textContent = `${dayName} ${dd} ${prep} ${mmNom}`;

  if (dayIndex === 0) {
    container.innerHTML = `
      <div class="no-classes-msg">
        <strong>${I18n.idioma === "ca" ? "Sense activitats" : "No activities"}</strong>
        ${I18n.idioma === "ca" ? "Els diumenges no hi ha classes programades." : "No classes scheduled on Sundays."}
      </div>`;
    countEl.textContent = "";
    return;
  }

  const selDateKey = toDateKey(
    selectedDate.getFullYear(),
    selectedDate.getMonth(),
    selectedDate.getDate()
  );
  let clases = TOTES_LES_CLASSES.filter(c => c.dateKey === selDateKey);

  if (activeFilter !== "all") {
    clases = clases.filter(c => c.category === activeFilter);
  }

  if (clases.length === 0) {
    container.innerHTML = `
      <div class="no-classes-msg">
        <strong>${I18n.idioma === "ca" ? "Sense resultats" : "No results"}</strong>
        ${I18n.idioma === "ca"
          ? "No hi ha classes d'aquesta categoria per al dia seleccionat."
          : "No classes of this category for the selected day."}
      </div>`;
    countEl.textContent = "";
    return;
  }

  clases = clases.slice().sort((a, b) => a.time.localeCompare(b.time));
  // Fragment suggerit per assistent IA - revisar i adaptar
  countEl.textContent = `${clases.length} ${t("activitats.places")}`;

  // Buidem la caché per forçar dades fresques
  Object.keys(_reservesCache).forEach(k => delete _reservesCache[k]);

  const cards = await Promise.all(clases.map(async c => {
    const reservesReals = await getReservesClasse(c.id);
    return buildClassCard(c, reservesReals);
  }));
  container.innerHTML = cards.join("");
  }

function buildClassCard(clase, reservesReals) {
  const dateKey     = toDateKey(
    selectedDate.getFullYear(),
    selectedDate.getMonth(),
    selectedDate.getDate()
  );
  const reservaId   = `${dateKey}_${clase.id}`;
  const reservasHoy = (typeof reservesReals === "number") ? reservesReals : 0; // ← fix NaN
  const disponibles = clase.spots - reservasHoy;
  const pct         = clase.spots > 0 ? Math.round((reservasHoy / clase.spots) * 100) : 0;
  const estaLlena   = disponibles <= 0;
  const yaReservada = userHasReservation(reservaId);
  const imgSrc      = CATEGORY_IMAGES[clase.category] || "";

  let btnHtml;
  if (yaReservada) {
    btnHtml = `<button class="btn-reservar reservada" disabled>${t("activitats.reservada")}</button>`;
  } else if (estaLlena) {
    btnHtml = `<button class="btn-reservar completo" disabled>${t("activitats.plena")}</button>`;
  } else {
    btnHtml = `
      <button
        class="btn-reservar disponible"
        data-class-id="${clase.id}"
        data-date-key="${dateKey}"
      >
        ${t("activitats.reservar")}
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
            ${estaLlena
              ? (I18n.idioma === "ca" ? "Sense places" : "Full")
              : `${disponibles} / ${clase.spots} ${t("activitats.places")}`}
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
   5. MODAL DE CONFIRMACIÓ
   ===================================================== */

function openReservationModal(classId, dateKey) {
  if (!Auth.isLoggedIn()) {
    showToast(t("toast.sessionExp"), "error");
    setTimeout(() => { window.location.href = "login.html"; }, 1500);
    return;
  }

  const clase = TOTES_LES_CLASSES.find(c => String(c.id) === String(classId));
  if (!clase) {
    showToast(I18n.idioma === "ca" ? "No s'ha trobat la classe." : "Class not found.", "error");
    return;
  }

  pendingClass   = clase;
  pendingDateKey = dateKey;

  const msg = document.getElementById("modal-message");
  if (msg) {
    const prep = I18n.idioma === "ca" ? "el dia" : "on";
    const aLes = I18n.idioma === "ca" ? "a les" : "at";
    msg.innerHTML = I18n.idioma === "ca"
      ? `Vols reservar <strong>${clase.name}</strong> ${prep} <strong>${formatDateKey(dateKey)}</strong> ${aLes} <strong>${clase.time}</strong>?`
      : `Book <strong>${clase.name}</strong> ${prep} <strong>${formatDateKey(dateKey)}</strong> ${aLes} <strong>${clase.time}</strong>?`;
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
  if (dateKey.includes("-")) {
    const [y, m, d] = dateKey.split("-");
    return `${d}/${m}/${y}`;
  }
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
    showToast(t("toast.sessionExp"), "error");
    setTimeout(() => window.location.href = "login.html", 1500);
    return;
  }

  if (!user.tarifaId) {
        // Fragment suggerit per assistent IA - revisar i adaptar
    // Permetre reserves si té tarifa activa O si té tarifa cancel·lada però encara vigent
    const ara = new Date();
    const tarifaVigent = user.tarifaId && (
      !user.tarifaCancellada ||
      (user.tarifaDataFi && new Date(user.tarifaDataFi) > ara)
    );
    if (!tarifaVigent) {
      showToast(t("toast.sensePlanReserva"), "error");
      setTimeout(() => window.location.href = "tarifas.html", 2000);
      return;
    }
  }

  const btnConfirm = document.getElementById("modal-confirm-btn");
  if (btnConfirm) {
    btnConfirm.disabled = true;
    btnConfirm.textContent = t("activitats.carregantMsg");
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
    // Invalida la caché d'aquesta classe perquè el recompte es torna a demanar al backend
    delete _reservesCache[claseAReservar.id];

    // Re-sincronitza reserves des de la BD (font de veritat)
    await sincronitzaReserves();

    renderClasesDelDia();
    renderMisReservas();
    showToast(t("toast.reservaOk", [claseAReservar.name]), "success");
  }

  } catch (e) {
    console.error("Error confirmReservation:", e);
    closeModal();
    showToast(t("toast.reservaError"), "error");
  } finally {
    if (btnConfirm) {
      btnConfirm.disabled = false;
      btnConfirm.textContent = t("activitats.confirmar");
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
    // Fragment suggerit per assistent IA - revisar i adaptar
  // Caché de reserves per classe (evita crides repetides)
  const _reservesCache = {};

 async function getReservasDelDia(dateKey, classId) {
  if (_reservesCache[classId] !== undefined) return _reservesCache[classId];
  try {
    const res = await apiFetch(`/api/reserves/classe/${classId}/count`);
    const count = (res && res.ok) ? await res.json() : 0;
    _reservesCache[classId] = count;
    return count;
  } catch (e) {
    return 0;
  }
}
}

function userHasReservation(reservaId) {
  const totes = loadAllReservas();
  if (totes.some(r => r.reservaId === reservaId)) return true;
  const classeId = reservaId.split("_")[1];
  return totes.some(r => r.reservaId === `bd_${classeId}`);
}

async function cancelReservation(reservaId) {
  let totes = loadAllReservas();
  totes = totes.filter(r => r.reservaId !== reservaId);
  saveAllReservas(totes);
  renderClasesDelDia();
  renderMisReservas();
  showToast(t("toast.cancelReservaOk", [""]), "success");
}

/* =====================================================
   RENDER — Llista "Les meves reserves"
   ===================================================== */

// Fragment suggerit per assistent IA - revisar i adaptar
function renderMisReservas() {
  const lista = document.getElementById("my-reservations-list");
  if (!lista) return;

  const user = Auth.getUser();
  if (!Auth.isLoggedIn() || !user) {
    lista.innerHTML = `
      <p class="no-reservations">
        <a href="login.html">${t("nav.iniciSessio")}</a>
        ${I18n.idioma === "ca" ? "per veure les teves reserves" : "to see your bookings"}
      </p>`;
    return;
  }

  const totes = loadAllReservas();
  const avui  = toDateKey(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());

  const vigents = totes.filter(r =>
    r.dateKey >= avui && !r.reservaId.startsWith("bd_")
  );

  if (vigents.length !== totes.length) {
    saveAllReservas(totes.filter(r => !r.reservaId.startsWith("bd_") ? r.dateKey >= avui : true));
  }

  const misReservas = vigents.filter(r => r.userEmail === user.email);

  if (misReservas.length === 0) {
    lista.innerHTML = `<p class="no-reservations">
      ${I18n.idioma === "ca" ? "No tens cap reserva pròxima." : "You have no upcoming bookings."}
    </p>`;
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
        title="${I18n.idioma === "ca" ? "Cancel·lar reserva" : "Cancel booking"}"
        onclick="cancelReservation('${r.reservaId}')"
      >✕</button>
    </div>
  `).join("");
}

/* ── Sincronització BD → localStorage ── */

// Fragment suggerit per assistent IA - revisar i adaptar
// Sincronitza reserves: la BD és la font de veritat.
// Sobreescriu el localStorage completament en cada càrrega.
async function sincronitzaReserves() {
  const user = Auth.getUser();
  if (!user || !Auth.isLoggedIn()) {
    saveAllReservas([]); // usuari no loguejat → neteja
    return;
  }

  try {
    const reserves = await ApiUsuari.getReserves(user.id);

    // Si la BD no retorna res, netegem el localStorage
    if (!reserves || reserves.length === 0) {
      saveAllReservas([]);
      return;
    }

    // Construïm la llista EXCLUSIVAMENT des de la BD (no afegim res del localStorage)
    const reservesNoves = reserves.map(r => {
    const classe = TOTES_LES_CLASSES.find(c => String(c.id) === String(r.classeId));
    // Si no trobem la classe a TOTES_LES_CLASSES, usem la dataReserva com a fallback
    const dateKey = classe
      ? classe.dateKey
      : (r.dataReserva ? r.dataReserva.substring(0, 10).replace(/-/g, "") : "99991231");
    return {
      reservaId:  `bd_${r.classeId}`,
      classId:    String(r.classeId),
      dateKey,
      className:  classe ? classe.name     : `Classe #${r.classeId}`,
      category:   classe ? classe.category : "funcional",
      time:       classe ? classe.time     : "—",
      instructor: "—",
      userEmail:  user.email
    };
  });

    // Sobreescrivim completament — elimina automàticament reserves obsoletes
    saveAllReservas(reservesNoves);

  } catch (e) {
    console.error("Error sincronitzant reserves:", e);
    // En cas d'error de xarxa, NO toquem el localStorage per no perdre dades
  }
}


/* =====================================================
   7. ARRENCADA — càrrega asíncrona des del backend
   ===================================================== */

document.addEventListener("DOMContentLoaded", async function () {

  if (!document.getElementById("calendar-grid")) return;

  const container = document.getElementById("act-classes-list");
  if (container) {
    container.innerHTML = `
      <div style="text-align:center;padding:3rem;color:var(--text-muted)">
        ${t("activitats.carregant")}
      </div>`;
  }

  const classes = await ApiClasses.getAll();
  HORARIO_SEMANAL = construeixHorari(classes);

  await sincronitzaReserves();

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

  // Navegació del calendari
  document.getElementById("cal-prev").addEventListener("click", function () {
    calMonth--;
    if (calMonth < 0) { calMonth = 11; calYear--; }
    renderCalendar();
  });

  document.getElementById("cal-next").addEventListener("click", function () {
    const today    = new Date();
    const maxYear  = today.getMonth() >= 10 ? today.getFullYear() + 1 : today.getFullYear();
    const maxMonth = (today.getMonth() + 2) % 12;

    if (calYear === maxYear && calMonth === maxMonth) {
      showToast(
        I18n.idioma === "ca"
          ? "No pots veure classes més enllà de 2 mesos."
          : "Cannot view classes beyond 2 months.",
        "error"
      );
      return;
    }

    calMonth++;
    if (calMonth > 11) { calMonth = 0; calYear++; }
    renderCalendar();
  });

  // Filtres de categoria
  document.querySelectorAll("#act-filters .filter-tab").forEach(function (btn) {
    btn.addEventListener("click", function () {
      document.querySelectorAll("#act-filters .filter-tab").forEach(b => b.classList.remove("active"));
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

  // Re-renderitza quan canvia l'idioma
  // Fragment suggerit per assistent IA - revisar i adaptar
  document.addEventListener("idioma:canvi", function () {
    renderCalendar();
    updateSelectedDayInfo();
    renderClasesDelDia();
    renderMisReservas();
  });
});