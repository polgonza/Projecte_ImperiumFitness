/* =====================================================
   IMPERIUM FITNESS - JavaScript principal
   Funcionalidades: Navbar, Auth, Formularios, Toast, Datos
   Todo en un solo archivo para simplificar el proyecto
   ===================================================== */

// =====================================================
// 1. DATOS DE LA APLICACIÓN (mock data)
//    En un proyecto real estos datos vendrían de una API
// =====================================================

const PLANS = [
  {
    name: "Pass Diario",
    price: "12.99",
    period: "día",
    features: ["Acceso puntual a 1 centro", "Zona de musculación completa", "Vestuarios y duchas", "WiFi gratuito"],
    notIncluded: ["Clases dirigidas", "Zona wellness", "Invitaciones", "Descuentos tienda"],
    popular: false,
    cta: "Comprar Pass"
  },
  {
    name: "Quota Flex",
    price: "34.99",
    period: "mes",
    features: ["Acceso a tu centro local", "1 clase dirigida por día", "15 invitaciones al año", "App de seguimiento", "Vestuarios premium", "5% dto. en tienda"],
    notIncluded: ["Acceso a todos los centros", "Zona wellness", "Entrenador personal"],
    popular: false,
    cta: "Escoger Plan"
  },
  {
    name: "Quota Prime",
    price: "54.99",
    period: "mes",
    features: ["Acceso a todos los centros", "2 clases dirigidas por día", "20 invitaciones al año", "Zona wellness & spa", "App premium con métricas", "Entrenador personal 1x/mes", "10% dto. en tienda", "Taquilla personal"],
    notIncluded: [],
    popular: true,
    cta: "Escoger Plan"
  }
];

const GYMS = [
  { id: 1, name: "Imperium Centro",  location: "Calle Gran Vía 45, Madrid",          capacity: 200, current: 67 },
  { id: 2, name: "Imperium Norte",   location: "Av. de la Ilustración 12, Madrid",   capacity: 150, current: 112 },
  { id: 3, name: "Imperium Sur",     location: "Calle de Alcalá 230, Madrid",        capacity: 180, current: 45 },
  { id: 4, name: "Imperium Este",    location: "Paseo de la Chopera 8, Madrid",      capacity: 160, current: 98 },
  { id: 5, name: "Imperium Oeste",   location: "Calle de Princesa 77, Madrid",       capacity: 220, current: 154 }
];

const CLASSES = [
  { id: 1, name: "CrossFit Intenso",     trainer: "Carlos Martínez",     time: "08:00", duration: "60 min", spots: 20, booked: 14, day: "Lunes",     category: "crossfit" },
  { id: 2, name: "Yoga Flow",            trainer: "Laura Sánchez",       time: "09:30", duration: "75 min", spots: 25, booked: 20, day: "Lunes",     category: "yoga" },
  { id: 3, name: "Spinning Power",       trainer: "Miguel Ángel Torres", time: "10:00", duration: "45 min", spots: 30, booked: 28, day: "Martes",    category: "spinning" },
  { id: 4, name: "HIIT Extremo",         trainer: "Ana García",          time: "11:00", duration: "30 min", spots: 20, booked: 8,  day: "Martes",    category: "hiit" },
  { id: 5, name: "Pilates Core",         trainer: "Elena Ruiz",          time: "17:00", duration: "60 min", spots: 15, booked: 12, day: "Miércoles", category: "pilates" },
  { id: 6, name: "CrossFit WOD",         trainer: "Carlos Martínez",     time: "18:30", duration: "60 min", spots: 20, booked: 18, day: "Miércoles", category: "crossfit" },
  { id: 7, name: "Yoga Restaurativo",    trainer: "Laura Sánchez",       time: "07:00", duration: "60 min", spots: 20, booked: 5,  day: "Jueves",    category: "yoga" },
  { id: 8, name: "Spinning Endurance",   trainer: "Miguel Ángel Torres", time: "19:00", duration: "50 min", spots: 30, booked: 25, day: "Jueves",    category: "spinning" },
  { id: 9, name: "HIIT Tabata",          trainer: "Ana García",          time: "08:00", duration: "30 min", spots: 20, booked: 16, day: "Viernes",   category: "hiit" },
  { id: 10, name: "Pilates Avanzado",    trainer: "Elena Ruiz",          time: "10:00", duration: "60 min", spots: 15, booked: 10, day: "Viernes",   category: "pilates" }
];

const PRODUCTS = [
  { id: 1, name: "Camiseta Imperium Pro",  price: 34.99, originalPrice: 44.99, category: "ropa",        icon: "👕", badge: "Nuevo" },
  { id: 2, name: "Leggins Power Fit",      price: 49.99, originalPrice: null,  category: "ropa",        icon: "🩳", badge: null },
  { id: 3, name: "Whey Protein Gold",      price: 39.99, originalPrice: null,  category: "suplementos", icon: "🥤", badge: "Top Ventas" },
  { id: 4, name: "Creatina Monohidrato",   price: 24.99, originalPrice: null,  category: "suplementos", icon: "💊", badge: null },
  { id: 5, name: "Sudadera Imperium Elite",price: 59.99, originalPrice: null,  category: "ropa",        icon: "🧥", badge: null },
  { id: 6, name: "Pre-Workout Ignite",     price: 29.99, originalPrice: null,  category: "suplementos", icon: "⚡", badge: "Nuevo" },
  { id: 7, name: "Tank Top Competition",   price: 27.99, originalPrice: null,  category: "ropa",        icon: "🎽", badge: null },
  { id: 8, name: "BCAA Recovery Plus",     price: 22.99, originalPrice: null,  category: "suplementos", icon: "💧", badge: null }
];

const BLOG_POSTS = [
  { id: 1, title: "5 Rutinas para Maximizar tu Fuerza",       excerpt: "Descubre las rutinas que están transformando los resultados de nuestros atletas.", category: "Fitness",    date: "01/04/2026", readTime: "5 min", icon: "💪" },
  { id: 2, title: "Nutrición Pre y Post Entreno",              excerpt: "Qué comer antes y después del gimnasio para optimizar tus resultados.",            category: "Nutrición",  date: "28/03/2026", readTime: "7 min", icon: "🥗" },
  { id: 3, title: "Evento: Imperium Challenge 2026",           excerpt: "Prepárate para la competición más esperada del año. Inscripciones abiertas.",      category: "Eventos",    date: "25/03/2026", readTime: "3 min", icon: "🏆" },
  { id: 4, title: "Los Beneficios del Yoga para Atletas",      excerpt: "Por qué los deportistas de élite integran yoga en su rutina diaria.",              category: "Fitness",    date: "20/03/2026", readTime: "6 min", icon: "🧘" },
  { id: 5, title: "Suplementación Inteligente: Guía Completa", excerpt: "Todo lo que necesitas saber sobre creatina, proteína y más.",                      category: "Nutrición",  date: "15/03/2026", readTime: "8 min", icon: "💊" },
  { id: 6, title: "Inauguración: Imperium Oeste",              excerpt: "Nuestro centro más grande abre sus puertas con tecnología de última generación.",   category: "Eventos",    date: "10/03/2026", readTime: "4 min", icon: "🏢" }
];

const FACILITIES = [
  { icon: "🏋️", name: "Bodybuilding",          desc: "Zona de musculación con más de 200 máquinas de última generación y peso libre completo." },
  { icon: "💪", name: "Powerlifting",           desc: "Plataformas olímpicas profesionales con barras calibradas y racks de competición." },
  { icon: "🥊", name: "Boxing",                 desc: "Ring profesional, sacos de boxeo, speed bags y zona de sparring con entrenadores certificados." },
  { icon: "🔥", name: "Condicionamiento",       desc: "Área funcional con trineos, battle ropes, kettlebells y circuitos metabólicos." },
  { icon: "🧊", name: "Recuperación",           desc: "Zona wellness con sauna, crioterapia, foam rolling y pistolas de masaje." },
  { icon: "🎯", name: "Actividades Dirigidas",  desc: "Salas climatizadas con sonido envolvente para clases de yoga, spinning, HIIT y más." }
];


// =====================================================
// 2. AUTENTICACIÓ REAL AMB JWT + BACKEND
//    Substitueix el sistema mock de localStorage
// =====================================================

const Auth = {

  /* Retorna les dades de l'usuari guardat (o null) */
  getUser() {
    const data = localStorage.getItem("imperium_user");
    return data ? JSON.parse(data) : null;
  },

  /* Guarda les dades de l'usuari en local */
  setUser(user) {
    localStorage.setItem("imperium_user", JSON.stringify(user));
  },

  /* Comprova si hi ha sessió activa (token present) */
  isLoggedIn() {
    return !!localStorage.getItem("imperium_token");
  },

  /* ── LOGIN REAL ──────────────────────────────────────
     Crida POST /api/auth/login al backend.
     Si té èxit, guarda el token i les dades bàsiques. */
  async login(email, password) {
    const result = await ApiAuth.login(email, password);

    if (result.ok) {
      // Descodifiquem el token JWT per obtenir les dades de l'usuari
      // El token és: header.payload.signature (base64)
      const payload = JSON.parse(atob(result.token.split(".")[1]));

      // Guardem les dades bàsiques de l'usuari al localStorage
      this.setUser({
        id:    payload.userId,
        name:  payload.sub,   // sub = email (el subject del JWT)
        email: payload.sub,
        roles: payload.roles
      });
    }

    return result;
  },

  /* ── REGISTRE REAL ───────────────────────────────────
     Crida POST /api/auth/registre al backend. */
  async register(nom, email, password) {
    const result = await ApiAuth.register(nom, email, password);

    if (result.ok) {
      // Igual que el login: descodifiquem el JWT i guardem l'usuari
      const payload = JSON.parse(atob(result.token.split(".")[1]));
      this.setUser({
        id:    payload.userId,
        name:  nom,           // el nom el tenim del formulari
        email: payload.sub,
        roles: payload.roles
      });
    }

    return result;
  },

  /* ── LOGOUT ──────────────────────────────────────────
     Neteja token i dades locals, redirigeix a l'inici. */
  logout() {
    localStorage.removeItem("imperium_token");
    localStorage.removeItem("imperium_user");
    localStorage.removeItem("imperium_carrito");
    window.location.href = "index.html";
  }
};

// =====================================================
// 3. NOTIFICACIONES TOAST
//    Muestra mensajes flotantes en la esquina
// =====================================================

function showToast(message, type = "success") {
  // Crea o busca el contenedor de toasts
  let container = document.getElementById("toast-container");
  if (!container) {
    container = document.createElement("div");
    container.id = "toast-container";
    container.className = "toast-container";
    document.body.appendChild(container);
  }

  // Crea el toast
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.innerHTML = `
    <span class="toast-icon">${type === "success" ? "✅" : "❌"}</span>
    <span>${message}</span>
  `;
  container.appendChild(toast);

  // Lo elimina después de 3 segundos
  setTimeout(() => {
    toast.remove();
  }, 3000);
}


// =====================================================
// 4. NAVBAR
//    Scroll effect + menú móvil + estado del usuario
// =====================================================

function initNavbar() {
  const navbar = document.getElementById("navbar");
  const navToggle = document.getElementById("nav-toggle");
  const navMobile = document.getElementById("nav-mobile");

  if (!navbar) return;

  // Efecto de scroll: añade clase "scrolled" al hacer scroll
  window.addEventListener("scroll", () => {
    if (window.scrollY > 20) {
      navbar.classList.add("scrolled");
    } else {
      navbar.classList.remove("scrolled");
    }
  });

  // Abrir/cerrar menú móvil
  if (navToggle && navMobile) {
    navToggle.addEventListener("click", () => {
      navMobile.classList.toggle("open");
    });
  }

  // Marca el link activo según la página actual
  const currentPage = window.location.pathname.split("/").pop() || "index.html";
  document.querySelectorAll(".nav-links a, .nav-mobile a").forEach(link => {
    const href = link.getAttribute("href");
    if (href === currentPage || (currentPage === "index.html" && href === "index.html")) {
      link.classList.add("active");
    }
  });

  // Actualizar navbar según sesión del usuario
  updateNavbarUser();
}

function updateNavbarUser() {
  // La lógica de render está en render.js
  // Esta función se mantiene para compatibilidad con llamadas existentes
  renderNavbarUser();
}


// =====================================================
// 5. RENDERIZADO DE SECCIONES (función de apoyo)
//    Genera HTML dinámico desde los datos
// =====================================================

// Renderiza los planes de tarifas — HTML generado en render.js
function renderPlans(containerId) {
  renderPlanes(containerId);
}

// Añade el plan elegido al carrito como producto especial y va al checkout
function buyPlan(planName, planPrice) {
  if (!Auth.getUser()) {
    showToast("Debes iniciar sesión para contratar un plan.", "error");
    setTimeout(() => window.location.href = "login.html", 1500);
    return;
  }
  // Crea un item especial de plan en el carrito
  const planItem = [{
    id: "plan_" + planName.replace(/\s/g, "_"),
    name: "Plan: " + planName,
    price: parseFloat(planPrice),
    img: "",
    quantity: 1,
    esPlan: true,        // marcador para que checkout.js lo sepa
    planName: planName
  }];
  // Sustituye el carrito por este único item (solo se compra 1 plan)
  localStorage.setItem("imperium_carrito", JSON.stringify(planItem));
  window.location.href = "checkout.html";
}

// Renderiza los gimnasios con su ocupación — HTML generado en render.js
function renderGymOccupancy(containerId) {
  renderOcupacion(containerId);
}

// Renderiza las clases con filtro de categoría — HTML generado en render.js
function renderClasses(containerId, filter = "all") {
  renderClases(containerId, filter);
}

function reserveClass(name) {
  const user = Auth.getUser();
  if (!user) {
    showToast("Debes iniciar sesión para reservar clases.", "error");
    setTimeout(() => window.location.href = "login.html", 1500);
    return;
  }
  // Comprueba si el usuario tiene tarifa activa
  if (!user.plan) {
    // Muestra el mensaje de aviso con botón a tarifas
    showNoPlantAlert();
    return;
  }
  showToast(`¡Reserva de "${name}" confirmada! 🎉`, "success");
}

// Muestra un mensaje de aviso cuando no hay tarifa activa — HTML en render.js
function showNoPlantAlert() {
  renderAlertaSinPlan();
}

// Renderiza los productos — HTML generado en render.js
function renderProducts(containerId, filter = "all") {
  renderProductos(containerId, filter);
}

function addToCart(name) {
  showToast(`"${name}" añadido al carrito 🛒`, "success");
}

// Renderiza el blog — HTML generado en render.js
// NOTA: en app.js no redefinimos renderBlog porque render.js
// ya define la función con el mismo nombre. La llamada en
// DOMContentLoaded llama directamente a la de render.js.

// Renderiza las instalaciones — HTML generado en render.js
function renderFacilities(containerId) {
  renderInstalaciones(containerId);
}

// Renderiza los 4 primeros productos en la home — HTML generado en render.js
function renderFeaturedProducts(containerId) {
  renderProductosDestacados(containerId);
}


// =====================================================
// 6. PERFIL DE USUARIO
// =====================================================

function renderProfile() {
  const user = Auth.getUser();
  if (!user) {
    window.location.href = "login.html";
    return;
  }

  // Rellenar info básica
  document.getElementById("profile-initial").textContent  = user.name.charAt(0).toUpperCase();
  document.getElementById("profile-name").textContent     = user.name;
  document.getElementById("profile-email").textContent    = user.email;
  document.getElementById("profile-plan").textContent     = user.plan;
  document.getElementById("profile-since").textContent    = user.memberSince;

  // Reservas
  const reservList = document.getElementById("reservations-list");
  if (reservList) {
    if (user.reservations && user.reservations.length > 0) {
      reservList.innerHTML = user.reservations.map(r => `
        <div class="reservation-item">
          <div>
            <h4>${r.className}</h4>
            <p>${r.date} · ${r.time}</p>
          </div>
          <span class="status-badge ${r.status === "Confirmada" ? "confirmed" : "completed"}">
            ${r.status}
          </span>
        </div>
      `).join("");
    } else {
      reservList.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">No tienes reservas aún.</p>`;
    }
  }

  // Compras
  const purchaseList = document.getElementById("purchases-list");
  if (purchaseList) {
    if (user.purchases && user.purchases.length > 0) {
      purchaseList.innerHTML = user.purchases.map(p => `
        <div class="purchase-item">
          <div>
            <h4>${p.product}</h4>
            <p>${p.date}</p>
          </div>
          <div style="text-align:right">
            <div style="font-weight:700">${p.amount.toFixed(2)}€</div>
            <span class="status-badge delivered">${p.status}</span>
          </div>
        </div>
      `).join("");
    } else {
      purchaseList.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">No has realizado compras aún.</p>`;
    }
  }
}

// Tabs del perfil (Reservas / Compras)
function initProfileTabs() {
  const tabs = document.querySelectorAll(".profile-tab");
  const contents = document.querySelectorAll(".tab-content");

  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      // Quita activo de todos
      tabs.forEach(t => t.classList.remove("active"));
      contents.forEach(c => c.classList.remove("active"));

      // Activa el clicado
      tab.classList.add("active");
      const target = document.getElementById(tab.dataset.tab);
      if (target) target.classList.add("active");
    });
  });
}


// =====================================================
// 7. FORMULARIS D'AUTENTICACIÓ CONNECTATS AL BACKEND
// =====================================================

function initLoginForm() {
  const form = document.getElementById("login-form");
  if (!form) return;

  form.addEventListener("submit", async function(e) {
    e.preventDefault();

    const email    = document.getElementById("login-email").value.trim();
    const password = document.getElementById("login-password").value;
    const alertEl  = document.getElementById("login-alert");
    const btn      = form.querySelector("button[type=submit]");

    if (!email || !password) {
      showAlert(alertEl, "Rellena todos los campos.", "error");
      return;
    }

    // Deshabilitem el botó mentre esperem la resposta
    btn.disabled = true;
    btn.textContent = "Entrando...";

    const result = await Auth.login(email, password);

    if (result.ok) {
      showAlert(alertEl, "¡Bienvenido! Redirigiendo...", "success");
      setTimeout(() => window.location.href = "index.html", 1200);
    } else {
      showAlert(alertEl, result.error, "error");
      btn.disabled = false;
      btn.textContent = "Entrar";
    }
  });
}

function initRegisterForm() {
  const form = document.getElementById("register-form");
  if (!form) return;

  form.addEventListener("submit", async function(e) {
    e.preventDefault();

    const nom      = document.getElementById("reg-name").value.trim();
    const email    = document.getElementById("reg-email").value.trim();
    const password = document.getElementById("reg-password").value;
    const confirm  = document.getElementById("reg-confirm").value;
    const alertEl  = document.getElementById("reg-alert");
    const btn      = form.querySelector("button[type=submit]");

    // Validacions del client
    if (!nom || !email || !password || !confirm) {
      showAlert(alertEl, "Rellena todos los campos.", "error");
      return;
    }
    if (password.length < 6) {
      showAlert(alertEl, "La contraseña debe tener al menos 6 caracteres.", "error");
      return;
    }
    if (password !== confirm) {
      showAlert(alertEl, "Las contraseñas no coinciden.", "error");
      return;
    }

    btn.disabled = true;
    btn.textContent = "Creando cuenta...";

    const result = await Auth.register(nom, email, password);

    if (result.ok) {
      showAlert(alertEl, "¡Cuenta creada! Redirigiendo...", "success");
      setTimeout(() => window.location.href = "index.html", 1200);
    } else {
      showAlert(alertEl, result.error, "error");
      btn.disabled = false;
      btn.textContent = "Crear Cuenta";
    }
  });
}

function initRecoverForm() {
  const form = document.getElementById("recover-form");
  if (!form) return;

  form.addEventListener("submit", function(e) {
    e.preventDefault();

    const email = document.getElementById("recover-email").value.trim();
    const alert = document.getElementById("recover-alert");

    if (!email) {
      showAlert(alert, "Introduce tu email.", "error");
      return;
    }

    // Simulación: simplemente muestra un mensaje de éxito
    showAlert(alert, `Hemos enviado un enlace de recuperación a ${email}.`, "success");
    form.reset();
  });
}

// Muestra / oculta un mensaje de alerta en un formulario
function showAlert(alertEl, msg, type) {
  if (!alertEl) return;
  alertEl.textContent = msg;
  alertEl.className = `alert ${type} show`;
}


// =====================================================
// 8. TOGGLE CONTRASEÑA (mostrar/ocultar)
// =====================================================

function initPasswordToggle() {
  document.querySelectorAll(".toggle-password").forEach(btn => {
    btn.addEventListener("click", function() {
      const targetId = this.dataset.target;
      const input = document.getElementById(targetId);
      if (!input) return;

      if (input.type === "password") {
        input.type = "text";
        this.textContent = "🙈";
      } else {
        input.type = "password";
        this.textContent = "👁";
      }
    });
  });
}


// =====================================================
// 9. FILTROS (actividades y tienda)
// =====================================================

function initFilters(filterSelector, onFilterChange) {
  document.querySelectorAll(filterSelector).forEach(btn => {
    btn.addEventListener("click", function() {
      // Quita activo de todos
      document.querySelectorAll(filterSelector).forEach(b => b.classList.remove("active"));
      // Activa este
      this.classList.add("active");
      // Llama al callback con el valor del filtro
      onFilterChange(this.dataset.filter);
    });
  });
}


// =====================================================
// 10. FORMULARIO DE CONTACTO
// =====================================================

function initContactForm() {
  const form = document.getElementById("contact-form");
  if (!form) return;

  form.addEventListener("submit", function(e) {
    e.preventDefault();
    showToast("¡Mensaje enviado! Te responderemos en menos de 24h 📩", "success");
    form.reset();
  });
}

// También para el mini-form del home
function initHomeContactForm() {
  const form = document.getElementById("home-contact-form");
  if (!form) return;

  form.addEventListener("submit", function(e) {
    e.preventDefault();
    showToast("¡Mensaje enviado! Te contactaremos pronto. 👍", "success");
    form.reset();
  });
}

// Newsletter
function initNewsletter() {
  document.querySelectorAll(".newsletter-btn").forEach(btn => {
    btn.addEventListener("click", function() {
      const inputId = this.dataset.input;
      const input = inputId ? document.getElementById(inputId) : this.previousElementSibling;
      if (input && input.value) {
        showToast("¡Suscrito correctamente! 📬", "success");
        input.value = "";
      } else {
        showToast("Introduce un email válido.", "error");
      }
    });
  });
}


// =====================================================
// 11. INICIALIZACIÓN GENERAL
//    Se ejecuta cuando carga la página
// =====================================================

document.addEventListener("DOMContentLoaded", function() {
  // Navbar siempre
  initNavbar();

  // Auth forms
  initLoginForm();
  initRegisterForm();
  initRecoverForm();
  initPasswordToggle();

  // Contacto
  initContactForm();
  initHomeContactForm();
  initNewsletter();

  // Renderiza secciones según la página
  const page = window.location.pathname.split("/").pop() || "index.html";

  // NOTA: index.html y tienda.html los gestiona carrito.js
  // (productos con imágenes reales de Unsplash)

  if (page === "tarifas.html") {
    renderPlans("plans-grid");
  }

  if (page === "ocupacion.html") {
    renderGymOccupancy("gyms-grid");
  }

  if (page === "actividades.html") {
    // actividades.html usa actividades.js — nada que hacer aquí
  }

  if (page === "blog.html") {
    renderBlog("blog-grid");
  }

  if (page === "instalaciones.html") {
    renderFacilities("facilities-grid");
  }

  // perfil.html usa perfil.js — no llamamos a renderProfile() desde aquí
});
