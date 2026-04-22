/* =====================================================
   IMPERIUM FITNESS — perfil.js
   Connectat al backend real via api.js
   ===================================================== */


/* =====================================================
   1. INICIALITZACIÓ — càrrega de dades reals
   ===================================================== */

async function initPerfil() {
  // Si no hi ha sessió, redirigim al login
  if (!Auth.isLoggedIn()) {
    window.location.href = "login.html";
    return;
  }

  const user = Auth.getUser();
  if (!user) {
    window.location.href = "login.html";
    return;
  }

  // Mostrem dades bàsiques del localStorage mentre carreguen les del backend
  const inicial = user.name ? user.name.charAt(0).toUpperCase() : "?";
  setTextById("profile-initial", inicial);
  setTextById("profile-name",    user.name || "—");
  setTextById("profile-email",   user.email || "—");
  setTextById("profile-plan",    "Sin plan activo");
  setTextById("profile-plan-stat", "Sin plan activo");
  setTextById("profile-since",   "—");

  // Carreguem dades reals del backend
  const perfil = await ApiUsuari.getPerfil(user.id);

  if (perfil) {
    const dataRegistre = perfil.dataRegistre
      ? new Date(perfil.dataRegistre).toLocaleDateString("es-ES")
      : "—";

    setTextById("profile-initial", perfil.nom.charAt(0).toUpperCase());
    setTextById("profile-name",    perfil.nom);
    setTextById("profile-email",   perfil.email);
    setTextById("profile-since",   dataRegistre);

    // Actualitzem també el localStorage amb el nom real
    Auth.setUser({
      ...user,
      name:  perfil.nom,
      email: perfil.email
    });
  }

  // Mostrem missatge si no té pla actiu
  const noPlanMsg = document.getElementById("no-plan-msg");
  if (noPlanMsg) noPlanMsg.style.display = "block";
}

/* Utilitat: posa text en un element si existeix */
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

  lista.innerHTML = `
    <p style="color:var(--text-muted);text-align:center;padding:2rem">
      ⏳ Cargando reservas...
    </p>`;

  const user = Auth.getUser();
  if (!user) return;

  // Obtenim les reserves del backend
  const reserves = await ApiUsuari.getReserves(user.id);

  if (reserves.length === 0) {
    lista.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;padding:2rem">
        No tienes reservas de clases.<br>
        <a href="actividades.html" style="color:var(--primary)">
          Ver clases disponibles
        </a>
      </p>`;
    return;
  }

  // Per cada reserva, obtenim el nom de la classe
  // Les reserves retornen classeId, necessitem el nom
  const classesCache = {};

  lista.innerHTML = await Promise.all(reserves.map(async r => {
    // Obtenim el nom de la classe si no el tenim en caché
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

    const dataReserva = r.dataReserva
      ? new Date(r.dataReserva).toLocaleDateString("es-ES")
      : "—";

    return `
      <div class="reservation-item">
        <div>
          <h4>${nomClasse}</h4>
          <p>${dataReserva}</p>
        </div>
        <span class="status-badge confirmed">Confirmada</span>
      </div>
    `;
  })).then(items => items.join(""));
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

  if (vendes.length === 0) {
    container.innerHTML = `
      <div style="color:var(--text-muted);text-align:center;padding:2rem">
        No has realizado ningún pedido aún.<br>
        <a href="tienda.html" style="color:var(--primary)">
          Ir a la tienda
        </a>
      </div>`;
    return;
  }

  // Per cada venda obtenim el nom del producte
  const productesCache = {};

  container.innerHTML = await Promise.all(vendes.map(async v => {
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

    const dataVenda = v.dataVenda
      ? new Date(v.dataVenda).toLocaleDateString("es-ES")
      : "—";

    return `
      <div class="order-card">
        <div class="order-card-header">
          <div>
            <div class="order-id">Pedido #${v.id}</div>
            <div class="order-date">${dataVenda}</div>
          </div>
          <span class="order-status">Completado</span>
        </div>
        <ul class="order-products">
          <li>
            <span class="p-name">${nomProducte} x${v.quantitat}</span>
          </li>
        </ul>
      </div>
    `;
  })).then(items => items.join(""));
}


/* =====================================================
   4. TAB HISTORIAL — localStorage (pedidos del carrito)
   ===================================================== */

function renderOldPurchases() {
  const lista = document.getElementById("purchases-list");
  if (!lista) return;

  const user = Auth.getUser();
  if (!user) return;

  const allOrders = localStorage.getItem("imperium_pedidos");
  const pedidos   = allOrders ? JSON.parse(allOrders) : [];
  const misPedidos = pedidos.filter(p => p.userEmail === user.email);

  if (misPedidos.length === 0) {
    lista.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;padding:2rem">
        No tienes compras en el historial.
      </p>`;
    return;
  }

  misPedidos.sort((a, b) => b.pedidoId.localeCompare(a.pedidoId));

  lista.innerHTML = misPedidos.map(pedido => {
    const productesHTML = pedido.productos.map(p => `
      <li>
        <span class="p-name">${p.name} x${p.quantity}</span>
        <span class="p-price">${(p.price * p.quantity).toFixed(2)} €</span>
      </li>
    `).join("");

    return `
      <div class="order-card">
        <div class="order-card-header">
          <div>
            <div class="order-id">${pedido.pedidoId}</div>
            <div class="order-date">${pedido.fecha}</div>
          </div>
          <span class="order-status">Completado</span>
        </div>
        <ul class="order-products">${productesHTML}</ul>
        <div class="order-card-footer">
          <span class="order-total-label">Total:</span>
          <span class="order-total-amount">${pedido.total.toFixed(2)} €</span>
        </div>
      </div>
    `;
  }).join("");
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

document.addEventListener("DOMContentLoaded", async function() {

  // Només executem si estem a perfil.html
  if (!document.getElementById("profile-initial")) return;

  // Carreguem tot en paral·lel per ser més ràpids
  await Promise.all([
    initPerfil(),
    renderReservationsInProfile(),
    renderOrderHistory()
  ]);

  renderOldPurchases();
  initProfileTabs();
});