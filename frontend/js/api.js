/* =====================================================
   IMPERIUM FITNESS — api.js
   Centralitza totes les crides al backend Spring Boot
   BASE_URL apunta al backend local (canvia en producció)
   ===================================================== */

/* ── Configuració de l'entorn ───────────────────────────
   IS_LOCAL_BACKEND = true  → backend al teu ordinador (mvn spring-boot:run)
   IS_LOCAL_BACKEND = false → backend al servidor (ZeroTier)                */
const IS_LOCAL_BACKEND = false; // ← canvia segons on treballs

const API_BASE = IS_LOCAL_BACKEND
  ? "http://localhost:8084"
  : "http://10.147.17.250:8086";

/* ── Utilitat: capçaleres amb JWT ─────────────────────── */
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

  if (res.status === 401) {
    SessionManager.logoutWithMessage(t("toast.sessionExp"));
    return null;
  }

  return res;
}

/* ══════════════════════════════════════════════════════
   AUTH
   ══════════════════════════════════════════════════════ */

const ApiAuth = {

  async login(email, password) {
    try {
      const res = await fetch(API_BASE + "/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: email, password })
      });

      if (res.ok) {
        const data = await res.json();
        localStorage.setItem("imperium_token", data.token);
        return { ok: true, token: data.token };
      }

      return { ok: false, error: t("toast.errorLogin") };

    } catch (e) {
      return { ok: false, error: I18n.idioma === "ca"
        ? "No es pot connectar amb el servidor."
        : "Cannot connect to the server." };
    }
  },

  async recover(email) {
    try {
      await fetch(API_BASE + "/api/auth/recover", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email })
      });
      return { ok: true };
    } catch (e) {
      return { ok: false, error: I18n.idioma === "ca"
        ? "No es pot connectar amb el servidor."
        : "Cannot connect to the server." };
    }
  },

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
        return { ok: false, error: I18n.idioma === "ca"
          ? "Aquest email ja està registrat."
          : "This email is already registered." };
      }

      return { ok: false, error: I18n.idioma === "ca"
        ? "Error en crear el compte."
        : "Error creating account." };

    } catch (e) {
      return { ok: false, error: I18n.idioma === "ca"
        ? "No es pot connectar amb el servidor."
        : "Cannot connect to the server." };
    }
  },

  logout() {
    localStorage.removeItem("imperium_token");
    localStorage.removeItem("imperium_user");
    window.location.href = "index.html";
  },

  isLoggedIn() {
    return !!localStorage.getItem("imperium_token");
  }
};

/* ══════════════════════════════════════════════════════
   CLASSES
   ══════════════════════════════════════════════════════ */

const ApiClasses = {

  async getAll() {
    try {
      const res = await apiFetch("/api/classes");
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  },

  async reservar(usuariId, classeId) {
    try {
      const res = await apiFetch("/api/reserves", {
        method: "POST",
        body: JSON.stringify({ usuariId, classeId })
      });

      if (res && res.status === 201) return { ok: true };
      if (res && res.status === 409) {
        const data = await res.json().catch(() => ({}));
        const msg  = data.detail || data.message || "";
        if (msg.includes("límit") || msg.includes("limit")) {
          return { ok: false, error: I18n.idioma === "ca"
            ? "Has assolit el límit de 5 reserves actives."
            : "You have reached the limit of 5 active bookings." };
        }
        return { ok: false, error: I18n.idioma === "ca"
          ? "Ja tens una reserva per a aquesta classe."
          : "You already have a booking for this class." };
      }
      return { ok: false, error: t("toast.reservaError") };

    } catch (e) {
      return { ok: false, error: I18n.idioma === "ca"
        ? "Error de connexió."
        : "Connection error." };
    }
  },

  async getReservesUsuari(usuariId) {
    try {
      const res = await apiFetch(`/api/reserves/usuari/${usuariId}`);
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  },

  async cancelarReserva(usuariId, classeId) {
    try {
      const res = await apiFetch(
        `/api/reserves/usuari/${usuariId}/classe/${classeId}`,
        { method: "DELETE" }
      );
      return res && (res.status === 204 || res.status === 200);
    } catch (e) { return false; }
  }
};

/* ══════════════════════════════════════════════════════
   PRODUCTES
   ══════════════════════════════════════════════════════ */

const ApiProductes = {

  async getAll() {
    try {
      const res = await apiFetch("/api/productes");
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  },

  // Fragment suggerit per assistent IA - revisar i adaptar
  async crear(producte) {
    try {
      const res = await apiFetch("/api/productes", {
        method: "POST",
        body: JSON.stringify(producte)
      });
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  },

  async actualitzar(id, producte) {
    try {
      const res = await apiFetch(`/api/productes/${id}`, {
        method: "PUT",
        body: JSON.stringify(producte)
      });
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  },

  async eliminar(id) {
    try {
      const res = await apiFetch(`/api/productes/${id}`, { method: "DELETE" });
      return res && (res.status === 204 || res.status === 200);
    } catch (e) { return false; }
  }
};

/* ══════════════════════════════════════════════════════
   CLASSES ADMIN (crear / editar / eliminar)
   ══════════════════════════════════════════════════════ */

// Fragment suggerit per assistent IA - revisar i adaptar
const ApiClassesAdmin = {

  async crear(classe) {
    try {
      const res = await apiFetch("/api/classes", {
        method: "POST",
        body: JSON.stringify(classe)
      });
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  },

  async actualitzar(id, classe) {
    try {
      const res = await apiFetch(`/api/classes/${id}`, {
        method: "PUT",
        body: JSON.stringify(classe)
      });
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  },

  async eliminar(id) {
    try {
      const res = await apiFetch(`/api/classes/${id}`, { method: "DELETE" });
      return res && (res.status === 204 || res.status === 200);
    } catch (e) { return false; }
  }
};

/* ══════════════════════════════════════════════════════
   NOTÍCIES
   ══════════════════════════════════════════════════════ */

const ApiNoticies = {

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
  },

  async assignarTarifa(usuariId, tarifaId) {
    try {
      const res = await apiFetch(`/api/usuaris/${usuariId}/tarifa`, {
        method: "PUT",
        body: JSON.stringify({ tarifaId })
      });
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  },

  async getTarifaActiva() {
    const user = Auth.getUser();
    if (!user) return null;
    try {
      const perfil = await this.getPerfil(user.id);
      if (perfil && perfil.tarifaId) {
        Auth.setUser({ ...user, tarifaId: perfil.tarifaId, tarifaNom: perfil.tarifaNom });
        return perfil.tarifaNom;
      }
      return null;
    } catch (e) { return null; }
  },

  async cancelarTarifa(usuariId) {
    try {
      const res = await apiFetch(`/api/usuaris/${usuariId}/cancel-tarifa`, {
        method: "PUT"
      });
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  }
};

/* ══════════════════════════════════════════════════════
   ESTADÍSTIQUES — només ADMIN
   ══════════════════════════════════════════════════════ */

const ApiStats = {
    // Fragment suggerit per assistent IA - revisar i adaptar
  async getVendesMensuals() {
    try { const res = await apiFetch("/api/stats/vendes-mensuals"); return res && res.ok ? await res.json() : []; } catch(e) { return []; }
  },
  async getUsuarisMensuals() {
    try { const res = await apiFetch("/api/stats/usuaris-mensuals"); return res && res.ok ? await res.json() : []; } catch(e) { return []; }
  },
  async getDistribucioTarifes() {
    try { const res = await apiFetch("/api/stats/distribucio-tarifes"); return res && res.ok ? await res.json() : []; } catch(e) { return []; }
  },
  async getEstocBaix() {
    try { const res = await apiFetch("/api/stats/estoc-baix"); return res && res.ok ? await res.json() : []; } catch(e) { return []; }
  },
  async getActivitatRecent() {
    try { const res = await apiFetch("/api/stats/activitat-recent"); return res && res.ok ? await res.json() : null; } catch(e) { return null; }
  },
  async getResum() {
    try {
      const res = await apiFetch("/api/stats/resum");
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  },
    // Fragment suggerit per assistent IA - revisar i adaptar
  async getClasses() {
    try {
      const res = await apiFetch("/api/stats/classes");
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  },

  async getProductes() {
    try {
      const res = await apiFetch("/api/stats/productes");
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  }
};

/* ══════════════════════════════════════════════════════
   GESTIÓ DE SESSIÓ — expiració del token JWT
   ══════════════════════════════════════════════════════ */

const SessionManager = {

  checkToken() {
    const token = localStorage.getItem("imperium_token");
    if (!token) return "no_token";

    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const ara     = Math.floor(Date.now() / 1000);
      const restant = payload.exp - ara;

      if (restant <= 0)   return "expired";
      if (restant <= 300) return "expiring_soon";
      return "valid";
    } catch (e) {
      return "invalid";
    }
  },

  logoutWithMessage(msg) {
    localStorage.removeItem("imperium_token");
    localStorage.removeItem("imperium_user");
    sessionStorage.setItem("session_msg", msg);
    window.location.href = "login.html";
  },

  init(requiresAuth = false) {
    const status = this.checkToken();
    const page   = window.location.pathname.split("/").pop();
    const publicPages = ["login.html", "register.html", "index.html", "recover.html", ""];

    if (status === "expired" || status === "invalid" || status === "no_token") {
      if (requiresAuth || !publicPages.includes(page)) {
        this.logoutWithMessage(t("toast.sessionExp"));
        return false;
      }
    }

    if (status === "expiring_soon") {
      this.showExpirationWarning();
    }

    this.startTokenCheck();
    return true;
  },

  showExpirationWarning() {
    if (document.getElementById("session-warning")) return;

    const banner = document.createElement("div");
    banner.id = "session-warning";
    banner.style.cssText = `
      position:fixed;top:70px;left:50%;transform:translateX(-50%);
      background:var(--bg-card);border:1px solid var(--primary);
      border-radius:12px;padding:1rem 1.5rem;max-width:420px;width:90%;
      box-shadow:0 8px 32px rgba(0,0,0,0.4);z-index:9999;
      text-align:center;font-size:0.875rem;
    `;
    banner.innerHTML = `
      <p style="margin:0 0 0.75rem;color:var(--text-primary)">
        ${t("toast.sessionAviat")}
      </p>
      <div style="display:flex;gap:0.75rem;justify-content:center">
        <button onclick="SessionManager.renovarSessio()"
          class="btn btn-primary" style="padding:0.4rem 1rem;font-size:0.8rem">
          ${t("toast.renovarSessio")}
        </button>
        <button onclick="document.getElementById('session-warning').remove()"
          class="btn btn-secondary" style="padding:0.4rem 1rem;font-size:0.8rem">
          ${t("toast.ignorar")}
        </button>
      </div>
    `;
    document.body.appendChild(banner);
    setTimeout(() => banner?.remove(), 30000);
  },

  async renovarSessio() {
    document.getElementById("session-warning")?.remove();
    sessionStorage.setItem("session_msg", t("toast.sessionExp"));
    window.location.href = "login.html";
  },

  startTokenCheck() {
    setInterval(() => {
      const status = this.checkToken();
      if (status === "expired" || status === "invalid") {
        this.logoutWithMessage(t("toast.sessionExp"));
      } else if (status === "expiring_soon") {
        this.showExpirationWarning();
      }
    }, 60000);
  }
};