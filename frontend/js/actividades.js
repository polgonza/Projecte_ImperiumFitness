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
const HORARIO_SEMANAL = {
  "Lunes": [
    { id: "lun-1", name: "CrossFit Matutino",   category: "crossfit",  instructor: "Carlos Martínez",  time: "08:00", duration: "60 min", spots: 12 },
    { id: "lun-2", name: "Yoga Flow",            category: "yoga",      instructor: "Laura Sánchez",    time: "10:00", duration: "75 min", spots: 15 },
    { id: "lun-3", name: "HIIT Express",         category: "hiit",      instructor: "Ana García",       time: "13:00", duration: "30 min", spots: 10 },
    { id: "lun-4", name: "Funcional Total",      category: "funcional", instructor: "David Torres",     time: "18:00", duration: "60 min", spots: 14 },
    { id: "lun-5", name: "Spinning Power",       category: "spinning",  instructor: "Miguel Ángel",     time: "20:00", duration: "45 min", spots: 20 }
  ],
  "Martes": [
    { id: "mar-1", name: "Boxeo Técnico",        category: "boxeo",     instructor: "Javier Ramos",     time: "08:00", duration: "60 min", spots: 10 },
    { id: "mar-2", name: "Pilates Core",         category: "pilates",   instructor: "Elena Ruiz",       time: "10:00", duration: "60 min", spots: 12 },
    { id: "mar-3", name: "HIIT Tabata",          category: "hiit",      instructor: "Ana García",       time: "12:00", duration: "30 min", spots: 10 },
    { id: "mar-4", name: "CrossFit WOD",         category: "crossfit",  instructor: "Carlos Martínez",  time: "18:30", duration: "60 min", spots: 12 },
    { id: "mar-5", name: "Yoga Nocturno",        category: "yoga",      instructor: "Laura Sánchez",    time: "20:00", duration: "60 min", spots: 15 }
  ],
  "Miércoles": [
    { id: "mie-1", name: "Funcional Avanzado",   category: "funcional", instructor: "David Torres",     time: "08:00", duration: "60 min", spots: 14 },
    { id: "mie-2", name: "Spinning Endurance",   category: "spinning",  instructor: "Miguel Ángel",     time: "10:00", duration: "50 min", spots: 20 },
    { id: "mie-3", name: "Boxeo Fitness",        category: "boxeo",     instructor: "Javier Ramos",     time: "13:00", duration: "45 min", spots: 10 },
    { id: "mie-4", name: "Pilates Avanzado",     category: "pilates",   instructor: "Elena Ruiz",       time: "17:00", duration: "60 min", spots: 12 },
    { id: "mie-5", name: "CrossFit Noche",       category: "crossfit",  instructor: "Carlos Martínez",  time: "20:00", duration: "60 min", spots: 12 }
  ],
  "Jueves": [
    { id: "jue-1", name: "Yoga Restaurativo",    category: "yoga",      instructor: "Laura Sánchez",    time: "07:00", duration: "60 min", spots: 15 },
    { id: "jue-2", name: "HIIT Extremo",         category: "hiit",      instructor: "Ana García",       time: "09:00", duration: "45 min", spots: 10 },
    { id: "jue-3", name: "Funcional Cardio",     category: "funcional", instructor: "David Torres",     time: "12:00", duration: "45 min", spots: 14 },
    { id: "jue-4", name: "Spinning Power",       category: "spinning",  instructor: "Miguel Ángel",     time: "18:00", duration: "50 min", spots: 20 },
    { id: "jue-5", name: "Boxeo Avanzado",       category: "boxeo",     instructor: "Javier Ramos",     time: "20:00", duration: "60 min", spots: 10 }
  ],
  "Viernes": [
    { id: "vie-1", name: "CrossFit Intenso",     category: "crossfit",  instructor: "Carlos Martínez",  time: "08:00", duration: "60 min", spots: 12 },
    { id: "vie-2", name: "Pilates Total Body",   category: "pilates",   instructor: "Elena Ruiz",       time: "10:00", duration: "60 min", spots: 12 },
    { id: "vie-3", name: "HIIT Final Week",      category: "hiit",      instructor: "Ana García",       time: "13:00", duration: "30 min", spots: 10 },
    { id: "vie-4", name: "Yoga Flow Viernes",    category: "yoga",      instructor: "Laura Sánchez",    time: "17:00", duration: "75 min", spots: 15 },
    { id: "vie-5", name: "Funcional Weekend",    category: "funcional", instructor: "David Torres",     time: "19:00", duration: "60 min", spots: 14 }
  ],
  "Sábado": [
    { id: "sab-1", name: "CrossFit Matinal",     category: "crossfit",  instructor: "Carlos Martínez",  time: "09:00", duration: "60 min", spots: 12 },
    { id: "sab-2", name: "Spinning Weekend",     category: "spinning",  instructor: "Miguel Ángel",     time: "10:30", duration: "45 min", spots: 20 },
    { id: "sab-3", name: "Yoga Relajante",       category: "yoga",      instructor: "Laura Sánchez",    time: "12:00", duration: "75 min", spots: 15 },
    { id: "sab-4", name: "Boxeo Weekend",        category: "boxeo",     instructor: "Javier Ramos",     time: "11:00", duration: "60 min", spots: 10 }
  ]
  // Domingo: sin actividades (bloqueado en el calendario)
};

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
  const dateKey    = toDateKey(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate());
  const reservaId  = `${dateKey}_${clase.id}`;    // clave única de reserva

  // Plazas ya reservadas = número de reservas guardadas para esta clase hoy
  const reservasHoy    = getReservasDelDia(dateKey, clase.id);
  const plazasOcupadas = reservasHoy;
  const disponibles    = clase.spots - plazasOcupadas;
  const pct            = Math.round((plazasOcupadas / clase.spots) * 100);
  const estaLlena      = disponibles <= 0;

  // ¿El usuario actual ya reservó esta clase?
  const yaReservada    = userHasReservation(reservaId);

  // Imagen de la categoría
  const imgSrc = CATEGORY_IMAGES[clase.category] || "";

  // Botón: 3 estados posibles
  let btnHtml;
  if (yaReservada) {
    btnHtml = `<button class="btn-reservar reservada" disabled>Reservada</button>`;
  } else if (estaLlena) {
    btnHtml = `<button class="btn-reservar completo" disabled>Completo</button>`;
  } else {
    // data-* almacena la info necesaria para abrir el modal
    btnHtml = `
      <button
        class="btn-reservar disponible"
        data-class-id="${clase.id}"
        data-date-key="${dateKey}"
        onclick="openReservationModal('${clase.id}', '${dateKey}')"
      >
        Reservar
      </button>`;
  }

  return `
    <div class="act-card">
      <!-- Banda de color de la categoría -->
      <div class="act-card-color ${clase.category}"></div>

      <!-- Imagen de la categoría (Unsplash) -->
      <img
        class="act-card-img"
        src="${imgSrc}"
        alt="${clase.category}"
        loading="lazy"
        onerror="this.style.display='none'"
      >

      <!-- Cuerpo de la tarjeta -->
      <div class="act-card-body">
        <div class="act-card-top">
          <span class="act-card-name">${clase.name}</span>
          <span class="act-card-time">${clase.time}</span>
        </div>

        <div class="act-card-meta">
          <span>Instructor: ${clase.instructor}</span>
          <span>${clase.duration}</span>
        </div>

        <!-- Barra de plazas -->
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

        <!-- Pie: tag + botón -->
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

/* Abre el modal con los datos de la clase */
function openReservationModal(classId, dateKey) {
  const user = Auth.getUser();

  // Si no hay sesión: redirige al login con mensaje
  if (!user) {
    showToast("Debes iniciar sesion para reservar clases.", "error");
    setTimeout(() => { window.location.href = "login.html"; }, 1500);
    return;
  }

  // Busca la clase en el horario del día
  const dayIndex = selectedDate.getDay();
  const dayName  = NOMBRE_DIA[dayIndex];
  const clases   = HORARIO_SEMANAL[dayName] || [];
  const clase    = clases.find(c => c.id === classId);

  if (!clase) return;

  // Guarda la clase pendiente en las variables de estado
  pendingClass   = clase;
  pendingDateKey = dateKey;

  // Rellena el mensaje del modal
  const msg = document.getElementById("modal-message");
  msg.innerHTML = `
    Quieres reservar <strong>${clase.name}</strong>
    el dia <strong>${formatDateKey(dateKey)}</strong>
    a las <strong>${clase.time}</strong>?
  `;

  // Muestra el modal añadiendo la clase "open"
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
   6. SISTEMA DE RESERVAS (localStorage)
   ===================================================== */

/*
  CLAVE EN localStorage: "imperium_reservas"
  Estructura guardada: array de objetos
  {
    reservaId:  "2026-04-14_lun-1",   // dateKey + "_" + classId
    classId:    "lun-1",
    dateKey:    "2026-04-14",
    className:  "CrossFit Matutino",
    time:       "08:00",
    instructor: "Carlos Martínez",
    userEmail:  "usuario@email.com"
  }
*/

/* Carga TODAS las reservas guardadas */
function loadAllReservas() {
  const data = localStorage.getItem("imperium_reservas");
  return data ? JSON.parse(data) : [];
}

/* Guarda el array de reservas actualizado */
function saveAllReservas(reservas) {
  localStorage.setItem("imperium_reservas", JSON.stringify(reservas));
}

/* Cuántas reservas hay para esta clase en este día (de cualquier usuario) */
function getReservasDelDia(dateKey, classId) {
  const todas = loadAllReservas();
  return todas.filter(r => r.dateKey === dateKey && r.classId === classId).length;
}

/* ¿El usuario actual ya tiene reservada esta clase? */
function userHasReservation(reservaId) {
  const user = Auth.getUser();
  if (!user) return false;
  const todas = loadAllReservas();
  return todas.some(r => r.reservaId === reservaId && r.userEmail === user.email);
}

/* Añade una nueva reserva */
function addReservation(clase, dateKey) {
  const user     = Auth.getUser();
  const reservaId = `${dateKey}_${clase.id}`;

  const nueva = {
    reservaId,
    classId:    clase.id,
    dateKey,
    className:  clase.name,
    category:   clase.category,
    time:       clase.time,
    instructor: clase.instructor,
    userEmail:  user.email
  };

  const todas = loadAllReservas();
  todas.push(nueva);
  saveAllReservas(todas);
}

/* Cancela una reserva (la elimina del array) */
function cancelReservation(reservaId) {
  const user  = Auth.getUser();
  if (!user) return;

  let todas = loadAllReservas();
  // Filtra: conserva todas excepto la del usuario con ese id
  todas = todas.filter(
    r => !(r.reservaId === reservaId && r.userEmail === user.email)
  );
  saveAllReservas(todas);

  // Refresca la UI
  renderClasesDelDia();
  renderMisReservas();
  showToast("Reserva cancelada correctamente.", "success");
}

/* Confirma la reserva pendiente (llamada desde el botón del modal) */
function confirmReservation() {
  if (!pendingClass || !pendingDateKey) return;

  addReservation(pendingClass, pendingDateKey);

  // Cierra el modal
  closeModal();

  // Refresca tarjetas y lista de reservas
  renderClasesDelDia();
  renderMisReservas();

  showToast(`Reserva de "${pendingClass.name}" confirmada.`, "success");
}


/* =====================================================
   RENDER — Lista "Mis Reservas" (columna izquierda)
   ===================================================== */

function renderMisReservas() {
  const lista    = document.getElementById("my-reservations-list");
  const user     = Auth.getUser();

  // Si no hay usuario logueado
  if (!user) {
    lista.innerHTML = `
      <p class="no-reservations">
        <a href="login.html">Inicia sesion</a> para ver tus reservas
      </p>`;
    return;
  }

  // Filtra solo las reservas del usuario actual
  const todas    = loadAllReservas();
  const misReservas = todas.filter(r => r.userEmail === user.email);

  if (misReservas.length === 0) {
    lista.innerHTML = `<p class="no-reservations">No tienes ninguna reserva aun.</p>`;
    return;
  }

  // Ordena por fecha y hora
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
      <!-- Botón cancelar: llama a cancelReservation con el id -->
      <button
        class="btn-cancel-res"
        title="Cancelar reserva"
        onclick="cancelReservation('${r.reservaId}')"
      >X</button>
    </div>
  `).join("");
}


/* =====================================================
   7. ARRANQUE (se ejecuta al cargar la pagina)
   ===================================================== */

document.addEventListener("DOMContentLoaded", function () {

  /* Solo ejecutamos este código si estamos en actividades.html */
  if (!document.getElementById("calendar-grid")) return;

  /* --- Dibuja el calendario con el mes actual --- */
  renderCalendar();

  /* --- Selecciona el día de hoy por defecto --- */
  updateSelectedDayInfo();
  renderClasesDelDia();

  /* --- Mis reservas --- */
  renderMisReservas();

  /* --- Botones de navegación del calendario (mes anterior/siguiente) --- */
  document.getElementById("cal-prev").addEventListener("click", function () {
    calMonth--;
    if (calMonth < 0) {
      calMonth = 11;
      calYear--;
    }
    renderCalendar();
  });

  document.getElementById("cal-next").addEventListener("click", function () {
    calMonth++;
    if (calMonth > 11) {
      calMonth = 0;
      calYear++;
    }
    renderCalendar();
  });

  /* --- Filtros de categoría --- */
  document.querySelectorAll("#act-filters .filter-tab").forEach(function (btn) {
    btn.addEventListener("click", function () {
      // Quita el estado activo de todos
      document.querySelectorAll("#act-filters .filter-tab").forEach(b => b.classList.remove("active"));
      // Activa el clicado
      this.classList.add("active");
      // Guarda el filtro y re-pinta
      activeFilter = this.dataset.filter;
      renderClasesDelDia();
    });
  });

  /* --- Modal: botón Cancelar --- */
  document.getElementById("modal-cancel-btn").addEventListener("click", closeModal);

  /* --- Modal: botón Confirmar --- */
  document.getElementById("modal-confirm-btn").addEventListener("click", confirmReservation);

  /* --- Modal: clic en el fondo oscuro cierra el modal --- */
  document.getElementById("reservation-modal").addEventListener("click", function (e) {
    // Solo cierra si el clic fue en el overlay, no en el modal-box
    if (e.target === this) closeModal();
  });

  /* --- Tecla Escape cierra el modal --- */
  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape") closeModal();
  });

});
