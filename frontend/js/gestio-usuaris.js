const ApiAdmin = {

  async getTots() {
    try {
      const res = await apiFetch("/api/usuaris");
      if (res && res.ok) return await res.json();
      return [];
    } catch (e) { return []; }
  },

  async canviarRol(usuariId, nouRol) {
    try {
      const res = await apiFetch(`/api/usuaris/${usuariId}/rol`, {
        method: "PUT",
        body: JSON.stringify({ rol: nouRol })
      });
      if (res && res.ok) return await res.json();
      return null;
    } catch (e) { return null; }
  }
};

let _totsElsUsuaris = [];  
let _filtre = "";         
async function renderGestioUsuaris() {
  const container = document.getElementById("gestio-usuaris-container");
  if (!container) return;

  const user = Auth.getUser();
  if (!user || !user.roles || !user.roles.includes("ROLE_ADMIN")) {
    container.innerHTML = "";
    return;
  }

  container.innerHTML = `
    <p style="color:var(--text-muted);text-align:center;padding:2rem">
      ${t("gestioUsuaris.carregant")}
    </p>`;

  const usuaris = await ApiAdmin.getTots();

  if (!usuaris || usuaris.length === 0) {
    container.innerHTML = `
      <p style="color:var(--text-muted);text-align:center;padding:2rem">
        ${t("gestioUsuaris.error")}
      </p>`;
    return;
  }

  _totsElsUsuaris = usuaris;
  _pintaUsuaris(container, usuaris);
  const inputCerca = document.getElementById("gestio-cerca");
  if (inputCerca && _filtre) {
    inputCerca.value = _filtre;
    _aplicaFiltre(_filtre);
  }
}

function _pintaUsuaris(container, usuaris) {
  const total = usuaris.length;

  container.innerHTML = `
    <div style="display:flex;align-items:center;gap:1rem;margin-bottom:1rem;flex-wrap:wrap">
      <input
        type="text"
        id="gestio-cerca"
        placeholder="${t("gestioUsuaris.cerca")}"
        style="flex:1;min-width:200px;padding:.5rem .75rem;
               background:var(--bg-secondary);border:1px solid var(--border);
               border-radius:8px;color:var(--text);font-size:.875rem"
        oninput="_aplicaFiltre(this.value)"
      >
      <span id="gestio-total"
        style="font-size:.8rem;color:var(--text-muted);white-space:nowrap">
        ${t("gestioUsuaris.total", [total])}
      </span>
    </div>

    <div style="overflow-x:auto">
      <table id="gestio-taula"
        style="width:100%;border-collapse:collapse;font-size:.875rem">
        <thead>
          <tr style="border-bottom:1px solid var(--border)">
            <th style="text-align:left;padding:.6rem .75rem;color:var(--text-muted);font-weight:600;font-size:.75rem;text-transform:uppercase;letter-spacing:.06em">
              ${t("gestioUsuaris.nom")}
            </th>
            <th style="text-align:left;padding:.6rem .75rem;color:var(--text-muted);font-weight:600;font-size:.75rem;text-transform:uppercase;letter-spacing:.06em">
              ${t("gestioUsuaris.email")}
            </th>
            <th style="text-align:center;padding:.6rem .75rem;color:var(--text-muted);font-weight:600;font-size:.75rem;text-transform:uppercase;letter-spacing:.06em">
              ${t("gestioUsuaris.rol")}
            </th>
            <th style="text-align:left;padding:.6rem .75rem;color:var(--text-muted);font-weight:600;font-size:.75rem;text-transform:uppercase;letter-spacing:.06em">
              ${t("gestioUsuaris.data")}
            </th>
            <th style="text-align:center;padding:.6rem .75rem;color:var(--text-muted);font-weight:600;font-size:.75rem;text-transform:uppercase;letter-spacing:.06em">
              ${t("gestioUsuaris.accions")}
            </th>
          </tr>
        </thead>
        <tbody id="gestio-tbody">
          ${_fileresHTML(usuaris)}
        </tbody>
      </table>
    </div>
  `;

  if (_filtre) _aplicaFiltre(_filtre, false);
}

function _fileresHTML(usuaris) {
  if (usuaris.length === 0) {
    return `<tr><td colspan="5" style="text-align:center;padding:2rem;color:var(--text-muted)">
      ${t("gestioUsuaris.buit")}
    </td></tr>`;
  }

  return usuaris.map(u => {
    const esAdmin   = u.rol && u.rol.toUpperCase() === "ADMIN";
    const dataAlta  = u.dataRegistre
      ? new Date(u.dataRegistre).toLocaleDateString(I18n.idioma === "ca" ? "ca-ES" : "en-GB")
      : "—";

    const badgeColor = esAdmin
      ? "background:rgba(212,175,55,.15);color:var(--primary)"
      : "background:rgba(99,102,241,.12);color:#6366f1";

    const botoRol = esAdmin
      ? `<button
           onclick="canviarRolUsuari(${u.id}, 'USER', '${_escapaAtribut(u.nom)}')"
           style="padding:.3rem .75rem;border-radius:6px;border:1px solid var(--border);
                  background:transparent;color:var(--text-muted);cursor:pointer;font-size:.75rem"
           title="${t("gestioUsuaris.ferUser")}">
           ${t("gestioUsuaris.ferUser")}
         </button>`
      : `<button
           onclick="canviarRolUsuari(${u.id}, 'ADMIN', '${_escapaAtribut(u.nom)}')"
           style="padding:.3rem .75rem;border-radius:6px;border:1px solid var(--primary);
                  background:transparent;color:var(--primary);cursor:pointer;font-size:.75rem"
           title="${t("gestioUsuaris.ferAdmin")}">
           ${t("gestioUsuaris.ferAdmin")}
         </button>`;

    return `
      <tr data-nom="${u.nom.toLowerCase()}" data-email="${u.email.toLowerCase()}"
          style="border-bottom:.5px solid var(--border)">
        <td style="padding:.65rem .75rem;font-weight:500">${_escapa(u.nom)}</td>
        <td style="padding:.65rem .75rem;color:var(--text-muted)">${_escapa(u.email)}</td>
        <td style="padding:.65rem .75rem;text-align:center">
          <span style="font-size:.72rem;padding:.25rem .6rem;border-radius:4px;${badgeColor}">
            ${esAdmin ? t("gestioUsuaris.admin") : t("gestioUsuaris.user")}
          </span>
        </td>
        <td style="padding:.65rem .75rem;color:var(--text-muted);font-size:.8rem">${dataAlta}</td>
        <td style="padding:.65rem .75rem;text-align:center">${botoRol}</td>
      </tr>`;
  }).join("");
}

function _aplicaFiltre(text, actualitzaComptador = true) {
  _filtre = text.toLowerCase().trim();
  const files = document.querySelectorAll("#gestio-tbody tr[data-nom]");
  let visibles = 0;

  files.forEach(fila => {
    const nom   = fila.dataset.nom   || "";
    const email = fila.dataset.email || "";
    const coincideix = !_filtre || nom.includes(_filtre) || email.includes(_filtre);
    fila.style.display = coincideix ? "" : "none";
    if (coincideix) visibles++;
  });

  if (actualitzaComptador) {
    const comptador = document.getElementById("gestio-total");
    if (comptador) {
      comptador.textContent = t("gestioUsuaris.total", [visibles]);
    }
  }
}

async function canviarRolUsuari(usuariId, nouRol, nomUsuari) {
  const nouRolText = nouRol === "ADMIN"
    ? t("gestioUsuaris.admin")
    : t("gestioUsuaris.user");

  const confirmat = confirm(t("gestioUsuaris.confirmar", [nomUsuari, nouRolText]));
  if (!confirmat) return;

  const resultat = await ApiAdmin.canviarRol(usuariId, nouRol);

  if (resultat) {
    const idx = _totsElsUsuaris.findIndex(u => u.id === usuariId);
    if (idx !== -1) _totsElsUsuaris[idx].rol = nouRol;

    showToast(t("gestioUsuaris.ok", [nomUsuari, nouRolText]), "success");

    const container = document.getElementById("gestio-usuaris-container");
    if (container) _pintaUsuaris(container, _totsElsUsuaris);
    if (_filtre) _aplicaFiltre(_filtre);
  } else {
    showToast(t("gestioUsuaris.error2"), "error");
  }
}

function _escapa(str) {
  return String(str || "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}
function _escapaAtribut(str) {
  return String(str || "").replace(/'/g, "\\'");
}