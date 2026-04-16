/* =====================================================
   IMPERIUM FITNESS — perfil.js
   Página de Perfil del Usuario
   -------------------------------------------------------
   Este archivo se carga en perfil.html después de app.js y carrito.js.
   Gestiona:
     1. Mostrar datos del usuario
     2. Tabs (Reservas / Mis Pedidos / Historial)
     3. Renderizar el historial de pedidos desde localStorage
   ===================================================== */


/* =====================================================
   1. CARGA DE DATOS DEL USUARIO
   ===================================================== */

function initPerfil() {
  const user = Auth.getUser();

  if (!user) {
    window.location.href = "login.html";
    return;
  }

  const inicial = user.name ? user.name.charAt(0).toUpperCase() : "?";

  setTextById("profile-initial",  inicial);
  setTextById("profile-name",     user.name || "—");
  setTextById("profile-email",    user.email || "—");
  setTextById("profile-since",    user.memberSince || "—");

  // Plan activo: si no tiene, mensaje informativo
  const planText = user.plan || "Sin plan activo";
  setTextById("profile-plan",      planText);
  setTextById("profile-plan-stat", planText);

  // Si no tiene plan, destaca el botón de tarifas con un mensaje
  if (!user.plan) {
    const noPlanMsg = document.getElementById("no-plan-msg");
    if (noPlanMsg) noPlanMsg.style.display = "block";
  }
}

/* Pequeña utilidad: pone texto en un elemento si existe */
function setTextById(id, text) {
  const el = document.getElementById(id);
  if (el) el.textContent = text;
}


/* =====================================================
   2. HISTORIAL DE PEDIDOS
   Lee "imperium_pedidos" de localStorage y pinta las tarjetas
   ===================================================== */

function renderOrderHistory() {
  const container = document.getElementById("orders-list");
  if (!container) return;

  const user = Auth.getUser();
  if (!user) return;

  // Carga todos los pedidos y filtra los del usuario actual
  const allOrders  = localStorage.getItem("imperium_pedidos");
  const pedidos    = allOrders ? JSON.parse(allOrders) : [];
  const misPedidos = pedidos.filter(function(p) {
    return p.userEmail === user.email;
  });

  if (misPedidos.length === 0) {
    container.innerHTML = `
      <div class="no-orders">
        Todavía no has realizado ningún pedido.
        <br>
        <a href="tienda.html">Ir a la tienda</a>
      </div>`;
    return;
  }

  // Ordena del más reciente al más antiguo (por ID que contiene timestamp)
  misPedidos.sort(function(a, b) {
    return b.pedidoId.localeCompare(a.pedidoId);
  });

  // Construye el HTML de cada tarjeta de pedido
  container.innerHTML = misPedidos.map(function(pedido) {

    // Lista de productos del pedido
    const productosHTML = pedido.productos.map(function(p) {
      return `
        <li>
          <span class="p-name">${p.name} x${p.quantity}</span>
          <span class="p-price">${(p.price * p.quantity).toFixed(2)} €</span>
        </li>
      `;
    }).join("");

    return `
      <div class="order-card">

        <!-- Cabecera: ID + fecha + estado -->
        <div class="order-card-header">
          <div>
            <div class="order-id">${pedido.pedidoId}</div>
            <div class="order-date">${pedido.fecha}</div>
          </div>
          <span class="order-status">Completado</span>
        </div>

        <!-- Lista de productos -->
        <ul class="order-products">
          ${productosHTML}
        </ul>

        <!-- Total -->
        <div class="order-card-footer">
          <span class="order-total-label">Total:</span>
          <span class="order-total-amount">${pedido.total.toFixed(2)} €</span>
        </div>

      </div>
    `;
  }).join("");
}


/* =====================================================
   3. RESERVAS DE CLASES (lee de actividades.js)
   ===================================================== */

function renderReservationsInProfile() {
  const lista = document.getElementById("reservations-list");
  if (!lista) return;

  const user = Auth.getUser();
  if (!user) return;

  // Las reservas las guarda actividades.js con la clave "imperium_reservas"
  const data       = localStorage.getItem("imperium_reservas");
  const todas      = data ? JSON.parse(data) : [];
  const misReservas = todas.filter(function(r) {
    return r.userEmail === user.email;
  });

  if (misReservas.length === 0) {
    lista.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;padding:2rem">
        No tienes reservas de clases.
        <br>
        <a href="actividades.html">Ver clases disponibles</a>
      </p>`;
    return;
  }

  // Ordena por fecha
  misReservas.sort(function(a, b) {
    return a.dateKey.localeCompare(b.dateKey);
  });

  lista.innerHTML = misReservas.map(function(r) {
    // Convierte dateKey "2026-04-14" a "14/04/2026"
    const parts = r.dateKey.split("-");
    const fecha = parts[2] + "/" + parts[1] + "/" + parts[0];

    return `
      <div class="reservation-item">
        <div>
          <h4>${r.className}</h4>
          <p>${fecha} · ${r.time} · ${r.instructor || ""}</p>
        </div>
        <span class="status-badge confirmed">Confirmada</span>
      </div>
    `;
  }).join("");
}


/* =====================================================
   4. HISTORIAL DE COMPRAS ANTIGUAS (datos mock de app.js)
   ===================================================== */

function renderOldPurchases() {
  const lista = document.getElementById("purchases-list");
  if (!lista) return;

  const user = Auth.getUser();
  if (!user) return;

  // Los datos mockeados que vienen del objeto usuario original
  if (user.purchases && user.purchases.length > 0) {
    lista.innerHTML = user.purchases.map(function(p) {
      return `
        <div class="purchase-item">
          <div>
            <h4>${p.product}</h4>
            <p>${p.date}</p>
          </div>
          <div style="text-align:right">
            <div style="font-weight:700">${p.amount.toFixed(2)} €</div>
            <span class="status-badge delivered">${p.status}</span>
          </div>
        </div>
      `;
    }).join("");
  } else {
    lista.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;padding:2rem">
        No tienes compras en el historial.
      </p>`;
  }
}


/* =====================================================
   5. SISTEMA DE TABS
   ===================================================== */

function initProfileTabs() {
  const tabs     = document.querySelectorAll(".profile-tab");
  const contents = document.querySelectorAll(".tab-content");

  tabs.forEach(function(tab) {
    tab.addEventListener("click", function() {
      // Quita el estado activo de todos
      tabs.forEach(function(t)     { t.classList.remove("active"); });
      contents.forEach(function(c) { c.classList.remove("active"); });

      // Activa el tab clicado y su contenido
      tab.classList.add("active");
      const target = document.getElementById(tab.dataset.tab);
      if (target) target.classList.add("active");
    });
  });
}


/* =====================================================
   6. ARRANQUE
   ===================================================== */

document.addEventListener("DOMContentLoaded", function() {

  // Solo ejecutamos si estamos en perfil.html
  if (!document.getElementById("profile-initial")) return;

  initPerfil();
  renderReservationsInProfile();
  renderOrderHistory();
  renderOldPurchases();
  initProfileTabs();

});
