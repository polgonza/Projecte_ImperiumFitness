/* =====================================================
   IMPERIUM FITNESS — api.js
   Centralitza totes les crides al backend Spring Boot
   BASE_URL apunta al backend local (canvia en producció)
   ===================================================== */

const API_BASE = (window.location.hostname === "localhost" && 
                  document.title.includes("local"))  
  ? "http://localhost:8084"
  : "http://10.147.17.250:8086";

/* ── Utilitat: capçaleres amb JWT ───────────────────────
   Totes les peticions autenticades necessiten enviar
   el token JWT a la capçalera Authorization.
   El token es guarda al localStorage després del login. */
function authHeaders() {
  const token = localStorage.getItem("imperium_token");
  return {
    "Content-Type": "application/json",
    ...(token ? { "Authorization": `Bearer ${token}` } : {})
  };
}

/* ── Utilitat: gestió d'errors HTTP ─────────────────────
   Si el servidor retorna 401 (token expirat) redirigim
   automàticament al login. */
async function apiFetch(url, options = {}) {
  const res = await fetch(API_BASE + url, {
    ...options,
    headers: authHeaders()
  });

  // Token expirat → logout amb missatge elegant
  if (res.status === 401) {
    SessionManager.logoutWithMessage(
      "Tu sesión ha expirado. Por favor, inicia sesión de nuevo."
    );
    return null;
  }

  return res;
}

/* ══════════════════════════════════════════════════════
   AUTH
   ══════════════════════════════════════════════════════ */

const ApiAuth = {

  /* Login: retorna { ok, token, error } */
  async login(email, password) {
    try {
      const res = await fetch(API_BASE + "/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: email, password })
      });

      if (res.ok) {
        const data = await res.json();
        // Guardem el token al localStorage
        localStorage.setItem("imperium_token", data.token);
        return { ok: true, token: data.token };
      }

      // Error 401: credencials incorrectes
      return { ok: false, error: "Email o contraseña incorrectos." };

    } catch (e) {
      return { ok: false, error: "No se puede conectar con el servidor." };
    }
  },
  // Dins de ApiAuth, afegeix aquest mètode:
async recover(email) {
  try {
    const res = await fetch(API_BASE + "/api/auth/recover", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email })
    });
    return { ok: true };
  } catch (e) {
    return { ok: false, error: "No se puede conectar con el servidor." };
  }
},
  /* Registre: retorna { ok, token, error } */
  async register(nom, email, password) {
    try {
      const res = await fetch(API_BASE + "/api/auth/registre", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nom, email, password })
      });

      if (res.status === 201) {
        const data = await res.json();
        localStorage.setItem("imperium_token", data.token);
        return { ok: true, token: data.token };
      }

      if (res.status === 409) {
        return { ok: false, error: "Este email ya está registrado." };
      }

      return { ok: false, error: "Error al crear la cuenta." };

    } catch (e) {
      return { ok: false, error: "No se puede conectar con el servidor." };
    }
  },

  /* Logout: neteja el localStorage */
  logout() {
    localStorage.removeItem("imperium_token");
    localStorage.removeItem("imperium_user");
    window.location.href = "index.html";
  },

  /* Comprova si hi ha sessió activa */
  isLoggedIn() {
    return !!localStorage.getItem("imperium_token");
  }
};

/* ══════════════════════════════════════════════════════
   CLASSES
   ══════════════════════════════════════════════════════ */

const ApiClasses = {

  /* Llista totes les classes */
  async getAll() {
    try {
      const res = await apiFetch("/api/classes");
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  },

  /* Reservar una classe */
  async reservar(usuariId, classeId) {
  try {
    const res = await apiFetch("/api/reserves", {
      method: "POST",
      body: JSON.stringify({ usuariId, classeId })
    });


    if (res && res.status === 201) return { ok: true };
    if (res && res.status === 409) return { ok: false, error: "Ya tienes una reserva para esta clase." };
    return { ok: false, error: "No se pudo completar la reserva." };

  } catch (e) {
    return { ok: false, error: "Error de conexión." };
  }
},

  /* Obtenir les reserves d'un usuari */
  async getReservesUsuari(usuariId) {
    try {
      const res = await apiFetch(`/api/reserves/usuari/${usuariId}`);
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  }
};

/* ══════════════════════════════════════════════════════
   PRODUCTES
   ══════════════════════════════════════════════════════ */

const ApiProductes = {

  /* Llista tots els productes */
  async getAll() {
    try {
      const res = await apiFetch("/api/productes");
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  }
};

/* ══════════════════════════════════════════════════════
   NOTÍCIES
   ══════════════════════════════════════════════════════ */

const ApiNoticies = {

  /* Llista totes les notícies (endpoint públic, sense token) */
  async getAll() {
    try {
      const res = await fetch(API_BASE + "/api/noticies");
      if (res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  }
};

/* ══════════════════════════════════════════════════════
   CONTACTE
   ══════════════════════════════════════════════════════ */

const ApiContacte = {

  /* Envia un missatge de contacte (endpoint públic) */
  async enviar(nom, email, missatge) {
    try {
      const res = await fetch(API_BASE + "/api/contactes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nom, email, missatge })
      });
      return res && res.status === 201;
    } catch (e) { return false; }
  }
};
/* ══════════════════════════════════════════════════════
   USUARI — perfil
   ══════════════════════════════════════════════════════ */

const ApiUsuari = {

  async getPerfil(usuariId) {
    try {
      // Usem el nou endpoint específic de perfil
      const res = await apiFetch(`/api/usuaris/perfil/${usuariId}`);
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  },

  async getReserves(usuariId) {
    try {
      const res = await apiFetch(`/api/reserves/usuari/${usuariId}`);
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  },

  async getVendes(usuariId) {
    try {
      const res = await apiFetch(`/api/vendes/usuari/${usuariId}`);
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  }
};
/* ══════════════════════════════════════════════════════
   GESTIÓ DE SESSIÓ — expiració del token JWT
   ══════════════════════════════════════════════════════ */

const SessionManager = {

  /* Comprova si el token expirarà aviat o ja ha expirat */
  checkToken() {
    const token = localStorage.getItem("imperium_token");
    if (!token) return "no_token";

    try {
      // Descodifiquem el payload del JWT (part central)
      const payload = JSON.parse(atob(token.split(".")[1]));
      const ara     = Math.floor(Date.now() / 1000); // temps actual en segons
      const expira  = payload.exp;
      const restant = expira - ara;                   // segons que queden

      if (restant <= 0)   return "expired";           // ja ha expirat
      if (restant <= 300) return "expiring_soon";     // expira en < 5 minuts
      return "valid";

    } catch (e) {
      return "invalid";
    }
  },

  /* Tanca la sessió mostrant un missatge al login */
  logoutWithMessage(msg) {
    localStorage.removeItem("imperium_token");
    localStorage.removeItem("imperium_user");
    // Guardem el missatge per mostrar-lo a la pàgina de login
    sessionStorage.setItem("session_msg", msg);
    window.location.href = "login.html";
  },

  /* Comprova la sessió en carregar qualsevol pàgina protegida */
  init(requiresAuth = false) {
    const status = this.checkToken();
    const page   = window.location.pathname.split("/").pop();

    // Pàgines que no requereixen auth
    const publicPages = ["login.html", "register.html", "index.html",
                         "recover.html", ""];

    if (status === "expired" || status === "invalid" || status === "no_token") {
      if (requiresAuth || !publicPages.includes(page)) {
        this.logoutWithMessage(
          "Tu sesión ha expirado. Por favor, inicia sesión de nuevo."
        );
        return false;
      }
    }

    if (status === "expiring_soon") {
      // Avisem l'usuari que la sessió expira aviat
      this.showExpirationWarning();
    }

    return true;
  },

  /* Mostra un banner d'avís quan queden menys de 5 minuts */
  showExpirationWarning() {
    // Evitem mostrar-lo dues vegades
    if (document.getElementById("session-warning")) return;

    const banner = document.createElement("div");
    banner.id = "session-warning";
    banner.style.cssText = `
      position: fixed;
      top: 70px;
      left: 50%;
      transform: translateX(-50%);
      background: var(--bg-card);
      border: 1px solid var(--primary);
      border-radius: 12px;
      padding: 1rem 1.5rem;
      max-width: 420px;
      width: 90%;
      box-shadow: 0 8px 32px rgba(0,0,0,0.4);
      z-index: 9999;
      text-align: center;
      font-size: 0.875rem;
    `;
    banner.innerHTML = `
      <p style="margin:0 0 0.75rem;color:var(--text-primary)">
        ⚠️ Tu sesión expirará en menos de 5 minutos.
      </p>
      <div style="display:flex;gap:0.75rem;justify-content:center">
        <button
          onclick="SessionManager.renovarSessio()"
          class="btn btn-primary"
          style="padding:0.4rem 1rem;font-size:0.8rem">
          Renovar sesión
        </button>
        <button
          onclick="document.getElementById('session-warning').remove()"
          class="btn btn-secondary"
          style="padding:0.4rem 1rem;font-size:0.8rem">
          Ignorar
        </button>
      </div>
    `;
    document.body.appendChild(banner);

    // S'elimina sol als 30 segons
    setTimeout(() => banner?.remove(), 30000);
  },

  /* Renova la sessió fent login automàtic si tenim les dades */
  async renovarSessio() {
    document.getElementById("session-warning")?.remove();

    // Redirigim al login amb un missatge per renovar
    sessionStorage.setItem("session_msg",
      "Por favor, inicia sesión de nuevo para renovar tu sesión.");
    window.location.href = "login.html";
  }
};