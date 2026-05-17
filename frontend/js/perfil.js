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
async function renderStats() {
  const container = document.getElementById("stats-container");
  if (!container) return;

  const user   = Auth.getUser();
  const tabBtn = document.getElementById("tab-stats-btn");
  if (!user || !user.roles || !user.roles.includes("ROLE_ADMIN")) {
    if (tabBtn) tabBtn.style.display = "none";
    return;
  }
  if (tabBtn) tabBtn.style.display = "block";

  const [resum, productes, classesTop] = await Promise.all([
    ApiStats.getResum(),
    ApiStats.getProductes(),
    ApiStats.getClasses()
  ]);

  if (!resum && !productes) {
    container.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">${t("stats.error")}</p>`;
    return;
  }

  container.innerHTML = `

    <!-- Bloc 1: KPIs principals -->
    <div style="margin-bottom:2rem">
      <h3 style="font-size:.85rem;font-weight:700;text-transform:uppercase;
                 letter-spacing:.1em;color:var(--primary);margin-bottom:1rem">
        ${t("stats.resum")}
      </h3>
      <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(130px,1fr));gap:1rem">
        ${statCard("👥", t("stats.totalUsuaris"),   resum?.totalUsuaris         ?? "—")}
        ${statCard("🆕", t("stats.nousM"),          resum?.usuarisNousMes       ?? "—")}
        ${statCard("📅", t("stats.reservesActives"), resum?.totalReservesActives ?? "—")}
        ${statCard("🏆", t("stats.classeMes"),
          resum?.classeMesReservada
            ? `${resum.classeMesReservada} (${resum.classeMesReservadaCount})`
            : "—"
        )}
      </div>
    </div>

    <!-- Bloc 2: Rànquing visual de classes -->
    ${classesTop && classesTop.length > 0 ? `
    <div style="margin-bottom:2rem">
      <h3 style="font-size:.85rem;font-weight:700;text-transform:uppercase;
                 letter-spacing:.1em;color:var(--primary);margin-bottom:1rem">
        📊 ${I18n.idioma === "ca" ? "Ocupació per Classe" : "Class Occupancy"}
      </h3>
      <div style="background:var(--bg-secondary);border:1px solid var(--border);
                  border-radius:12px;padding:1.25rem">
        ${classesTop.slice(0,8).map(c => {
          const pct   = Math.min(100, c.pct || 0);
          const color = pct >= 80 ? "var(--danger)" : pct >= 50 ? "var(--primary)" : "var(--success, #22c55e)";
          return `
            <div style="margin-bottom:.85rem">
              <div style="display:flex;justify-content:space-between;
                          font-size:.8rem;margin-bottom:.3rem">
                <span style="font-weight:500;color:var(--text)">${c.nom}</span>
                <span style="color:var(--text-muted)">${c.reserves} / ${c.capacitat} · <strong style="color:${color}">${pct}%</strong></span>
              </div>
              <div style="height:6px;background:var(--bg-card);border-radius:3px;overflow:hidden">
                <div style="height:100%;width:${pct}%;background:${color};
                            border-radius:3px;transition:width .4s ease"></div>
              </div>
            </div>`;
        }).join("")}
      </div>
    </div>` : ""}

    <!-- Bloc 3: Botiga -->
    <div>
      <h3 style="font-size:.85rem;font-weight:700;text-transform:uppercase;
                 letter-spacing:.1em;color:var(--primary);margin-bottom:1rem">
        ${t("stats.botiga")}
      </h3>
      <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(130px,1fr));gap:1rem">
        ${statCard("📦", t("stats.topMes"),
          productes?.topProducteMes
            ? `${productes.topProducteMes} (${productes.topProducteMesUnitats} uds)`
            : t("stats.senseVendes")
        )}
        ${statCard("📆", t("stats.topAny"),
          productes?.topProducteAny
            ? `${productes.topProducteAny} (${productes.topProducteAnyUnitats} uds)`
            : t("stats.senseVendes")
        )}
        ${statCard("⚠️", t("stats.menysEstoc"),
          productes?.menysEstocNom
            ? `${productes.menysEstocNom} (${productes.menysEstocUnitats} uds)`
            : "—"
        )}
        ${statCard("🛒", t("stats.vendesMes"), productes?.totalVendesMes ?? "—")}
      </div>
    </div>
  `;
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