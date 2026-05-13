/* =====================================================
   IMPERIUM FITNESS — carrito.js
   ===================================================== */

const CATEGORY_IMGS = {
  "Roba":       "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=300&fit=crop",
  "Suplement":  "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400&h=300&fit=crop",
  "Accesoris":  "https://images.unsplash.com/photo-1517344368193-41552b6ad3f5?w=400&h=300&fit=crop",
  "ropa":       "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=300&fit=crop",
  "suplementos":"https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400&h=300&fit=crop",
  "accesorios": "https://images.unsplash.com/photo-1517344368193-41552b6ad3f5?w=400&h=300&fit=crop",
  "default":    "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400&h=300&fit=crop"
};

let PRODUCTS_IMG = [];

function producteBackendToLocal(p) {
  return {
    id:            p.id,
    name:          p.nom,
    price:         parseFloat(p.preu),
    originalPrice: null,
    category:      p.categoria || "default",
    badge:         p.estoc <= 5 ? (I18n.idioma === "ca" ? "Últimes unitats" : "Last units") : null,
    estoc:         p.estoc,
    img:           CATEGORY_IMGS[p.categoria] || CATEGORY_IMGS["default"]
  };
}

function getCarrito() {
  const data = localStorage.getItem("imperium_carrito");
  return data ? JSON.parse(data) : [];
}

function saveCarrito(carrito) {
  localStorage.setItem("imperium_carrito", JSON.stringify(carrito));
}

function getCarritoCount() {
  return getCarrito().reduce(function(total, item) {
    return total + item.quantity;
  }, 0);
}

function getCarritoTotal() {
  return getCarrito().reduce(function(total, item) {
    return total + (item.price * item.quantity);
  }, 0);
}

function getCarritoDescuento() {
  const user = typeof Auth !== "undefined" ? Auth.getUser() : null;
  if (!user) return 0;
  const subtotal  = getCarritoTotal();
  const tarifaNom = (user.tarifaNom || "").toLowerCase();
  if (tarifaNom.includes("prime")) return Math.round(subtotal * 0.10 * 100) / 100;
  if (tarifaNom.includes("flex"))  return Math.round(subtotal * 0.05 * 100) / 100;
  return 0;
}

function getCarritoTotalFinal() {
  return Math.round((getCarritoTotal() - getCarritoDescuento()) * 100) / 100;
}

function addToCarrito(productId) {
  const producto = PRODUCTS_IMG.find(function(p) { return p.id === productId; });
  if (!producto) return;

  const carrito   = getCarrito();
  const existente = carrito.find(function(item) { return item.id === productId; });

  if (existente) {
    existente.quantity += 1;
  } else {
    carrito.push({
      id:       producto.id,
      name:     producto.name,
      price:    producto.price,
      img:      producto.img,
      quantity: 1
    });
  }

  saveCarrito(carrito);
  renderCarritoPanel();
  updateCartBadge();
  showToast(t("toast.afegitCarret", [producto.name]), "success");
}

function removeFromCarrito(productId) {
  let carrito = getCarrito();
  carrito = carrito.filter(function(item) { return item.id !== productId; });
  saveCarrito(carrito);
  renderCarritoPanel();
  updateCartBadge();
}

function changeQuantity(productId, delta) {
  const carrito = getCarrito();
  const item    = carrito.find(function(i) { return i.id === productId; });
  if (!item) return;
  item.quantity += delta;
  if (item.quantity <= 0) {
    removeFromCarrito(productId);
    return;
  }
  saveCarrito(carrito);
  renderCarritoPanel();
  updateCartBadge();
}

function vaciarCarrito() {
  localStorage.removeItem("imperium_carrito");
  renderCarritoPanel();
  updateCartBadge();
}

function renderCarritoPanel() {
  renderCarrito();
}

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

function openCart() {
  document.getElementById("cart-drawer")?.classList.add("open");
  document.getElementById("cart-backdrop")?.classList.add("open");
}

function closeCart() {
  document.getElementById("cart-drawer")?.classList.remove("open");
  document.getElementById("cart-backdrop")?.classList.remove("open");
}

function renderProductosConImagenes(containerId, filter) {
  const container = document.getElementById(containerId);
  if (!container) return;

  const lista = filter === "all" || !filter
    ? PRODUCTS_IMG
    : PRODUCTS_IMG.filter(function(p) {
        const cat  = (p.category || "").toLowerCase();
        const f    = (filter || "").toLowerCase();
        const mapa = { "roba": "ropa", "suplement": "suplementos", "accesoris": "accesorios" };
        return (mapa[cat] || cat) === f;
      });

  const btnText = I18n.idioma === "ca" ? "Afegeix al carret" : "Add to cart";

  container.innerHTML = lista.map(function(p) {
    return `
      <div class="product-card">
        <div class="product-img" style="padding:0;overflow:hidden">
          ${p.badge ? `<span class="product-badge">${p.badge}</span>` : ""}
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
            ${p.originalPrice ? `<span class="price-old">${p.originalPrice.toFixed(2)} €</span>` : ""}
          </div>
          <button
            class="btn btn-primary"
            style="width:100%;justify-content:center;padding:0.55rem;margin-top:0.75rem;font-size:0.78rem"
            onclick="addToCarrito(${p.id})">
            ${btnText}
          </button>
        </div>
      </div>
    `;
  }).join("");
}

function renderFeaturedProductsConImagenes(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  const featured = PRODUCTS_IMG.slice(0, 4);
  const btnText  = I18n.idioma === "ca" ? "Afegeix al carret" : "Add to cart";

  container.innerHTML = featured.map(function(p) {
    return `
      <div class="product-card">
        <div class="product-img" style="padding:0;overflow:hidden">
          ${p.badge ? `<span class="product-badge">${p.badge}</span>` : ""}
          <img src="${p.img}" alt="${p.name}"
               style="width:100%;height:160px;object-fit:cover;display:block"
               loading="lazy">
        </div>
        <div class="product-info">
          <div class="product-category">${p.category}</div>
          <div class="product-name">${p.name}</div>
          <div class="product-price">
            <span class="price-current">${p.price.toFixed(2)} €</span>
            ${p.originalPrice ? `<span class="price-old">${p.originalPrice.toFixed(2)} €</span>` : ""}
          </div>
          <button
            class="btn btn-primary"
            style="width:100%;justify-content:center;padding:0.55rem;margin-top:0.75rem;font-size:0.78rem"
            onclick="addToCarrito(${p.id})">
            ${btnText}
          </button>
        </div>
      </div>
    `;
  }).join("");
}

// Fragment suggerit per assistent IA - revisar i adaptar
// Insereix el botó del carret a la navbar — funció separada perquè
// renderNavbarUser() sobreescriu nav-user-area en canviar d'idioma
function insertarBotoCarret() {
  const navUserArea = document.getElementById("nav-user-area");
  if (!navUserArea) return;
  if (document.getElementById("cart-open-btn")) return; // ja existeix, no dupliquem
  const cartBtn     = document.createElement("a");
  cartBtn.href      = "#";
  cartBtn.className = "cart-icon-btn";
  cartBtn.id        = "cart-open-btn";
  cartBtn.title     = t("carret.titol");
  cartBtn.innerHTML = `&#128722;<span class="cart-badge" id="cart-badge">0</span>`;
  cartBtn.addEventListener("click", function(e) {
    e.preventDefault();
    renderCarritoPanel();
    openCart();
  });
  navUserArea.insertBefore(cartBtn, navUserArea.firstChild);
  updateCartBadge();
}

// Quan canvia l'idioma, renderNavbarUser() elimina el botó → el reinsertem
document.addEventListener("idioma:canvi", function() {
  insertarBotoCarret();
  // Re-renderitza la botiga si estem a tienda.html
  const page = window.location.pathname.split("/").pop() || "index.html";
  if (page === "tienda.html") renderProductosConImagenes("products-grid", _filtreActiu || "all");
  if (page === "index.html" || page === "") renderFeaturedProductsConImagenes("featured-products");
});

let _filtreActiu = "all";

document.addEventListener("DOMContentLoaded", async function() {

  /* --- Carrega productes del backend --- */
  try {
    const productes = await ApiProductes.getAll();
    PRODUCTS_IMG = productes.map(producteBackendToLocal);
  } catch (e) {
    console.error("Error carregant productes:", e);
    PRODUCTS_IMG = [];
  }

  /* --- Carrega la tarifa de l'usuari per aplicar descompte --- */
  if (typeof ApiUsuari !== "undefined" && Auth.isLoggedIn()) {
    await ApiUsuari.getTarifaActiva();
  }

  /* --- Insereix el botó del carret al navbar --- */
  insertarBotoCarret();

  const closeBtn = document.getElementById("cart-close-btn");
  if (closeBtn) closeBtn.addEventListener("click", closeCart);

  const backdrop = document.getElementById("cart-backdrop");
  if (backdrop) backdrop.addEventListener("click", closeCart);

  const checkoutBtn = document.getElementById("cart-checkout-btn");
  if (checkoutBtn) {
    checkoutBtn.addEventListener("click", function() {
      closeCart();
      if (getCarritoCount() === 0) {
        showToast(t("carret.buit"), "error");
        return;
      }
      window.location.href = "checkout.html";
    });
  }

  updateCartBadge();
  renderCarritoPanel();

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
        _filtreActiu = this.dataset.filter;
        renderProductosConImagenes("products-grid", _filtreActiu);
      });
    });
  }
});