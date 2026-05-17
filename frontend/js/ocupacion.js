/* =====================================================
   ocupacion.js — Lógica de la página de ocupación
   IMPERIUM FITNESS

   Depende de:
     - ocupacion_data.js  (OCUPACION_STATS, HORAS_LABELS,
                           DIAS_SEMANA, getOcupacionHoy,
                           getOcupacionAhora)
     - app.js             (GYMS — datos de nombre y capacidad)
     - api.js             (SessionManager)
   ===================================================== */

/*
  Vincula cada gymKey con el objeto GYMS de app.js.
  El orden sigue el mismo orden del array GYMS.
*/
const GYM_KEYS = ["gym1","gym2","gym3","gym4","gym5"];

/* Estado de la página */
let vistaActual  = "cards";
let filtroActual = "";


/* ─────────────────────────────────────────
   UTILIDADES
   ───────────────────────────────────────── */

function nivelOcupacion(pct) {
  if (pct < 40) return "low";
  if (pct < 70) return "medium";
  return "high";
}

function textoNivel(pct) {
  if (pct < 40) return t("ocupacio.baixa").split(" ")[0];
  if (pct < 70) return I18n.idioma === "ca" ? "Mitja" : "Medium";
  return I18n.idioma === "ca" ? "Alta" : "High";
}

/* Índice de la hora actual en el array de 17 valores */
function horaIdx() {
  const h = new Date().getHours();
  return Math.max(0, Math.min(16, h - 6));
}

/* Predicción: valor de la próxima hora */
function prediccion(gymKey) {
  const arr  = getOcupacionHoy(gymKey);
  const idx  = horaIdx();
  const next = arr[Math.min(idx + 1, 16)];
  const curr = arr[idx];
  return { pct: next, subiendo: next > curr };
}


/* ─────────────────────────────────────────
   RELOJ
   ───────────────────────────────────────── */

function actualizarReloj() {
  const el = document.getElementById("occ-time-label");
  if (!el) return;
  const now  = new Date();
  const dies = {
    ca: ["Diumenge","Dilluns","Dimarts","Dimecres","Dijous","Divendres","Dissabte"],
    en: ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
  };
  const hh = String(now.getHours()).padStart(2,"0");
  const mm = String(now.getMinutes()).padStart(2,"0");
  el.textContent = `${dies[I18n.idioma][now.getDay()]} ${hh}:${mm}`;
}


/* ─────────────────────────────────────────
   KPIs DEL HERO
   ───────────────────────────────────────── */

function actualizarKPIs() {
  const hoy = DIAS_SEMANA[new Date().getDay()];
  const idx = horaIdx();

  /* 1. Mejor hora hoy: menor media de ocupación */
  let menorMedia = 999, mejorIdx = 0;
  for (let i = 0; i < 17; i++) {
    const media = GYM_KEYS.reduce((sum, key) => {
      const arr = OCUPACION_STATS[key]?.horario[hoy] || [];
      return sum + (arr[i] || 0);
    }, 0) / GYM_KEYS.length;
    if (media < menorMedia) { menorMedia = media; mejorIdx = i; }
  }
  const mejorHora  = mejorIdx + 6;
  const mejorLabel = `${String(mejorHora).padStart(2,"0")}:00 - ${String(mejorHora+1).padStart(2,"0")}:00`;
  setTxt("kpi-best-hour", mejorLabel);
  setTxt("kpi-best-sub",  `${I18n.idioma === "ca" ? "Ocupació estimada" : "Estimated occupancy"}: ${Math.round(menorMedia)}%`);

  /* 2. Gimnasio menos lleno ahora */
  let minPct = 999, minIdx = 0;
  GYM_KEYS.forEach((key, i) => {
    const pct = getOcupacionAhora(key);
    if (pct < minPct) { minPct = pct; minIdx = i; }
  });
  setTxt("kpi-least-busy", GYMS[minIdx]?.name || "—");
  setTxt("kpi-least-sub", `${I18n.idioma === "ca" ? "Només al" : "Only at"} ${minPct}% ${I18n.idioma === "ca" ? "de capacitat" : "capacity"}`);

  /* 3. Cuántos centros están subiendo */
  const subiendo = GYM_KEYS.filter(k => prediccion(k).subiendo).length;
  setTxt("kpi-prediction", `${subiendo} ${I18n.idioma === "ca" ? `centre${subiendo !== 1 ? "s" : ""} pujant` : `centre${subiendo !== 1 ? "s" : ""} rising`}`);
}
function setTxt(id, txt) {
  const el = document.getElementById(id);
  if (el) el.textContent = txt;
}


/* ─────────────────────────────────────────
   RENDER DE CARDS
   ───────────────────────────────────────── */

function renderCards(filtro) {
  const grid = document.getElementById("occ-cards-grid");
  if (!grid) return;

  const lista = GYMS.filter(g =>
    !filtro || g.name.toLowerCase().includes(filtro.toLowerCase())
  );

  if (lista.length === 0) {
    grid.innerHTML = `<p style="color:var(--text-muted);grid-column:1/-1;text-align:center;padding:3rem">
      ${I18n.idioma === "ca" ? "No s'han trobat gimnasos amb aquest nom." : "No gyms found with that name."}
    </p>`;
    return;
  }

  grid.innerHTML = lista.map((gym, i) => buildCard(gym, GYM_KEYS[i])).join("");
}

function buildCard(gym, gymKey) {
  const pct      = getOcupacionAhora(gymKey);
  const nivel    = nivelOcupacion(pct);
  const personas = Math.round((pct / 100) * gym.capacity);
  const pred     = prediccion(gymKey);
  const predIcon = pred.subiendo ? "↑" : "↓";
  const predCls  = pred.subiendo ? "pred-up" : "pred-down";

  /* Donut SVG */
  const r      = 30;
  const c      = 2 * Math.PI * r;
  const offset = c - (pct / 100) * c;

  /* Barras horarias */
  const arrHoy    = getOcupacionHoy(gymKey);
  const idxActual = horaIdx();

  const barrasHorarias = arrHoy.map((v, i) => {
    const cls  = i < idxActual ? "past" : i === idxActual ? "current" : "future";
    const hora = i % 3 === 0 ? `${String(i + 6).padStart(2,"0")}:00` : "";
    return `
      <div class="occ-bar-col">
        <div class="occ-bar ${cls}" style="height:${Math.max(4, v * 0.7)}%"></div>
        <span class="occ-bar-hour">${hora}</span>
      </div>`;
  }).join("");

  /* Barras por día */
  const hoyDiaIdx = new Date().getDay();
  const barrasDias = DIAS_SEMANA.map((dia, i) => {
    const labels = {
  ca: ["Dg","Dl","Dt","Dc","Dj","Dv","Ds"],
  en: ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"]
};
    if (dia === "domingo") {
      return `<div class="occ-day-col">
        <div class="occ-day-bar other" style="height:4%"></div>
        <span class="occ-day-label">${labels[i]}</span>
      </div>`;
    }
    const arr = OCUPACION_STATS[gymKey]?.horario[dia] || [];
    const med = arr.length ? Math.round(arr.reduce((a,b) => a+b,0) / arr.length) : 0;
    const esHoy = i === hoyDiaIdx;
    return `
      <div class="occ-day-col">
        <div class="occ-day-bar ${esHoy ? "today" : "other"}"
             style="height:${Math.max(5, med)}%"></div>
        <span class="occ-day-label">${labels[i]}</span>
      </div>`;
  }).join("");

  return `
    <div class="occ-card" id="card-${gymKey}">
      <div class="occ-card-stripe ${nivel}"></div>
      <div class="occ-card-body">

        <div class="occ-card-header">
          <div class="occ-card-name">${gym.name}</div>
          <span class="occ-level-badge ${nivel}">${textoNivel(pct)}</span>
        </div>
        <div class="occ-card-address">📍 ${gym.location}</div>

        <div class="occ-card-main">
          <!-- Donut -->
          <div class="occ-donut-wrap">
            <svg class="occ-donut-svg" viewBox="0 0 80 80">
              <circle class="occ-donut-bg"              cx="40" cy="40" r="${r}"/>
              <circle class="occ-donut-fill ${nivel}"   cx="40" cy="40" r="${r}"
                stroke-dasharray="${c.toFixed(1)}"
                stroke-dashoffset="${offset.toFixed(1)}"/>
            </svg>
            <div class="occ-donut-label ${nivel}">${pct}%</div>
          </div>

          <!-- Stats -->
          <div class="occ-card-stats">
            <div class="occ-stat-row">
              <span class="occ-stat-label">👤 ${I18n.idioma === "ca" ? "Persones" : "People"}</span>
              <span class="occ-stat-value">${personas}</span>
            </div>
            <div class="occ-stat-row">
              <span class="occ-stat-label">⚡ ${I18n.idioma === "ca" ? "Capacitat" : "Capacity"}</span>
              <span class="occ-stat-value">${gym.capacity}</span>
            </div>
            <div class="occ-stat-row">
              <span class="occ-stat-label">🕐 ${I18n.idioma === "ca" ? "En 30 min" : "In 30 min"}</span>
              <span class="occ-stat-value ${predCls}">${predIcon} ~${pred.pct}%</span>
            </div>
          </div>
        </div>

      </div>

      <!-- Barra base -->
      <div class="occ-card-bar">
        <div class="occ-card-bar-fill ${nivel}" style="width:${pct}%"></div>
      </div>

      <!-- Botón expandir -->
      <button class="occ-card-toggle" onclick="toggleCharts('${gymKey}', this)">
  📊 ${I18n.idioma === "ca" ? "Veure gràfiques ∨" : "View charts ∨"}
</button>

      <!-- Panel gráficas -->
      <div class="occ-charts-panel" id="charts-${gymKey}">
        <div class="occ-chart-title">
  ${I18n.idioma === "ca" ? "OCUPACIÓ AVUI PER HORES" : "TODAY'S OCCUPANCY BY HOUR"}
</div>
        <div class="occ-bar-chart">${barrasHorarias}</div>
        <div class="occ-chart-title">
  ${I18n.idioma === "ca" ? "MITJANA ÚLTIMS 7 DIES" : "AVERAGE LAST 7 DAYS"}
</div>

        <div class="occ-day-chart">${barrasDias}</div>
      </div>
    </div>
  `;
}

function toggleCharts(gymKey, btn) {
  const panel = document.getElementById(`charts-${gymKey}`);
  if (!panel) return;
  const isOpen = panel.classList.toggle("open");
  btn.textContent = isOpen 
  ? `📊 ${I18n.idioma === "ca" ? "Ocultar gràfiques ∧" : "Hide charts ∧"}` 
  : `📊 ${I18n.idioma === "ca" ? "Veure gràfiques ∨" : "View charts ∨"}`;
}


/* ─────────────────────────────────────────
   FILTRO Y VISTA
   ───────────────────────────────────────── */

function filtrarGimnasios(valor) {
  filtroActual = valor;
  renderCards(valor);
}

function setVista(vista) {
  vistaActual = vista;
  const gridEl = document.getElementById("occ-cards-grid");
  const mapaEl = document.getElementById("occ-map-view");
  if (gridEl) gridEl.style.display = vista === "cards" ? "" : "none";
  if (mapaEl) mapaEl.style.display = vista === "mapa"  ? "" : "none";
  document.getElementById("btn-cards")?.classList.toggle("active", vista === "cards");
  document.getElementById("btn-mapa")?.classList.toggle("active",  vista === "mapa");
  if (vista === "mapa") renderMapa();
}

function renderMapa() {
  const container = document.getElementById("occ-map-pins");
  if (!container) return;
  container.innerHTML = GYMS.map((gym, i) => {
    const pct   = getOcupacionAhora(GYM_KEYS[i]);
    const nivel = nivelOcupacion(pct);
    return `
      <div class="occ-map-pin">
        <span class="occ-map-pin-dot ${nivel}"></span>
        <span>${gym.name} — <strong>${pct}%</strong></span>
      </div>`;
  }).join("");
}


/* ─────────────────────────────────────────
   REFRESCO AUTOMÁTICO
   ───────────────────────────────────────── */

function actualizarTodo() {
  actualizarReloj();
  actualizarKPIs();
  if (vistaActual === "cards") renderCards(filtroActual);
  if (vistaActual === "mapa")  renderMapa();
}


/* ─────────────────────────────────────────
   ARRANQUE
   ───────────────────────────────────────── */

document.addEventListener("DOMContentLoaded", function() {
  if (!document.getElementById("occ-cards-grid")) return;

  SessionManager.init(false);

  actualizarTodo();
  setInterval(actualizarTodo, 30000);  /* refresco cada 30 s */
  setInterval(actualizarReloj, 60000); /* reloj cada minuto  */
  document.addEventListener("idioma:canvi", function() {
    actualizarTodo();
  });
});
