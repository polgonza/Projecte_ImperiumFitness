const PLANS = [
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

const FACILITIES = [
  { icon: "🏋️", name: "Bodybuilding",          desc: "Zona de musculación con más de 200 máquinas de última generación y peso libre completo." },
  { icon: "💪", name: "Powerlifting",           desc: "Plataformas olímpicas profesionales con barras calibradas y racks de competición." },
  { icon: "🥊", name: "Boxing",                 desc: "Ring profesional, sacos de boxeo, speed bags y zona de sparring con entrenadores certificados." },
  { icon: "🔥", name: "Condicionamiento",       desc: "Área funcional con trineos, battle ropes, kettlebells y circuitos metabólicos." },
  { icon: "🧊", name: "Recuperación",           desc: "Zona wellness con sauna, crioterapia, foam rolling y pistolas de masaje." },
  { icon: "🎯", name: "Actividades Dirigidas",  desc: "Salas climatizadas con sonido envolvente para clases de yoga, spinning, HIIT y más." }
];

const Auth = {

  getUser() {
    const data = localStorage.getItem("imperium_user");
    return data ? JSON.parse(data) : null;
  },

  setUser(user) {
    localStorage.setItem("imperium_user", JSON.stringify(user));
  },

  isLoggedIn() {
    return !!localStorage.getItem("imperium_token");
  },

  async login(email, password) {
    const result = await ApiAuth.login(email, password);

    if (result.ok) {

      const payload = JSON.parse(atob(result.token.split(".")[1]));

      this.setUser({
        id:    payload.userId,
        name:  payload.sub, 
        email: payload.sub,
        roles: payload.roles
      });
    }

    return result;
  },

  async register(nom, email, password) {
    const result = await ApiAuth.register(nom, email, password);

    if (result.ok) {
      const payload = JSON.parse(atob(result.token.split(".")[1]));
      this.setUser({
        id:    payload.userId,
        name:  nom,      
        email: payload.sub,
        roles: payload.roles
      });
    }

    return result;
  },

  logout() {
    localStorage.removeItem("imperium_token");
    localStorage.removeItem("imperium_user");
    localStorage.removeItem("imperium_carrito");
    window.location.href = "index.html";
  }
};


function showToast(message, type = "success") {
  let container = document.getElementById("toast-container");
  if (!container) {
    container = document.createElement("div");
    container.id = "toast-container";
    container.className = "toast-container";
    document.body.appendChild(container);
  }

  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.innerHTML = `
    <span class="toast-icon">${type === "success" ? "✅" : "❌"}</span>
    <span>${message}</span>
  `;
  container.appendChild(toast);

  setTimeout(() => {
    toast.remove();
  }, 3000);
}

function initNavbar() {
  const navbar = document.getElementById("navbar");
  const navToggle = document.getElementById("nav-toggle");
  const navMobile = document.getElementById("nav-mobile");

  if (!navbar) return;

  window.addEventListener("scroll", () => {
    if (window.scrollY > 20) {
      navbar.classList.add("scrolled");
    } else {
      navbar.classList.remove("scrolled");
    }
  });

  if (navToggle && navMobile) {
    navToggle.addEventListener("click", () => {
      navMobile.classList.toggle("open");
    });
  }

  const currentPage = window.location.pathname.split("/").pop() || "index.html";
  document.querySelectorAll(".nav-links a, .nav-mobile a").forEach(link => {
    const href = link.getAttribute("href");
    if (href === currentPage || (currentPage === "index.html" && href === "index.html")) {
      link.classList.add("active");
    }
  });

  updateNavbarUser();
}

function updateNavbarUser() {
  renderNavbarUser();
}

function renderPlans(containerId) {
  renderPlanes(containerId);
}

function buyPlan(planName, planPrice) {
  if (!Auth.getUser()) {
    showToast(I18n.idioma === "ca" ? "Has d'iniciar sessió per contractar un pla." : "You need to log in to subscribe.", "error");
    setTimeout(() => window.location.href = "login.html", 1500);
    return;
  }
  const planItem = [{
    id: "plan_" + planName.replace(/\s/g, "_"),
    name: "Plan: " + planName,
    price: parseFloat(planPrice),
    img: "",
    quantity: 1,
    esPlan: true,   
    planName: planName
  }];
  localStorage.setItem("imperium_carrito", JSON.stringify(planItem));
  window.location.href = "checkout.html";
}

function renderGymOccupancy(containerId) {
  renderOcupacion(containerId);
}

function renderClasses(containerId, filter = "all") {
  renderClases(containerId, filter);
}

function reserveClass(name) {
  const user = Auth.getUser();
  if (!user) {
    showToast(t("toast.sessionExp"), "error");
    setTimeout(() => window.location.href = "login.html", 1500);
    return;
  }
  if (!user.plan) {
    showNoPlantAlert();
    return;
  }
  showToast(t("toast.reservaOk", [name]), "success");
}

function showNoPlantAlert() {
  renderAlertaSinPlan();
}

function renderProducts(containerId, filter = "all") {
  renderProductos(containerId, filter);
}

function addToCart(name) {
  showToast(t("toast.afegitCarret", [name]), "success");
}

function renderFacilities(containerId) {
  renderInstalaciones(containerId);
}

function renderFeaturedProducts(containerId) {
  renderProductosDestacados(containerId);
}

function renderProfile() {
  const user = Auth.getUser();
  if (!user) {
    window.location.href = "login.html";
    return;
  }

  document.getElementById("profile-initial").textContent  = user.name.charAt(0).toUpperCase();
  document.getElementById("profile-name").textContent     = user.name;
  document.getElementById("profile-email").textContent    = user.email;
  document.getElementById("profile-plan").textContent     = user.plan;
  document.getElementById("profile-since").textContent    = user.memberSince;

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

function initProfileTabs() {
  const tabs = document.querySelectorAll(".profile-tab");
  const contents = document.querySelectorAll(".tab-content");

  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      tabs.forEach(t => t.classList.remove("active"));
      contents.forEach(c => c.classList.remove("active"));

      tab.classList.add("active");
      const target = document.getElementById(tab.dataset.tab);
      if (target) target.classList.add("active");
    });
  });
}

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
      showAlert(alertEl, t("toast.campsBuits"), "error");
      return;
    }

    btn.disabled = true;
    btn.textContent = t("login.entrant");

    const result = await Auth.login(email, password);

    if (result.ok) {
      showAlert(alertEl, t("toast.benvingut"), "success");
      setTimeout(() => window.location.href = "index.html", 1200);
    } else {
      showAlert(alertEl, result.error, "error");
      btn.disabled = false;
      btn.textContent = t("login.entrar");
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

    if (!nom || !email || !password || !confirm) {
      showAlert(alertEl, t("toast.campsBuits"), "error");
      return;
    }
    if (password.length < 6) {
      showAlert(alertEl, t("toast.contraError"), "error");
      return;
    }
    if (password !== confirm) {
      showAlert(alertEl, t("toast.contraNoCoincid"), "error");
      return;
    }

    btn.disabled = true;
    btn.textContent = t("registre.creant");

    const result = await Auth.register(nom, email, password);

    if (result.ok) {
      showAlert(alertEl, t("toast.compteCreat"), "success");
      setTimeout(() => window.location.href = "index.html", 1200);
    } else {
      showAlert(alertEl, result.error, "error");
      btn.disabled = false;
      btn.textContent = t("registre.crear");
    }
  });
}

function initRecoverForm() {
  const form = document.getElementById("recover-form");
  if (!form) return;

  form.addEventListener("submit", function(e) {
    e.preventDefault();

    const email   = document.getElementById("recover-email").value.trim();
    const alertEl = document.getElementById("recover-alert");

    if (!email) {
      showAlert(alertEl, t("toast.emailInvalid"), "error");
      return;
    }

    showAlert(alertEl, `S'ha enviat un enllaç de recuperació a ${email}.`, "success");
    form.reset();
  });
}

function showAlert(alertEl, msg, type) {
  if (!alertEl) return;
  alertEl.textContent = msg;
  alertEl.className = `alert ${type} show`;
}

function initPasswordToggle() {
  document.querySelectorAll(".toggle-password").forEach(btn => {
    btn.addEventListener("click", function() {
      const input = document.getElementById(this.dataset.target);
      if (!input) return;
      const visible = input.type === "text";
      input.type = visible ? "password" : "text";
      this.innerHTML = visible
        ? `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>`
        : `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/><line x1="1" y1="1" x2="23" y2="23"/></svg>`;
    });
  });
}

function initFilters(filterSelector, onFilterChange) {
  document.querySelectorAll(filterSelector).forEach(btn => {
    btn.addEventListener("click", function() {
      document.querySelectorAll(filterSelector).forEach(b => b.classList.remove("active"));
      this.classList.add("active");
      onFilterChange(this.dataset.filter);
    });
  });
}

function initContactForm() {
  const form = document.getElementById("contact-form");
  if (!form) return;

  form.addEventListener("submit", function(e) {
    e.preventDefault();
    showToast(t("toast.missatgeEnviat"), "success");
    form.reset();
  });
}

function initHomeContactForm() {
  const form = document.getElementById("home-contact-form");
  if (!form) return;

  form.addEventListener("submit", function(e) {
    e.preventDefault();
    showToast("¡Mensaje enviado! Te contactaremos pronto. 👍", "success");
    form.reset();
  });
}

function initNewsletter() {
  document.querySelectorAll(".newsletter-btn").forEach(btn => {
    btn.addEventListener("click", function() {
      const inputId = this.dataset.input;
      const input = inputId ? document.getElementById(inputId) : this.previousElementSibling;
      if (input && input.value) {
        showToast(t("toast.subscrit"), "success");
        input.value = "";
      } else {
        showToast("Introduce un email válido.", "error");
      }
    });
  });
}

document.addEventListener("DOMContentLoaded", function() {
  initNavbar();
  initLoginForm();
  initRegisterForm();
  initRecoverForm();
  initPasswordToggle();
  initContactForm();
  initHomeContactForm();
  initNewsletter();

  const page = window.location.pathname.split("/").pop() || "index.html";

  if (page === "tarifas.html") {
    renderPlans("plans-grid");
  }

  if (page === "ocupacion.html") {
    renderGymOccupancy("gyms-grid");
  }

  if (page === "actividades.html") {
  }

  if (page === "instalaciones.html") {
    renderFacilities("facilities-grid");
  }
});
