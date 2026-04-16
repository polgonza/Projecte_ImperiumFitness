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
// 2. AUTENTICACIÓN CON LOCALSTORAGE
//    Simula login / registro / logout sin backend real
// =====================================================

const Auth = {
  // Obtiene el usuario guardado en localStorage (o null si no hay sesión)
  getUser() {
    const data = localStorage.getItem("imperium_user");
    return data ? JSON.parse(data) : null;
  },

  // Guarda el usuario en localStorage
  setUser(user) {
    localStorage.setItem("imperium_user", JSON.stringify(user));
  },

  // Elimina la sesión
  logout() {
    localStorage.removeItem("imperium_user");
    window.location.href = "index.html";
  },

  // Busca usuarios registrados
  getUsers() {
    const data = localStorage.getItem("imperium_users");
    return data ? JSON.parse(data) : [];
  },

  // Guarda lista de usuarios
  saveUsers(users) {
    localStorage.setItem("imperium_users", JSON.stringify(users));
  },

  // Registra un nuevo usuario
  register(name, email, password) {
    const users = this.getUsers();
    // Comprueba si el email ya existe
    if (users.find(u => u.email === email)) {
      return { ok: false, msg: "Este email ya está registrado." };
    }
    const newUser = {
      id: "usr_" + Date.now(),
      name,
      email,
      password, // En un proyecto real NUNCA guardar contraseñas en texto plano
      plan: null,           // Sin tarifa al registrarse
      memberSince: new Date().toLocaleDateString("es-ES"),
      reservations: [],
      purchases: []
    };
    users.push(newUser);
    this.saveUsers(users);
    this.setUser(newUser);
    return { ok: true };
  },

  // Actualiza los datos del usuario en sesión y en la lista de usuarios
  updateUser(updatedUser) {
    this.setUser(updatedUser);
    const users = this.getUsers();
    const idx = users.findIndex(u => u.email === updatedUser.email);
    if (idx !== -1) {
      users[idx] = updatedUser;
      this.saveUsers(users);
    }
  },

  // Verifica login
  login(email, password) {
    const users = this.getUsers();
    const user = users.find(u => u.email === email && u.password === password);
    if (!user) {
      return { ok: false, msg: "Email o contraseña incorrectos." };
    }
    this.setUser(user);
    return { ok: true };
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
  const user = Auth.getUser();
  const navUserArea = document.getElementById("nav-user-area");
  const navMobileUser = document.getElementById("nav-mobile-user");

  if (!navUserArea) return;

  if (user) {
    // Muestra el nombre y botón de logout
    navUserArea.innerHTML = `
      <a href="perfil.html" class="btn-user">
        <div class="avatar">${user.name.charAt(0).toUpperCase()}</div>
        ${user.name.split(" ")[0]}
      </a>
      <button onclick="Auth.logout()" class="btn btn-secondary" style="padding:0.4rem 0.9rem;font-size:0.75rem">Salir</button>
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
      <a href="tarifas.html" class="btn btn-primary" style="padding:0.45rem 1.25rem;font-size:0.75rem">Únete</a>
    `;
    if (navMobileUser) {
      navMobileUser.innerHTML = `
        <a href="login.html">Iniciar Sesión</a>
        <a href="register.html">Registrarse</a>
      `;
    }
  }
}


// =====================================================
// 5. RENDERIZADO DE SECCIONES (función de apoyo)
//    Genera HTML dinámico desde los datos
// =====================================================

// Renderiza los planes de tarifas
function renderPlans(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  const user = Auth.getUser();
  const userPlan = user ? user.plan : null;

  container.innerHTML = PLANS.map(plan => {
    const isCurrent = userPlan && userPlan === plan.name;

    return `
    <div class="plan-card ${plan.popular ? "popular" : ""}">
      ${plan.popular ? '<div class="popular-badge">⭐ Más Popular</div>' : ""}
      ${isCurrent ? '<div class="popular-badge" style="background:var(--success)">✓ Plan Actual</div>' : ""}
      <div class="plan-header">
        <div class="plan-name">${plan.name}</div>
        <div class="plan-price">
          <span class="amount">${plan.price}€</span>
          <span class="period">/${plan.period}</span>
        </div>
      </div>
      <ul class="plan-features">
        ${plan.features.map(f => `
          <li>
            <span class="check">✓</span>
            <span>${f}</span>
          </li>
        `).join("")}
        ${plan.notIncluded.map(f => `
          <li class="excluded">
            <span class="check">✗</span>
            <span>${f}</span>
          </li>
        `).join("")}
      </ul>
      <div class="plan-footer">
        <button class="btn btn-${plan.popular ? "primary" : "secondary"}" style="width:100%;justify-content:center"
          onclick="buyPlan('${plan.name}', ${plan.price})">
          ${isCurrent ? "Plan Actual" : plan.cta}
        </button>
      </div>
    </div>
  `}).join("");
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

// Renderiza los gimnasios con su ocupación
function renderGymOccupancy(containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  container.innerHTML = GYMS.map(gym => {
    const pct = Math.round((gym.current / gym.capacity) * 100);
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

// Renderiza las clases con filtro de categoría
function renderClasses(containerId, filter = "all") {
  const container = document.getElementById(containerId);
  if (!container) return;

  const filtered = filter === "all"
    ? CLASSES
    : CLASSES.filter(c => c.category === filter);

  container.innerHTML = filtered.map(cls => {
    const pct = Math.round((cls.booked / cls.spots) * 100);
    const available = cls.spots - cls.booked;
    return `
      <div class="class-card">
        <div class="class-meta">
          <span class="class-tag">${cls.category}</span>
          <span class="class-tag" style="background:rgba(255,255,255,0.05);color:var(--text-muted)">${cls.day}</span>
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
          <span style="font-size:0.8rem;color:var(--text-muted);white-space:nowrap">${available} plazas</span>
        </div>
        <button class="btn btn-${available > 0 ? "outline" : "secondary"}" style="width:100%;justify-content:center;padding:0.6rem"
          onclick="${available > 0 ? `reserveClass('${cls.name}')` : "void(0)"}">
          ${available > 0 ? "Reservar" : "Completo"}
        </button>
      </div>
    `;
  }).join("");

  if (filtered.length === 0) {
    container.innerHTML = `<p style="color:var(--text-muted);text-align:center;grid-column:1/-1;padding:3rem">No hay clases disponibles para este filtro.</p>`;
  }
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

// Muestra un mensaje de aviso cuando no hay tarifa activa
function showNoPlantAlert() {
  // Busca si ya existe el aviso (evita duplicados)
  if (document.getElementById("no-plan-alert")) return;

  const alert = document.createElement("div");
  alert.id = "no-plan-alert";
  alert.style.cssText = `
    position:fixed; bottom:5rem; left:50%; transform:translateX(-50%);
    background:var(--bg-card); border:1px solid var(--primary);
    border-radius:12px; padding:1.25rem 1.5rem; max-width:360px; width:90%;
    box-shadow:0 8px 32px rgba(0,0,0,0.4); z-index:9999; text-align:center;
  `;
  alert.innerHTML = `
    <p style="margin:0 0 1rem;color:var(--text-primary);font-size:0.9rem">
      ⚠️ <strong>Necesitas tener una tarifa activa</strong> para reservar clases.
    </p>
    <div style="display:flex;gap:0.75rem;justify-content:center">
      <a href="tarifas.html" class="btn btn-primary" style="padding:0.5rem 1.25rem;font-size:0.8rem">
        Ver Tarifas
      </a>
      <button onclick="document.getElementById('no-plan-alert').remove()"
              class="btn btn-secondary" style="padding:0.5rem 1rem;font-size:0.8rem">
        Cerrar
      </button>
    </div>
  `;
  document.body.appendChild(alert);
  // Se elimina automáticamente a los 6 segundos
  setTimeout(() => alert.remove(), 6000);
}

// Renderiza los productos
function renderProducts(containerId, filter = "all") {
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
          ${p.originalPrice ? `<span class="price-old">${p.originalPrice.toFixed(2)}€</span>` : ""}
        </div>
        <button class="btn btn-outline" style="width:100%;justify-content:center;padding:0.5rem;margin-top:0.75rem;font-size:0.75rem"
          onclick="addToCart('${p.name}')">
          Añadir al carrito
        </button>
      </div>
    </div>
  `).join("");
}

function addToCart(name) {
  showToast(`"${name}" añadido al carrito 🛒`, "success");
}

// Renderiza el blog
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

// Renderiza las instalaciones
function renderFacilities(containerId) {
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

// Renderiza los 4 primeros productos en la home
function renderFeaturedProducts(containerId) {
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
          ${p.originalPrice ? `<span class="price-old">${p.originalPrice.toFixed(2)}€</span>` : ""}
        </div>
      </div>
    </div>
  `).join("");
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
// 7. FORMULARIOS DE AUTENTICACIÓN
// =====================================================

function initLoginForm() {
  const form = document.getElementById("login-form");
  if (!form) return;

  form.addEventListener("submit", function(e) {
    e.preventDefault();

    const email    = document.getElementById("login-email").value.trim();
    const password = document.getElementById("login-password").value;
    const alert    = document.getElementById("login-alert");

    // Validación simple
    if (!email || !password) {
      showAlert(alert, "Rellena todos los campos.", "error");
      return;
    }

    // Intenta el login
    const result = Auth.login(email, password);

    if (result.ok) {
      showAlert(alert, "¡Bienvenido de nuevo! Redirigiendo...", "success");
      setTimeout(() => window.location.href = "index.html", 1200);
    } else {
      showAlert(alert, result.msg, "error");
    }
  });
}

function initRegisterForm() {
  const form = document.getElementById("register-form");
  if (!form) return;

  form.addEventListener("submit", function(e) {
    e.preventDefault();

    const name     = document.getElementById("reg-name").value.trim();
    const email    = document.getElementById("reg-email").value.trim();
    const password = document.getElementById("reg-password").value;
    const confirm  = document.getElementById("reg-confirm").value;
    const alert    = document.getElementById("reg-alert");

    // Validaciones
    if (!name || !email || !password || !confirm) {
      showAlert(alert, "Rellena todos los campos.", "error");
      return;
    }

    if (password.length < 6) {
      showAlert(alert, "La contraseña debe tener al menos 6 caracteres.", "error");
      return;
    }

    if (password !== confirm) {
      showAlert(alert, "Las contraseñas no coinciden.", "error");
      return;
    }

    // Registra al usuario
    const result = Auth.register(name, email, password);

    if (result.ok) {
      showAlert(alert, "¡Cuenta creada con éxito! Redirigiendo...", "success");
      setTimeout(() => window.location.href = "index.html", 1200);
    } else {
      showAlert(alert, result.msg, "error");
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
