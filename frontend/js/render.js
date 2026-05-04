/* =====================================================
   IMPERIUM FITNESS — render.js
   Funciones de Renderizado (mostrar datos en pantalla)
   -------------------------------------------------------
   ESTE ARCHIVO contiene SOLO lógica de visualización:
   - Manipulación del DOM
   - innerHTML de secciones
   - Pintar datos en pantalla

   NO contiene:
   - Lógica de negocio
   - Llamadas al backend
   - Gestión de estado

   DEPENDE DE: app.js (Auth, PLANS, GYMS, CLASSES,
               PRODUCTS, BLOG_POSTS, FACILITIES)
   ===================================================== */


/* =====================================================
   1. NAVBAR — estado del usuario
   ===================================================== */

/*
  renderNavbarUser() — muestra el nombre del usuario
  o los botones de login/registro según si hay sesión.
  Reemplaza a: updateNavbarUser() en app.js
*/
function renderNavbarUser() {
  const user          = Auth.getUser();
  const loggedIn      = Auth.isLoggedIn();
  const navUserArea   = document.getElementById("nav-user-area");
  const navMobileUser = document.getElementById("nav-mobile-user");

  if (!navUserArea) return;

  if (loggedIn && user) {
    const inicial = user.name ? user.name.charAt(0).toUpperCase() : "?";
    const prenom  = user.name ? user.name.split(" ")[0] : "Usuario";

    navUserArea.innerHTML = `
      <a href="perfil.html" class="btn-user">
        <div class="avatar">${inicial}</div>
        ${prenom}
      </a>
      <button onclick="Auth.logout()" class="btn btn-secondary"
              style="padding:0.4rem 0.9rem;font-size:0.75rem">
        Salir
      </button>
    `;
    if (navMobileUser) {
      navMobileUser.innerHTML = `
        <a href="perfil.html">Mi Perfil</a>
        <button onclick="Auth.logout()">Cerrar Sesión</button>
      `;
    }
  } else {
    navUserArea.innerHTML = `
      <a href="login.html" class="btn-user">👤 Iniciar sesión</a>
      <a href="tarifas.html" class="btn btn-primary"
         style="padding:0.45rem 1.25rem;font-size:0.75rem">Únete</a>
    `;
    if (navMobileUser) {
      navMobileUser.innerHTML = `
        <a href="login.html">Iniciar Sesión</a>
        <a href="register.html">Registrarse</a>
      `;
    }
  }
}


/* =====================================================
   2. TARIFAS — planes de suscripción
   ===================================================== */

/*
  renderPlanes(containerId) — pinta las cards de planes
  en el elemento con el id indicado.
  Reemplaza a: renderPlans() en app.js
*/
function renderPlanes(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  const user     = Auth.getUser();
  const userPlan = user ? user.plan : null;

  container.innerHTML = PLANS.map(plan => {
    const isCurrent = userPlan && userPlan === plan.name;

    return `
      <div class="plan-card ${plan.popular ? "popular" : ""}">
        ${plan.popular ? '<div class="popular-badge">⭐ Más Popular</div>' : ""}
        ${isCurrent  ? '<div class="popular-badge" style="background:var(--success)">✓ Plan Actual</div>' : ""}
        <div class="plan-header">
          <div class="plan-name">${plan.name}</div>
          <div class="plan-price">
            <span class="amount">${plan.price}€</span>
            <span class="period">/${plan.period}</span>
          </div>
        </div>
        <ul class="plan-features">
          ${plan.features.map(f => `
            <li><span class="check">✓</span><span>${f}</span></li>
          `).join("")}
          ${plan.notIncluded.map(f => `
            <li class="excluded"><span class="check">✗</span><span>${f}</span></li>
          `).join("")}
        </ul>
        <div class="plan-footer">
          <button class="btn btn-${plan.popular ? "primary" : "secondary"}"
                  style="width:100%;justify-content:center"
                  onclick="buyPlan('${plan.name}', ${plan.price})">
            ${isCurrent ? "Plan Actual" : plan.cta}
          </button>
        </div>
      </div>
    `;
  }).join("");
}


/* =====================================================
   3. OCUPACIÓN — gimnasios en tiempo real
   ===================================================== */

/*
  renderOcupacion(containerId) — pinta las cards de
  ocupación de cada gimnasio.
  Reemplaza a: renderGymOccupancy() en app.js
*/
function renderOcupacion(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  container.innerHTML = GYMS.map(gym => {
    const pct   = Math.round((gym.current / gym.capacity) * 100);
    const level = pct < 40 ? "low" : pct < 70 ? "medium" : "high";
    const label = pct < 40 ? "Poca afluencia" : pct < 70 ? "Afluencia media" : "Mucha afluencia";

    return `
      <div class="gym-occupancy-card">
        <h3>${gym.name}</h3>
        <p class="gym-address">📍 ${gym.location}</p>
        <div class="occupancy-bar-wrap">
          <div class="occupancy-bar ${level}" style="width:${pct}%"></div>
        </div>
        <div class="occupancy-stats">
          <span>${gym.current} / ${gym.capacity} personas · ${label}</span>
          <span class="pct ${level}">${pct}%</span>
        </div>
      </div>
    `;
  }).join("");
}


/* =====================================================
   4. CLASES — tarjetas con filtro de categoría
   ===================================================== */

/*
  renderClases(containerId, filter) — pinta las tarjetas
  de clases. Si filter es "all" muestra todas; si no,
  solo las de esa categoría.
  Reemplaza a: renderClasses() en app.js
*/
function renderClases(containerId, filter = "all") {
  const container = document.getElementById(containerId);
  if (!container) return;

  const filtered = filter === "all"
    ? CLASSES
    : CLASSES.filter(c => c.category === filter);

  if (filtered.length === 0) {
    container.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;
                grid-column:1/-1;padding:3rem">
        No hay clases disponibles para este filtro.
      </p>`;
    return;
  }

  container.innerHTML = filtered.map(cls => {
    const pct       = Math.round((cls.booked / cls.spots) * 100);
    const available = cls.spots - cls.booked;

    return `
      <div class="class-card">
        <div class="class-meta">
          <span class="class-tag">${cls.category}</span>
          <span class="class-tag"
                style="background:rgba(255,255,255,0.05);color:var(--text-muted)">
            ${cls.day}
          </span>
        </div>
        <h3>${cls.name}</h3>
        <p class="class-trainer">👤 ${cls.trainer}</p>
        <div class="class-details">
          <span>🕐 ${cls.time}</span>
          <span>⏱ ${cls.duration}</span>
        </div>
        <div class="class-spots">
          <div class="spots-bar-wrap">
            <div class="spots-bar" style="width:${pct}%"></div>
          </div>
          <span style="font-size:0.8rem;color:var(--text-muted);white-space:nowrap">
            ${available} plazas
          </span>
        </div>
        <button class="btn btn-${available > 0 ? "outline" : "secondary"}"
                style="width:100%;justify-content:center;padding:0.6rem"
                onclick="${available > 0 ? `reserveClass('${cls.name}')` : "void(0)"}">
          ${available > 0 ? "Reservar" : "Completo"}
        </button>
      </div>
    `;
  }).join("");
}


/* =====================================================
   5. PRODUCTOS — tienda y home
   ===================================================== */

/*
  renderProductos(containerId, filter) — pinta las cards
  de productos con icono (versión sin imágenes reales).
  Reemplaza a: renderProducts() en app.js
*/
function renderProductos(containerId, filter = "all") {
  const container = document.getElementById(containerId);
  if (!container) return;

  const filtered = filter === "all"
    ? PRODUCTS
    : PRODUCTS.filter(p => p.category === filter);

  container.innerHTML = filtered.map(p => `
    <div class="product-card">
      <div class="product-img">
        ${p.badge ? `<span class="product-badge">${p.badge}</span>` : ""}
        ${p.icon}
      </div>
      <div class="product-info">
        <div class="product-category">${p.category}</div>
        <div class="product-name">${p.name}</div>
        <div class="product-price">
          <span class="price-current">${p.price.toFixed(2)}€</span>
          ${p.originalPrice
            ? `<span class="price-old">${p.originalPrice.toFixed(2)}€</span>`
            : ""}
        </div>
        <button class="btn btn-outline"
                style="width:100%;justify-content:center;
                       padding:0.5rem;margin-top:0.75rem;font-size:0.75rem"
                onclick="addToCart('${p.name}')">
          Añadir al carrito
        </button>
      </div>
    </div>
  `).join("");
}

/*
  renderProductosDestacados(containerId) — versión reducida
  para la home: solo los 4 primeros productos.
  Reemplaza a: renderFeaturedProducts() en app.js
*/
function renderProductosDestacados(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  const featured = PRODUCTS.slice(0, 4);

  container.innerHTML = featured.map(p => `
    <div class="product-card">
      <div class="product-img">
        ${p.badge ? `<span class="product-badge">${p.badge}</span>` : ""}
        ${p.icon}
      </div>
      <div class="product-info">
        <div class="product-category">${p.category}</div>
        <div class="product-name">${p.name}</div>
        <div class="product-price">
          <span class="price-current">${p.price.toFixed(2)}€</span>
          ${p.originalPrice
            ? `<span class="price-old">${p.originalPrice.toFixed(2)}€</span>`
            : ""}
        </div>
      </div>
    </div>
  `).join("");
}


/* =====================================================
   6. BLOG — listado de posts
   ===================================================== */

/*
  renderBlog(containerId) — pinta las cards del blog.
  Reemplaza a: renderBlog() en app.js
*/
function renderBlog(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  container.innerHTML = BLOG_POSTS.map(post => `
    <div class="blog-card">
      <div class="blog-img">${post.icon}</div>
      <div class="blog-body">
        <div class="blog-category">${post.category}</div>
        <h3>${post.title}</h3>
        <p>${post.excerpt}</p>
        <div class="blog-meta">
          <span>📅 ${post.date}</span>
          <span>⏱ ${post.readTime} lectura</span>
        </div>
      </div>
    </div>
  `).join("");
}


/* =====================================================
   7. INSTALACIONES — zonas del gimnasio
   ===================================================== */

/*
  renderInstalaciones(containerId) — pinta las cards
  de instalaciones.
  Reemplaza a: renderFacilities() en app.js
*/
function renderInstalaciones(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  container.innerHTML = FACILITIES.map(f => `
    <div class="facility-card">
      <div class="facility-icon">${f.icon}</div>
      <h3>${f.name}</h3>
      <p>${f.desc}</p>
    </div>
  `).join("");
}


/* =====================================================
   8. CARRITO — panel lateral
   ===================================================== */

/*
  renderCarrito() — dibuja los productos del carrito en
  el panel lateral y actualiza el total.
  Reemplaza a: renderCarritoPanel() en carrito.js
  NOTA: sigue llamándose renderCarritoPanel() en carrito.js
  para no romper nada — esta función es el alias organizado.
*/
function renderCarrito() {
  const itemsEl = document.getElementById("cart-items");
  const totalEl = document.getElementById("cart-total");
  if (!itemsEl) return;

  const carrito = getCarrito();

  if (carrito.length === 0) {
    itemsEl.innerHTML = `
      <div class="cart-empty">
        <span class="cart-empty-icon">&#128722;</span>
        <p>Tu carrito está vacío.<br>Añade productos desde la tienda.</p>
      </div>`;
  } else {
    itemsEl.innerHTML = carrito.map(item => `
      <div class="cart-item">
        <img class="cart-item-img" src="${item.img}" alt="${item.name}"
             onerror="this.style.background='var(--bg-secondary)';this.src=''">
        <div class="cart-item-info">
          <div class="cart-item-name">${item.name}</div>
          <div class="cart-item-price">${(item.price * item.quantity).toFixed(2)} €</div>
        </div>
        <div class="cart-item-controls">
          <button class="qty-btn" onclick="changeQuantity(${item.id}, -1)">−</button>
          <span class="qty-value">${item.quantity}</span>
          <button class="qty-btn" onclick="changeQuantity(${item.id}, +1)">+</button>
        </div>
        <button class="cart-item-remove" onclick="removeFromCarrito(${item.id})"
                title="Eliminar">&#x2715;</button>
      </div>
    `).join("");
  }

  // Total con descuento
  if (totalEl) {
    const subtotal   = getCarritoTotal();
    const descuento  = getCarritoDescuento();
    const totalFinal = getCarritoTotalFinal();

    if (descuento > 0 && carrito.length > 0) {
      totalEl.innerHTML = `
        <div style="font-size:0.78rem;color:var(--text-muted);
                    text-decoration:line-through;text-align:right">
          ${subtotal.toFixed(2)} €
        </div>
        <div style="font-size:0.78rem;color:var(--success);text-align:right">
          − ${descuento.toFixed(2)} € (5% socio)
        </div>
        <div>${totalFinal.toFixed(2)} €</div>
      `;
    } else {
      totalEl.textContent = subtotal.toFixed(2) + " €";
    }
  }
}


/* =====================================================
   9. RESUMEN DEL PEDIDO — checkout
   ===================================================== */

/*
  renderResumenPedido() — pinta las líneas de productos
  y los totales en la columna derecha del checkout.
  Reemplaza a: renderOrderSummary() en checkout.js
*/
function renderResumenPedido() {
  const summaryItems       = document.getElementById("summary-items");
  const summarySubtotal    = document.getElementById("summary-subtotal");
  const summaryDiscount    = document.getElementById("summary-discount");
  const summaryDiscountRow = document.getElementById("summary-discount-row");
  const summaryTotal       = document.getElementById("summary-total");

  if (!summaryItems) return;

  const carrito = getCarrito();

  if (carrito.length === 0) {
    showToast("Tu carrito está vacío. Añade productos primero.", "error");
    setTimeout(() => { window.location.href = "tienda.html"; }, 1500);
    return;
  }

  summaryItems.innerHTML = carrito.map(item => `
    <div class="summary-item">
      <div>
        <div class="s-name">${item.name}</div>
        <div class="s-qty">x${item.quantity}</div>
      </div>
      <div class="s-price">${(item.price * item.quantity).toFixed(2)} €</div>
    </div>
  `).join("");

  const subtotal   = getCarritoTotal();
  const descuento  = getCarritoDescuento();
  const totalFinal = getCarritoTotalFinal();

  summarySubtotal.textContent = subtotal.toFixed(2) + " €";

  if (summaryDiscountRow) {
    if (descuento > 0) {
      summaryDiscountRow.style.display = "";
      if (summaryDiscount) {
        summaryDiscount.textContent = "− " + descuento.toFixed(2) + " €";
      }
    } else {
      summaryDiscountRow.style.display = "none";
    }
  }

  summaryTotal.textContent = totalFinal.toFixed(2) + " €";
}


/* =====================================================
   10. PERFIL — datos del usuario
   ===================================================== */

/*
  renderDatosUsuario(user) — rellena los campos básicos
  del perfil: avatar, nombre, email, plan, desde cuándo.
  Reemplaza al bloque de getElementById/textContent en perfil.js
*/
function renderDatosUsuario(inicial, nombre, email, plan, desde) {
  setTextById("profile-initial",   inicial);
  setTextById("profile-name",      nombre);
  setTextById("profile-email",     email);
  setTextById("profile-plan",      plan);
  setTextById("profile-plan-stat", plan);
  setTextById("profile-since",     desde);
}

/*
  renderReservasPerfil(reserves) — pinta la lista de
  reservas en la pestaña del perfil.
  Reemplaza al bloque innerHTML de renderReservationsInProfile()
  en perfil.js
*/
function renderReservasPerfil(reserves) {
  const lista = document.getElementById("reservations-list");
  if (!lista) return;

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

  lista.innerHTML = reserves.map(r => `
    <div class="reservation-item">
      <div>
        <h4>${r.nomClasse || r.className || "—"}</h4>
        <p>${r.dataReserva || r.date || "—"}</p>
      </div>
      <span class="status-badge confirmed">Confirmada</span>
    </div>
  `).join("");
}

/*
  renderPedidosPerfil(vendes) — pinta el historial de
  pedidos del backend en el perfil.
  Reemplaza al bloque innerHTML de renderOrderHistory()
  en perfil.js
*/
function renderPedidosPerfil(vendes) {
  const container = document.getElementById("orders-list");
  if (!container) return;

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

  container.innerHTML = vendes.map(v => `
    <div class="order-card">
      <div class="order-card-header">
        <div>
          <div class="order-id">Pedido #${v.id}</div>
          <div class="order-date">${v.dataVenda || "—"}</div>
        </div>
        <span class="order-status">Completado</span>
      </div>
      <ul class="order-products">
        <li>
          <span class="p-name">${v.nomProducte || `Producto #${v.producteId}`} x${v.quantitat}</span>
        </li>
      </ul>
    </div>
  `).join("");
}

/*
  renderHistorialLocal(misPedidos) — pinta los pedidos
  guardados en localStorage (los del carrito).
  Reemplaza al bloque innerHTML de renderOldPurchases()
  en perfil.js
*/
function renderHistorialLocal(misPedidos) {
  const lista = document.getElementById("purchases-list");
  if (!lista) return;

  if (misPedidos.length === 0) {
    lista.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;padding:2rem">
        No tienes compras en el historial.
      </p>`;
    return;
  }

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
   11. ALERTA SIN PLAN — aviso modal para reservar
   ===================================================== */

/*
  renderAlertaSinPlan() — muestra un aviso flotante cuando
  el usuario intenta reservar sin tarifa activa.
  Reemplaza a: showNoPlantAlert() en app.js
*/
function renderAlertaSinPlan() {
  if (document.getElementById("no-plan-alert")) return;

  const alertEl = document.createElement("div");
  alertEl.id = "no-plan-alert";
  alertEl.style.cssText = `
    position:fixed; bottom:5rem; left:50%; transform:translateX(-50%);
    background:var(--bg-card); border:1px solid var(--primary);
    border-radius:12px; padding:1.25rem 1.5rem; max-width:360px; width:90%;
    box-shadow:0 8px 32px rgba(0,0,0,0.4); z-index:9999; text-align:center;
  `;
  alertEl.innerHTML = `
    <p style="margin:0 0 1rem;color:var(--text-primary);font-size:0.9rem">
      ⚠️ <strong>Necesitas tener una tarifa activa</strong> para reservar clases.
    </p>
    <div style="display:flex;gap:0.75rem;justify-content:center">
      <a href="tarifas.html" class="btn btn-primary"
         style="padding:0.5rem 1.25rem;font-size:0.8rem">
        Ver Tarifas
      </a>
      <button onclick="document.getElementById('no-plan-alert').remove()"
              class="btn btn-secondary" style="padding:0.5rem 1rem;font-size:0.8rem">
        Cerrar
      </button>
    </div>
  `;
  document.body.appendChild(alertEl);
  setTimeout(() => alertEl.remove(), 6000);
}