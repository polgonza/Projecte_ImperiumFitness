/* =====================================================
   IMPERIUM FITNESS — perfil.js
   Connectat al backend real via api.js
   ===================================================== */


/* =====================================================
   1. INICIALITZACIÓ — càrrega de dades reals
   ===================================================== */

async function initPerfil() {
  if (!Auth.isLoggedIn()) {
    window.location.href = "login.html";
    return;
  }

  const user = Auth.getUser();
  if (!user) {
    window.location.href = "login.html";
    return;
  }

  // Mostrem dades bàsiques mentre carreguen les del backend
  const inicial = user.name ? user.name.charAt(0).toUpperCase() : "?";
  renderDatosUsuario(inicial, user.name || "—", user.email || "—", "Sin plan activo", "—");

  // Carreguem dades reals del backend
  const perfil = await ApiUsuari.getPerfil(user.id);

  if (perfil) {
    const dataRegistre = perfil.dataRegistre
      ? new Date(perfil.dataRegistre).toLocaleDateString("es-ES")
      : "—";

    renderDatosUsuario(
      perfil.nom.charAt(0).toUpperCase(),
      perfil.nom,
      perfil.email,
      "Sin plan activo",
      dataRegistre
    );

    Auth.setUser({ ...user, name: perfil.nom, email: perfil.email });
  }

  const noPlanMsg = document.getElementById("no-plan-msg");
  if (noPlanMsg) noPlanMsg.style.display = "block";
}

function setTextById(id, text) {
  const el = document.getElementById(id);
  if (el) el.textContent = text;
}


/* =====================================================
   2. TAB RESERVES — dades reals del backend
   ===================================================== */

async function renderReservationsInProfile() {
  const lista = document.getElementById("reservations-list");
  if (!lista) return;

  const user = Auth.getUser();
  if (!user) return;

  lista.innerHTML = `
    <p style="color:var(--text-muted);text-align:center;padding:2rem">
      ⏳ Cargando reservas...
    </p>`;

  const reserves = await ApiUsuari.getReserves(user.id);

  if (!reserves || reserves.length === 0) {
    lista.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;padding:2rem">
        No tienes reservas de clases.<br>
        <a href="actividades.html" style="color:var(--primary)">
          Ver clases disponibles
        </a>
      </p>`;
    return;
  }

  // Filtrem reserves futures o d'avui
  const ara = new Date();
  ara.setHours(0, 0, 0, 0);

  const reservesFutures = reserves.filter(r => {
    if (!r.dataReserva) return true;
    const dataReserva = new Date(r.dataReserva);
    dataReserva.setHours(0, 0, 0, 0);
    return dataReserva >= ara;
  });

  if (reservesFutures.length === 0) {
    lista.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;padding:2rem">
        No tienes reservas próximas.<br>
        <a href="actividades.html" style="color:var(--primary)">
          Reservar una clase
        </a>
      </p>`;
    return;
  }

  // Ordenem de més pròxima a més llunyana
  reservesFutures.sort((a, b) => new Date(a.dataReserva) - new Date(b.dataReserva));

  // Obtenim el nom de cada classe
  const classesCache = {};
  const reservesAmbNom = await Promise.all(reservesFutures.map(async r => {
    let nomClasse = `Clase #${r.classeId}`;
    try {
      if (!classesCache[r.classeId]) {
        const res = await apiFetch(`/api/classes/${r.classeId}`);
        if (res && res.ok) {
          const classe = await res.json();
          classesCache[r.classeId] = classe.nom;
        }
      }
      nomClasse = classesCache[r.classeId] || nomClasse;
    } catch (e) {}

    return {
      classeId:    r.classeId,    // ← AFEGEIX AIXÒ
      nomClasse,
      dataReserva: r.dataReserva
        ? new Date(r.dataReserva).toLocaleDateString("es-ES")
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

  container.innerHTML = `
    <p style="color:var(--text-muted);text-align:center;padding:2rem">
      ⏳ Cargando pedidos...
    </p>`;

  const user = Auth.getUser();
  if (!user) return;

  const vendes = await ApiUsuari.getVendes(user.id);

  if (!vendes || vendes.length === 0) {
    container.innerHTML = `
      <div style="color:var(--text-muted);text-align:center;padding:2rem">
        No has realizado ningún pedido aún.<br>
        <a href="tienda.html" style="color:var(--primary)">
          Ir a la tienda
        </a>
      </div>`;
    return;
  }

  const productesCache = {};
  const vendesAmbNom = await Promise.all(vendes.map(async v => {
    let nomProducte = `Producto #${v.producteId}`;
    try {
      if (!productesCache[v.producteId]) {
        const res = await apiFetch(`/api/productes/${v.producteId}`);
        if (res && res.ok) {
          const p = await res.json();
          productesCache[v.producteId] = p;
        }
      }
      const prod = productesCache[v.producteId];
      if (prod) nomProducte = prod.nom;
    } catch (e) {}

    return {
      ...v,
      nomProducte,
      dataVenda: v.dataVenda
        ? new Date(v.dataVenda).toLocaleDateString("es-ES")
        : "—"
    };
  }));

  renderPedidosPerfil(vendesAmbNom);
}


/* =====================================================
   4. TAB HISTORIAL — localStorage (pedidos del carrito)
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
    tab.addEventListener("click", function() {
      tabs.forEach(t     => t.classList.remove("active"));
      contents.forEach(c => c.classList.remove("active"));
      tab.classList.add("active");
      const target = document.getElementById(tab.dataset.tab);
      if (target) target.classList.add("active");
    });
  });
}


/* =====================================================
   6. ARRANQUE
   ===================================================== */
async function cancelarReservaPerfil(classeId, nomClasse) {
  const confirmat = confirm(
    `¿Seguro que quieres cancelar la reserva de "${nomClasse}"?`
  );
  if (!confirmat) return;

  const user = Auth.getUser();
  if (!user) return;

  const ok = await ApiClasses.cancelarReserva(
    parseInt(user.id),
    parseInt(classeId)
  );

  if (ok) {
    // Eliminem també del localStorage
    const clau = `imperium_reservas_${user.id}`;
    const totes = JSON.parse(localStorage.getItem(clau) || "[]");
    const noves = totes.filter(r =>
      String(r.classId) !== String(classeId) &&
      r.reservaId !== `bd_${classeId}`
    );
    localStorage.setItem(clau, JSON.stringify(noves));

    showToast(`Reserva de "${nomClasse}" cancelada.`, "success");
    await renderReservationsInProfile();
  } else {
    showToast("No se pudo cancelar la reserva.", "error");
  }
}
document.addEventListener("DOMContentLoaded", async function() {

  if (!document.getElementById("profile-initial")) return;

  await Promise.all([
    initPerfil(),
    renderReservationsInProfile(),
    renderOrderHistory()
  ]);

  renderOldPurchases();
  initProfileTabs();
});
