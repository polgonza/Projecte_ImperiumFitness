/* =====================================================
   IMPERIUM FITNESS — perfil.js
   Connectat al backend real via api.js
   ===================================================== */


/* =====================================================
   1. INICIALITZACIÓ — càrrega de dades reals
   ===================================================== */

async function initPerfil() {
  if (!Auth.isLoggedIn()) { window.location.href = "login.html"; return; }
  const user = Auth.getUser();
  if (!user) { window.location.href = "login.html"; return; }

  const inicial = user.name ? user.name.charAt(0).toUpperCase() : "?";
  renderDatosUsuario(inicial, user.name || "—", user.email || "—", t("perfil.sensePla"), "—");

  const perfil = await ApiUsuari.getPerfil(user.id);
  if (!perfil) return;

  const dataRegistre = perfil.dataRegistre
    ? new Date(perfil.dataRegistre).toLocaleDateString(I18n.idioma === "ca" ? "ca-ES" : "en-GB")
    : "—";

  const planNom = perfil.tarifaNom || t("perfil.sensePla");
  renderDatosUsuario(
    perfil.nom.charAt(0).toUpperCase(),
    perfil.nom,
    perfil.email,
    planNom,
    dataRegistre
  );

  if (perfil.tarifaId) {
    const dataFi = perfil.tarifaDataFi
      ? new Date(perfil.tarifaDataFi).toLocaleDateString(I18n.idioma === "ca" ? "ca-ES" : "en-GB")
      : "—";
    const cancelBtn = document.getElementById("cancel-tarifa-btn");
    if (cancelBtn) {
      cancelBtn.style.display = "block";
      cancelBtn.textContent = perfil.tarifaCancellada
        ? (I18n.idioma === "ca" ? `Cancel·lada (accés fins al ${dataFi})` : `Cancelled (access until ${dataFi})`)
        : (I18n.idioma === "ca" ? `Cancelar subscripció (activa fins al ${dataFi})` : `Cancel subscription (active until ${dataFi})`);
      cancelBtn.disabled = perfil.tarifaCancellada;
    }
  }

  Auth.setUser({ ...user, name: perfil.nom, email: perfil.email, tarifaId: perfil.tarifaId, tarifaNom: perfil.tarifaNom });

  const noPlanMsg = document.getElementById("no-plan-msg");
  if (noPlanMsg) noPlanMsg.style.display = perfil.tarifaId ? "none" : "block";
}

function setTextById(id, text) {
  const el = document.getElementById(id);
  if (el) el.textContent = text;
}

async function cancelarSubscripcio() {
  const confirmat = confirm(t("perfil.confirmCancel"));
  if (!confirmat) return;

  const user   = Auth.getUser();
  const perfil = await ApiUsuari.cancelarTarifa(user.id);

  if (perfil) {
    const dataFi = perfil.tarifaDataFi
      ? new Date(perfil.tarifaDataFi).toLocaleDateString(I18n.idioma === "ca" ? "ca-ES" : "en-GB")
      : "—";
    showToast(t("perfil.cancelOk", [dataFi]), "success");
    await initPerfil();
  } else {
    showToast(t("perfil.cancelError"), "error");
  }
}


/* =====================================================
   2. TAB RESERVES — dades reals del backend
   ===================================================== */

async function renderReservationsInProfile() {
  const lista = document.getElementById("reservations-list");
  if (!lista) return;

  const user = Auth.getUser();
  if (!user) return;

  lista.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">
    ⏳ ${t("activitats.carregant")}</p>`;

  const reserves = await ApiUsuari.getReserves(user.id);

  if (!reserves || reserves.length === 0) {
    lista.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">
      ${I18n.idioma === "ca" ? "No tens reserves de classes." : "You have no class bookings."}
      <br><a href="actividades.html" style="color:var(--primary)">
        ${I18n.idioma === "ca" ? "Veure classes disponibles" : "View available classes"}
      </a></p>`;
    return;
  }

  const ara = new Date();
  ara.setHours(0, 0, 0, 0);

  const reservesFutures = reserves.filter(r => {
    if (!r.dataReserva) return true;
    const d = new Date(r.dataReserva);
    d.setHours(0, 0, 0, 0);
    return d >= ara;
  });

  if (reservesFutures.length === 0) {
    lista.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">
      ${I18n.idioma === "ca" ? "No tens reserves pròximes." : "You have no upcoming bookings."}
      <br><a href="actividades.html" style="color:var(--primary)">
        ${I18n.idioma === "ca" ? "Reservar una classe" : "Book a class"}
      </a></p>`;
    return;
  }

  reservesFutures.sort((a, b) => new Date(a.dataReserva) - new Date(b.dataReserva));

  const classesCache = {};
  const reservesAmbNom = await Promise.all(reservesFutures.map(async r => {
    let nomClasse = `${I18n.idioma === "ca" ? "Classe" : "Class"} #${r.classeId}`;
    try {
      if (!classesCache[r.classeId]) {
        const res = await apiFetch(`/api/classes/${r.classeId}`);
        if (res && res.ok) classesCache[r.classeId] = (await res.json()).nom;
      }
      nomClasse = classesCache[r.classeId] || nomClasse;
    } catch (e) {}
    return {
      classeId:    r.classeId,
      nomClasse,
      dataReserva: r.dataReserva
        ? new Date(r.dataReserva).toLocaleDateString(I18n.idioma === "ca" ? "ca-ES" : "en-GB")
        : "—"
    };
  }));

  renderReservasPerfil(reservesAmbNom);
}


/* =====================================================
   3. TAB PEDIDOS — vendes reals del backend
   ===================================================== */

async function renderOrderHistory() {
  const container = document.getElementById("orders-list");
  if (!container) return;

  container.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">
    ⏳ ${I18n.idioma === "ca" ? "Carregant pedidos..." : "Loading orders..."}</p>`;

  const user = Auth.getUser();
  if (!user) return;

  const vendes = await ApiUsuari.getVendes(user.id);

  if (!vendes || vendes.length === 0) {
    container.innerHTML = `<div style="color:var(--text-muted);text-align:center;padding:2rem">
      ${I18n.idioma === "ca" ? "Encara no has fet cap comanda." : "You haven't placed any orders yet."}
      <br><a href="tienda.html" style="color:var(--primary)">
        ${I18n.idioma === "ca" ? "Anar a la botiga" : "Go to the shop"}
      </a></div>`;
    return;
  }

  const productesCache = {};
  const vendesAmbNom = await Promise.all(vendes.map(async v => {
    let nomProducte = `${I18n.idioma === "ca" ? "Producte" : "Product"} #${v.producteId}`;
    try {
      if (!productesCache[v.producteId]) {
        const res = await apiFetch(`/api/productes/${v.producteId}`);
        if (res && res.ok) productesCache[v.producteId] = await res.json();
      }
      const prod = productesCache[v.producteId];
      if (prod) nomProducte = prod.nom;
    } catch (e) {}
    return {
      ...v,
      nomProducte,
      dataVenda: v.dataVenda
        ? new Date(v.dataVenda).toLocaleDateString(I18n.idioma === "ca" ? "ca-ES" : "en-GB")
        : "—"
    };
  }));

  renderPedidosPerfil(vendesAmbNom);
}


/* =====================================================
   4. TAB HISTORIAL — localStorage
   ===================================================== */

function renderOldPurchases() {
  const lista = document.getElementById("purchases-list");
  if (!lista) return;

  const user = Auth.getUser();
  if (!user) return;

  const allOrders  = localStorage.getItem("imperium_pedidos");
  const pedidos    = allOrders ? JSON.parse(allOrders) : [];
  const misPedidos = pedidos.filter(p => p.userEmail === user.email);
  misPedidos.sort((a, b) => b.pedidoId.localeCompare(a.pedidoId));
  renderHistorialLocal(misPedidos);
}


/* =====================================================
   5. SISTEMA DE TABS
   ===================================================== */

function initProfileTabs() {
  const tabs     = document.querySelectorAll(".profile-tab");
  const contents = document.querySelectorAll(".tab-content");

  tabs.forEach(tab => {
    tab.addEventListener("click", function () {
      tabs.forEach(t     => t.classList.remove("active"));
      contents.forEach(c => c.classList.remove("active"));
      tab.classList.add("active");
      const target = document.getElementById(tab.dataset.tab);
      if (target) target.classList.add("active");
    });
  });
}


/* =====================================================
   6. CANCEL·LAR RESERVA DES DEL PERFIL
   ===================================================== */

async function cancelarReservaPerfil(classeId, nomClasse) {
  const confirmat = confirm(
    I18n.idioma === "ca"
      ? `Segur que vols cancel·lar la reserva de "${nomClasse}"?`
      : `Are you sure you want to cancel the booking for "${nomClasse}"?`
  );
  if (!confirmat) return;

  const user = Auth.getUser();
  if (!user) return;

  const ok = await ApiClasses.cancelarReserva(parseInt(user.id), parseInt(classeId));

  if (ok) {
    const clau = `imperium_reservas_${user.id}`;
    const totes = JSON.parse(localStorage.getItem(clau) || "[]");
    const noves = totes.filter(r =>
      String(r.classId) !== String(classeId) && r.reservaId !== `bd_${classeId}`
    );
    localStorage.setItem(clau, JSON.stringify(noves));
    showToast(t("toast.cancelReservaOk", [nomClasse]), "success");
    await renderReservationsInProfile();
  } else {
    showToast(t("toast.cancelReservaErr"), "error");
  }
}


/* =====================================================
   7. ESTADÍSTIQUES (només ADMIN)
   // Fragment suggerit per assistent IA - revisar i adaptar
   ===================================================== */

// Fragment suggerit per assistent IA - revisar i adaptar
// Fragment suggerit per assistent IA - revisar i adaptar
async function renderStats() {
  const container = document.getElementById("stats-container");
  if (!container) return;
  const user = Auth.getUser();
  const tabBtn = document.getElementById("tab-stats-btn");
  if (!user?.roles?.includes("ROLE_ADMIN")) { if (tabBtn) tabBtn.style.display="none"; return; }
  if (tabBtn) tabBtn.style.display = "block";

  container.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">⏳ ${I18n.idioma==="ca"?"Carregant estadístiques...":"Loading statistics..."}</p>`;

  const [resum, productes, classesTop, vendesMensuals, usuarisMensuals, distribucio, estocBaix, activitat] =
    await Promise.all([
      ApiStats.getResum(), ApiStats.getProductes(), ApiStats.getClasses(),
      ApiStats.getVendesMensuals(), ApiStats.getUsuarisMensuals(),
      ApiStats.getDistribucioTarifes(), ApiStats.getEstocBaix(), ApiStats.getActivitatRecent()
    ]);

  const mesos = { ca:["Gen","Feb","Mar","Abr","Mai","Jun","Jul","Ago","Set","Oct","Nov","Des"],
                  en:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"] };

  container.innerHTML = `

    <!-- KPIs principals -->
    <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(130px,1fr));gap:1rem;margin-bottom:2rem">
      ${statCard("👥", t("stats.totalUsuaris"),   resum?.totalUsuaris ?? "—")}
      ${statCard("🆕", t("stats.nousM"),          resum?.usuarisNousMes ?? "—")}
      ${statCard("📅", t("stats.reservesActives"), resum?.totalReservesActives ?? "—")}
      ${statCard("🛒", t("stats.vendesMes"),       productes?.totalVendesMes ?? "—")}
    </div>

    <!-- Fila de gràfics -->
    <div style="display:grid;grid-template-columns:1fr 1fr;gap:1.5rem;margin-bottom:2rem">

      <!-- Gràfic barres: Vendes mensuals -->
      <div style="background:var(--bg-secondary);border:1px solid var(--border);border-radius:12px;padding:1.25rem">
        <h4 style="font-size:.8rem;font-weight:700;text-transform:uppercase;letter-spacing:.08em;color:var(--primary);margin-bottom:1.25rem">
          📈 ${I18n.idioma==="ca"?"Vendes últims 6 mesos":"Sales last 6 months"}
        </h4>
        ${graficBarres(vendesMensuals, "vendes", mesos[I18n.idioma])}
      </div>

      <!-- Gràfic barres: Nous usuaris -->
      <div style="background:var(--bg-secondary);border:1px solid var(--border);border-radius:12px;padding:1.25rem">
        <h4 style="font-size:.8rem;font-weight:700;text-transform:uppercase;letter-spacing:.08em;color:var(--primary);margin-bottom:1.25rem">
          👥 ${I18n.idioma==="ca"?"Nous usuaris últims 6 mesos":"New users last 6 months"}
        </h4>
        ${graficBarres(usuarisMensuals, "nous", mesos[I18n.idioma])}
      </div>

    </div>

    <!-- Fila 2: Donut tarifes + Ocupació classes -->
    <div style="display:grid;grid-template-columns:1fr 1fr;gap:1.5rem;margin-bottom:2rem">

      <!-- Distribució tarifes -->
      <div style="background:var(--bg-secondary);border:1px solid var(--border);border-radius:12px;padding:1.25rem">
        <h4 style="font-size:.8rem;font-weight:700;text-transform:uppercase;letter-spacing:.08em;color:var(--primary);margin-bottom:1.25rem">
          🥇 ${I18n.idioma==="ca"?"Distribució de tarifes":"Plan distribution"}
        </h4>
        ${graficDonut(distribucio)}
      </div>

      <!-- Ocupació classes -->
      <div style="background:var(--bg-secondary);border:1px solid var(--border);border-radius:12px;padding:1.25rem">
        <h4 style="font-size:.8rem;font-weight:700;text-transform:uppercase;letter-spacing:.08em;color:var(--primary);margin-bottom:1rem">
          📊 ${I18n.idioma==="ca"?"Ocupació per classe":"Class occupancy"}
        </h4>
        ${classesTop?.length > 0
          ? classesTop.slice(0,6).map(c => {
              const pct = Math.min(100, c.pct||0);
              const col = pct>=80?"var(--danger)":pct>=50?"var(--primary)":"#22c55e";
              return `<div style="margin-bottom:.75rem">
                <div style="display:flex;justify-content:space-between;font-size:.78rem;margin-bottom:.25rem">
                  <span style="font-weight:500">${c.nom}</span>
                  <span style="color:var(--text-muted)">${c.reserves}/${c.capacitat} · <strong style="color:${col}">${pct}%</strong></span>
                </div>
                <div style="height:5px;background:var(--bg-card);border-radius:3px;overflow:hidden">
                  <div style="height:100%;width:${pct}%;background:${col};border-radius:3px"></div>
                </div>
              </div>`;
            }).join("")
          : `<p style="color:var(--text-muted);text-align:center;padding:1rem">${I18n.idioma==="ca"?"Sense dades":"No data"}</p>`
        }
      </div>

    </div>

    <!-- Fila 3: Estoc baix + Activitat recent -->
    <div style="display:grid;grid-template-columns:1fr 1fr;gap:1.5rem;margin-bottom:2rem">

      <!-- Alerta estoc baix -->
      <div style="background:var(--bg-secondary);border:1px solid var(--border);border-radius:12px;padding:1.25rem">
        <h4 style="font-size:.8rem;font-weight:700;text-transform:uppercase;letter-spacing:.08em;color:var(--danger);margin-bottom:1rem">
          ⚠️ ${I18n.idioma==="ca"?"Estoc baix (≤5 unitats)":"Low stock (≤5 units)"}
        </h4>
        ${estocBaix?.length > 0
          ? estocBaix.map(p => `
            <div style="display:flex;justify-content:space-between;align-items:center;
                        padding:.5rem .75rem;margin-bottom:.4rem;background:var(--bg-card);
                        border-radius:8px;border-left:3px solid ${p.estoc===0?"var(--danger)":"var(--primary)"}">
              <div>
                <div style="font-size:.82rem;font-weight:500">${p.nom}</div>
                <div style="font-size:.72rem;color:var(--text-muted)">${p.categoria||"—"}</div>
              </div>
              <span style="font-size:.85rem;font-weight:700;color:${p.estoc===0?"var(--danger)":"var(--primary)"}">
                ${p.estoc} uds
              </span>
            </div>`).join("")
          : `<p style="color:#22c55e;text-align:center;padding:1rem">✓ ${I18n.idioma==="ca"?"Tot l'estoc és correcte":"All stock levels OK"}</p>`
        }
      </div>

      <!-- Activitat recent -->
      <div style="background:var(--bg-secondary);border:1px solid var(--border);border-radius:12px;padding:1.25rem">
        <h4 style="font-size:.8rem;font-weight:700;text-transform:uppercase;letter-spacing:.08em;color:var(--primary);margin-bottom:1rem">
          🕐 ${I18n.idioma==="ca"?"Activitat recent":"Recent activity"}
        </h4>
        ${activitat?.reserves?.length > 0 ? `
          <p style="font-size:.72rem;color:var(--text-muted);margin-bottom:.4rem;text-transform:uppercase;letter-spacing:.06em">
            ${I18n.idioma==="ca"?"Últimes reserves":"Latest bookings"}
          </p>
          ${activitat.reserves.slice(0,3).map(r => `
            <div style="display:flex;justify-content:space-between;padding:.4rem 0;
                        border-bottom:.5px solid var(--border);font-size:.78rem">
              <span><strong>${r.usuari}</strong> → ${r.classe}</span>
              <span style="color:var(--text-muted)">${new Date(r.dataReserva).toLocaleDateString(I18n.idioma==="ca"?"ca-ES":"en-GB")}</span>
            </div>`).join("")}
          <p style="font-size:.72rem;color:var(--text-muted);margin:.75rem 0 .4rem;text-transform:uppercase;letter-spacing:.06em">
            ${I18n.idioma==="ca"?"Últimes vendes":"Latest sales"}
          </p>
          ${activitat.vendes?.slice(0,3).map(v => `
            <div style="display:flex;justify-content:space-between;padding:.4rem 0;
                        border-bottom:.5px solid var(--border);font-size:.78rem">
              <span><strong>${v.usuari}</strong> → ${v.producte} (×${v.quantitat})</span>
              <span style="color:var(--text-muted)">${new Date(v.dataVenda).toLocaleDateString(I18n.idioma==="ca"?"ca-ES":"en-GB")}</span>
            </div>`).join("")}
        ` : `<p style="color:var(--text-muted);text-align:center;padding:1rem">${I18n.idioma==="ca"?"Sense activitat recent":"No recent activity"}</p>`}
      </div>

    </div>

    <!-- Botiga KPIs -->
    <div style="background:var(--bg-secondary);border:1px solid var(--border);border-radius:12px;padding:1.25rem">
      <h4 style="font-size:.8rem;font-weight:700;text-transform:uppercase;letter-spacing:.08em;color:var(--primary);margin-bottom:1rem">
        🏪 ${t("stats.botiga")}
      </h4>
      <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(130px,1fr));gap:1rem">
        ${statCard("📦", t("stats.topMes"), productes?.topProducteMes ? `${productes.topProducteMes} (${productes.topProducteMesUnitats} uds)` : t("stats.senseVendes"))}
        ${statCard("📆", t("stats.topAny"), productes?.topProducteAny ? `${productes.topProducteAny} (${productes.topProducteAnyUnitats} uds)` : t("stats.senseVendes"))}
        ${statCard("⚠️", t("stats.menysEstoc"), productes?.menysEstocNom ? `${productes.menysEstocNom} (${productes.menysEstocUnitats} uds)` : "—")}
        ${statCard("🏆", t("stats.classeMes"), resum?.classeMesReservada ? `${resum.classeMesReservada} (${resum.classeMesReservadaCount})` : "—")}
      </div>
    </div>
  `;
}

// Fragment suggerit per assistent IA - revisar i adaptar
function graficBarres(dades, camp, nomMesos) {
  if (!dades || dades.length === 0)
    return `<p style="color:var(--text-muted);text-align:center;padding:1rem">${I18n.idioma==="ca"?"Sense dades":"No data"}</p>`;

  const maxVal = Math.max(...dades.map(d => d[camp] || 0), 1);
  const barWidth = 100 / Math.max(dades.length, 1);

  return `
    <div style="display:flex;align-items:flex-end;gap:4px;height:100px;padding-bottom:20px;position:relative">
      ${dades.map(d => {
        const val = d[camp] || 0;
        const pct = Math.round((val / maxVal) * 100);
        const mes = nomMesos[(d.mes - 1)] || d.nomMes || "";
        return `
          <div style="flex:1;display:flex;flex-direction:column;align-items:center;height:100%">
            <div style="font-size:.68rem;color:var(--text-muted);margin-bottom:2px">${val}</div>
            <div style="flex:1;display:flex;align-items:flex-end;width:100%">
              <div style="width:100%;height:${Math.max(pct,3)}%;background:var(--primary);
                          border-radius:3px 3px 0 0;opacity:.85;transition:height .3s"></div>
            </div>
            <div style="font-size:.65rem;color:var(--text-muted);margin-top:4px;white-space:nowrap">${mes}</div>
          </div>`;
      }).join("")}
    </div>`;
}

// Fragment suggerit per assistent IA - revisar i adaptar
function graficDonut(dades) {
  if (!dades || dades.length === 0)
    return `<p style="color:var(--text-muted);text-align:center;padding:1rem">${I18n.idioma==="ca"?"Sense dades":"No data"}</p>`;

  const total = dades.reduce((s, d) => s + (d.count || 0), 0);
  const colors = ["var(--primary)", "#6366f1", "#22c55e", "#f59e0b", "#ec4899"];
  let offset = 0;
  const r = 45, cx = 60, cy = 60, circumference = 2 * Math.PI * r;

  const segments = dades.map((d, i) => {
    const pct   = total > 0 ? d.count / total : 0;
    const dash  = pct * circumference;
    const gap   = circumference - dash;
    const seg   = `<circle cx="${cx}" cy="${cy}" r="${r}" fill="none"
      stroke="${colors[i % colors.length]}" stroke-width="18"
      stroke-dasharray="${dash.toFixed(1)} ${gap.toFixed(1)}"
      stroke-dashoffset="${(-offset * circumference).toFixed(1)}"
      transform="rotate(-90 ${cx} ${cy})"/>`;
    offset += pct;
    return seg;
  }).join("");

  const llegenda = dades.map((d, i) => `
    <div style="display:flex;align-items:center;gap:.5rem;margin-bottom:.3rem;font-size:.78rem">
      <div style="width:10px;height:10px;border-radius:50%;background:${colors[i%colors.length]};flex-shrink:0"></div>
      <span style="color:var(--text-muted)">${d.tarifa}</span>
      <span style="margin-left:auto;font-weight:600">${d.count}</span>
    </div>`).join("");

  return `
    <div style="display:flex;align-items:center;gap:1.5rem">
      <svg width="120" height="120" viewBox="0 0 120 120">
        ${segments}
        <text x="${cx}" y="${cy}" text-anchor="middle" dominant-baseline="middle"
              fill="var(--text)" font-size="14" font-weight="700">${total}</text>
      </svg>
      <div style="flex:1">${llegenda}</div>
    </div>`;
}

function statCard(icon, label, value) {
  return `
    <div style="background:var(--bg-secondary);border:1px solid var(--border);
                border-radius:12px;padding:1.25rem;text-align:center">
      <div style="font-size:1.8rem;margin-bottom:0.5rem">${icon}</div>
      <div style="font-size:0.75rem;color:var(--text-muted);text-transform:uppercase;
                  letter-spacing:0.08em;margin-bottom:0.5rem">${label}</div>
      <div style="font-size:1rem;font-weight:700;color:var(--text)">${value}</div>
    </div>`;
}

// Re-renderitza quan canvia l'idioma
// Fragment suggerit per assistent IA - revisar i adaptar
document.addEventListener("idioma:canvi", async function () {
  await Promise.all([
    renderReservationsInProfile(),
    renderOrderHistory(),
    renderStats(),
    renderGestioUsuaris()
  ]);
  renderOldPurchases();
});