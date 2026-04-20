/* =====================================================
   IMPERIUM FITNESS — api.js
   Centralitza totes les crides al backend Spring Boot
   BASE_URL apunta al backend local (canvia en producció)
   ===================================================== */

const API_BASE = "http://localhost:8084";

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

  // Token expirat o no autenticat → redirigir al login
  if (res.status === 401) {
    localStorage.removeItem("imperium_token");
    localStorage.removeItem("imperium_user");
    window.location.href = "login.html";
    return;
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