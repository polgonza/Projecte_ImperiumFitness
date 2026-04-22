/* =====================================================
   IMPERIUM FITNESS — carrito.js
   Sistema de Carrito de Compra
   -------------------------------------------------------
   Este archivo se carga después de app.js.
   Gestiona todo lo relacionado con el carrito:
     1. Datos de productos con imágenes (Unsplash)
     2. Funciones del carrito (añadir, eliminar, totales)
     3. Renderizado del panel lateral del carrito
     4. Botón del carrito en el navbar
     5. Renderizado de productos en la tienda (con imágenes)
   ===================================================== */


// =====================================================
// 1. IMATGES PER CATEGORIA (fallback quan no hi ha imatge al backend)
// =====================================================

const CATEGORY_IMGS = {
  "Roba":       "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=300&fit=crop",
  "Suplement":  "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400&h=300&fit=crop",
  "Accesoris":  "https://images.unsplash.com/photo-1517344368193-41552b6ad3f5?w=400&h=300&fit=crop",
  "ropa":       "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=300&fit=crop",
  "suplementos":"https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400&h=300&fit=crop",
  "accesorios": "https://images.unsplash.com/photo-1517344368193-41552b6ad3f5?w=400&h=300&fit=crop",
  "default":    "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400&h=300&fit=crop"
};

// Array que s'omplirà amb les dades reals del backend
let PRODUCTS_IMG = [];

/* Converteix un producte del backend al format que usa el carrito */
function producteBackendToLocal(p) {
  return {
    id:            p.id,
    name:          p.nom,
    price:         parseFloat(p.preu),
    originalPrice: null,
    category:      p.categoria || "default",
    badge:         p.estoc <= 5 ? "Últimas unidades" : null,
    estoc:         p.estoc,
    img:           CATEGORY_IMGS[p.categoria] || CATEGORY_IMGS["default"]
  };
}


/* =====================================================
   2. FUNCIONES DEL CARRITO
   El carrito se guarda en localStorage con la clave
   "imperium_carrito". Es un array de objetos:
   { id, name, price, img, quantity }
   ===================================================== */

/* Carga el carrito desde localStorage */
function getCarrito() {
  const data = localStorage.getItem("imperium_carrito");
  return data ? JSON.parse(data) : [];
}

/* Guarda el carrito en localStorage */
function saveCarrito(carrito) {
  localStorage.setItem("imperium_carrito", JSON.stringify(carrito));
}

/* Cuenta el total de unidades en el carrito */
function getCarritoCount() {
  const carrito = getCarrito();
  // Suma las cantidades de todos los items
  return carrito.reduce(function(total, item) {
    return total + item.quantity;
  }, 0);
}

/* Calcula el precio total del carrito */
function getCarritoTotal() {
  const carrito = getCarrito();
  return carrito.reduce(function(total, item) {
    return total + (item.price * item.quantity);
  }, 0);
}

/* Calcula el descuento (5% si está logueado, 0 si no) */
function getCarritoDescuento() {
  const user = typeof Auth !== "undefined" ? Auth.getUser() : null;
  if (!user) return 0;
  const subtotal = getCarritoTotal();
  return Math.round(subtotal * 0.05 * 100) / 100;
}

/* Total final tras descuento */
function getCarritoTotalFinal() {
  return Math.round((getCarritoTotal() - getCarritoDescuento()) * 100) / 100;
}

/*
  Añade un producto al carrito.
  - Si ya existe ese producto, aumenta su cantidad en 1.
  - Si es nuevo, lo añade con quantity: 1.
*/
function addToCarrito(productId) {
  const producto = PRODUCTS_IMG.find(function(p) { return p.id === productId; });
  if (!producto) return;

  const carrito = getCarrito();

  // Busca si ya está en el carrito
  const existente = carrito.find(function(item) { return item.id === productId; });

  if (existente) {
    // Ya existe: aumenta cantidad
    existente.quantity += 1;
  } else {
    // Nuevo: lo añade con cantidad 1
    carrito.push({
      id:       producto.id,
      name:     producto.name,
      price:    producto.price,
      img:      producto.img,
      quantity: 1
    });
  }

  saveCarrito(carrito);

  // Refresca la UI del carrito y el badge del navbar
  renderCarritoPanel();
  updateCartBadge();
  showToast('"' + producto.name + '" añadido al carrito.', "success");
}

/* Elimina completamente un producto del carrito */
function removeFromCarrito(productId) {
  let carrito = getCarrito();
  carrito = carrito.filter(function(item) { return item.id !== productId; });
  saveCarrito(carrito);
  renderCarritoPanel();
  updateCartBadge();
}

/* Cambia la cantidad de un producto (+1 o -1) */
function changeQuantity(productId, delta) {
  const carrito = getCarrito();
  const item = carrito.find(function(i) { return i.id === productId; });

  if (!item) return;

  item.quantity += delta;

  // Si la cantidad llega a 0, elimina el producto
  if (item.quantity <= 0) {
    removeFromCarrito(productId);
    return;
  }

  saveCarrito(carrito);
  renderCarritoPanel();
  updateCartBadge();
}

/* Vacía el carrito (se usa tras completar el pago) */
function vaciarCarrito() {
  localStorage.removeItem("imperium_carrito");
  renderCarritoPanel();
  updateCartBadge();
}


/* =====================================================
   3. RENDERIZADO DEL PANEL DEL CARRITO
   ===================================================== */

/* Dibuja los productos dentro del panel lateral */
function renderCarritoPanel() {
  const itemsEl  = document.getElementById("cart-items");
  const totalEl  = document.getElementById("cart-total");
  if (!itemsEl) return;   // Si no existe el panel en esta página, salimos

  const carrito = getCarrito();

  if (carrito.length === 0) {
    // Carrito vacío
    itemsEl.innerHTML = `
      <div class="cart-empty">
        <span class="cart-empty-icon">&#128722;</span>
        <p>Tu carrito está vacío.<br>Añade productos desde la tienda.</p>
      </div>`;
  } else {
    // Dibuja cada producto
    itemsEl.innerHTML = carrito.map(function(item) {
      return `
        <div class="cart-item">
          <!-- Imagen del producto -->
          <img class="cart-item-img" src="${item.img}" alt="${item.name}"
               onerror="this.style.background='var(--bg-secondary)';this.src=''">

          <div class="cart-item-info">
            <div class="cart-item-name">${item.name}</div>
            <div class="cart-item-price">${(item.price * item.quantity).toFixed(2)} €</div>
          </div>

          <!-- Controles de cantidad -->
          <div class="cart-item-controls">
            <button class="qty-btn" onclick="changeQuantity(${item.id}, -1)">−</button>
            <span class="qty-value">${item.quantity}</span>
            <button class="qty-btn" onclick="changeQuantity(${item.id}, +1)">+</button>
          </div>

          <!-- Botón eliminar -->
          <button class="cart-item-remove" onclick="removeFromCarrito(${item.id})"
                  title="Eliminar">&#x2715;</button>
        </div>
      `;
    }).join("");
  }

  // Actualiza el total (con descuento si está logueado)
  if (totalEl) {
    const subtotal   = getCarritoTotal();
    const descuento  = getCarritoDescuento();
    const totalFinal = getCarritoTotalFinal();

    if (descuento > 0 && carrito.length > 0) {
      totalEl.innerHTML = `
        <div style="font-size:0.78rem;color:var(--text-muted);text-decoration:line-through;text-align:right">
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

/* Actualiza el número sobre el icono del carrito en el navbar */
function updateCartBadge() {
  const badge = document.getElementById("cart-badge");
  if (!badge) return;

  const count = getCarritoCount();

  if (count > 0) {
    badge.textContent = count > 99 ? "99+" : count;
    badge.classList.add("visible");
  } else {
    badge.classList.remove("visible");
  }
}

/* Abre el panel del carrito */
function openCart() {
  document.getElementById("cart-drawer")?.classList.add("open");
  document.getElementById("cart-backdrop")?.classList.add("open");
}

/* Cierra el panel del carrito */
function closeCart() {
  document.getElementById("cart-drawer")?.classList.remove("open");
  document.getElementById("cart-backdrop")?.classList.remove("open");
}


/* =====================================================
   4. RENDERIZADO DE PRODUCTOS EN LA TIENDA
   Reemplaza la función de app.js para usar imágenes
   ===================================================== */

function renderProductosConImagenes(containerId, filter) {
  const container = document.getElementById(containerId);
  if (!container) return;

  // Filtra por categoría
  const lista = filter === "all" || !filter
    ? PRODUCTS_IMG
    : PRODUCTS_IMG.filter(function(p) { return p.category === filter; });

  container.innerHTML = lista.map(function(p) {
    return `
      <div class="product-card">
        <!-- Imagen real (Unsplash) -->
        <div class="product-img" style="padding:0;overflow:hidden">
          ${p.badge ? '<span class="product-badge">' + p.badge + '</span>' : ""}
          <img src="${p.img}" alt="${p.name}"
               style="width:100%;height:160px;object-fit:cover;display:block"
               loading="lazy"
               onerror="this.parentElement.style.background='var(--bg-secondary)'">
        </div>
        <div class="product-info">
          <div class="product-category">${p.category}</div>
          <div class="product-name">${p.name}</div>
          <div class="product-price">
            <span class="price-current">${p.price.toFixed(2)} €</span>
            ${p.originalPrice
              ? '<span class="price-old">' + p.originalPrice.toFixed(2) + ' €</span>'
              : ""}
          </div>
          <!-- Botón añadir al carrito -->
          <button
            class="btn btn-primary"
            style="width:100%;justify-content:center;padding:0.55rem;margin-top:0.75rem;font-size:0.78rem"
            onclick="addToCarrito(${p.id})">
            Añadir al carrito
          </button>
        </div>
      </div>
    `;
  }).join("");
}

/* Versión para la home: solo muestra los 4 primeros */
function renderFeaturedProductsConImagenes(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  const featured = PRODUCTS_IMG.slice(0, 4);

  container.innerHTML = featured.map(function(p) {
    return `
      <div class="product-card">
        <div class="product-img" style="padding:0;overflow:hidden">
          ${p.badge ? '<span class="product-badge">' + p.badge + '</span>' : ""}
          <img src="${p.img}" alt="${p.name}"
               style="width:100%;height:160px;object-fit:cover;display:block"
               loading="lazy">
        </div>
        <div class="product-info">
          <div class="product-category">${p.category}</div>
          <div class="product-name">${p.name}</div>
          <div class="product-price">
            <span class="price-current">${p.price.toFixed(2)} €</span>
            ${p.originalPrice
              ? '<span class="price-old">' + p.originalPrice.toFixed(2) + ' €</span>'
              : ""}
          </div>
          <button
            class="btn btn-primary"
            style="width:100%;justify-content:center;padding:0.55rem;margin-top:0.75rem;font-size:0.78rem"
            onclick="addToCarrito(${p.id})">
            Añadir al carrito
          </button>
        </div>
      </div>
    `;
  }).join("");
}


/* =====================================================
   5. ARRANQUE — càrrega asíncrona de productes
   ===================================================== */

document.addEventListener("DOMContentLoaded", async function() {

  /* --- Carrega els productes del backend --- */
  try {
    const productes = await ApiProductes.getAll();
    PRODUCTS_IMG = productes.map(producteBackendToLocal);
  } catch (e) {
    console.error("Error carregant productes:", e);
    PRODUCTS_IMG = []; // fallback buit
  }

  /* --- Inyecta el icono del carrito en el navbar --- */
  const navUserArea = document.getElementById("nav-user-area");
  if (navUserArea) {
    const cartBtn = document.createElement("a");
    cartBtn.href      = "#";
    cartBtn.className = "cart-icon-btn";
    cartBtn.id        = "cart-open-btn";
    cartBtn.title     = "Ver carrito";
    cartBtn.innerHTML = `
      &#128722;
      <span class="cart-badge" id="cart-badge">0</span>
    `;
    cartBtn.addEventListener("click", function(e) {
      e.preventDefault();
      renderCarritoPanel();
      openCart();
    });
    navUserArea.insertBefore(cartBtn, navUserArea.firstChild);
  }

  /* --- Botó tancar panel --- */
  const closeBtn = document.getElementById("cart-close-btn");
  if (closeBtn) closeBtn.addEventListener("click", closeCart);

  /* --- Clic al fons fosc tanca el panel --- */
  const backdrop = document.getElementById("cart-backdrop");
  if (backdrop) backdrop.addEventListener("click", closeCart);

  /* --- Botó "Finalizar Compra" --- */
  const checkoutBtn = document.getElementById("cart-checkout-btn");
  if (checkoutBtn) {
    checkoutBtn.addEventListener("click", function() {
      closeCart();
      if (getCarritoCount() === 0) {
        showToast("Tu carrito está vacío.", "error");
        return;
      }
      window.location.href = "checkout.html";
    });
  }

  /* --- Actualitza el badge --- */
  updateCartBadge();
  renderCarritoPanel();

  /* --- Renderitza productes segons la pàgina --- */
  const page = window.location.pathname.split("/").pop() || "index.html";

  if (page === "index.html" || page === "") {
    renderFeaturedProductsConImagenes("featured-products");
  }

  if (page === "tienda.html") {
    renderProductosConImagenes("products-grid", "all");

    document.querySelectorAll(".filter-tab").forEach(function(btn) {
      btn.addEventListener("click", function() {
        document.querySelectorAll(".filter-tab")
                .forEach(function(b) { b.classList.remove("active"); });
        this.classList.add("active");
        renderProductosConImagenes("products-grid", this.dataset.filter);
      });
    });
  }
});
