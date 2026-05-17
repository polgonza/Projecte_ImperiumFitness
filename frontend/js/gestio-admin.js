/* =====================================================
   IMPERIUM FITNESS — gestio-admin.js
   Panell d'administració: gestió de productes i classes
   Depèn de: api.js, app.js (showToast), i18n.js (t)
   ===================================================== */

// Fragment suggerit per assistent IA - revisar i adaptar

/* ── Estat del mòdul ─────────────────────────────── */
let _productes    = [];
let _classes      = [];
let _editProducte = null; // producte en edició (null = nou)
let _editClasse   = null; // classe en edició (null = nova)

/* ── Gimnasos disponibles (de la BD) ──────────────── */
const GIMNASOS = [
  { id: 1, nom: "Imperium Fitness" },
  { id: 2, nom: "Imperium Fitness Nord" },
  { id: 3, nom: "Imperium Fitness Sud" }
];

/* ── Categories disponibles ───────────────────────── */
const CATEGORIES = ["Roba", "Suplement", "Accesoris"];

/* =====================================================
   RENDER PRINCIPAL
   ===================================================== */

async function renderGestioAdmin() {
  const container = document.getElementById("gestio-admin-container");
  if (!container) return;

  const user = Auth.getUser();
  if (!user || !user.roles || !user.roles.includes("ROLE_ADMIN")) {
    container.innerHTML = "";
    return;
  }

  container.innerHTML = `
    <div style="display:flex;gap:0;border-bottom:1px solid var(--border);margin-bottom:1.5rem">
      <button id="admin-tab-productes"
        onclick="canviaSubTab('productes')"
        style="padding:.6rem 1.25rem;background:transparent;border:none;
               border-bottom:2px solid var(--primary);color:var(--primary);
               font-size:.85rem;font-weight:600;cursor:pointer">
        📦 ${I18n.idioma === "ca" ? "Productes" : "Products"}
      </button>
      <button id="admin-tab-classes"
        onclick="canviaSubTab('classes')"
        style="padding:.6rem 1.25rem;background:transparent;border:none;
               border-bottom:2px solid transparent;color:var(--text-muted);
               font-size:.85rem;font-weight:600;cursor:pointer">
        🏋️ ${I18n.idioma === "ca" ? "Classes" : "Classes"}
      </button>
    </div>
    <div id="admin-subtab-productes"></div>
    <div id="admin-subtab-classes" style="display:none"></div>
  `;

  await Promise.all([carregaProductes(), carregaClasses()]);
}

function canviaSubTab(tab) {
  const tabs = ["productes", "classes"];
  tabs.forEach(t => {
    const btn = document.getElementById(`admin-tab-${t}`);
    const div = document.getElementById(`admin-subtab-${t}`);
    const actiu = t === tab;
    if (btn) {
      btn.style.borderBottomColor = actiu ? "var(--primary)" : "transparent";
      btn.style.color = actiu ? "var(--primary)" : "var(--text-muted)";
    }
    if (div) div.style.display = actiu ? "block" : "none";
  });
}

/* =====================================================
   PRODUCTES
   ===================================================== */

async function carregaProductes() {
  const container = document.getElementById("admin-subtab-productes");
  if (!container) return;

  container.innerHTML = `<p style="color:var(--text-muted);text-align:center;padding:2rem">
    ⏳ ${I18n.idioma === "ca" ? "Carregant productes..." : "Loading products..."}</p>`;

  try {
    const res = await apiFetch("/api/productes");
    _productes = res && res.ok ? await res.json() : [];
  } catch (e) { _productes = []; }

  pintaProductes(container);
}

function pintaProductes(container) {
  const btnText = I18n.idioma === "ca" ? "Nou producte" : "New product";

  container.innerHTML = `
    <div style="display:flex;justify-content:flex-end;margin-bottom:1rem">
      <button onclick="obreFormProducte(null)"
        class="btn btn-primary" style="font-size:.82rem;padding:.45rem 1rem">
        + ${btnText}
      </button>
    </div>

    <div id="form-producte-container"></div>

    <div style="overflow-x:auto">
      <table style="width:100%;border-collapse:collapse;font-size:.85rem">
        <thead>
          <tr style="border-bottom:1px solid var(--border)">
            <th style="text-align:left;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Nom" : "Name"}
            </th>
            <th style="text-align:left;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Categoria" : "Category"}
            </th>
            <th style="text-align:right;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Preu" : "Price"}
            </th>
            <th style="text-align:right;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Estoc" : "Stock"}
            </th>
            <th style="text-align:center;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Accions" : "Actions"}
            </th>
          </tr>
        </thead>
        <tbody>
          ${_productes.length === 0
            ? `<tr><td colspan="5" style="text-align:center;padding:2rem;color:var(--text-muted)">
                ${I18n.idioma === "ca" ? "No hi ha productes." : "No products found."}
               </td></tr>`
            : _productes.map(p => `
              <tr style="border-bottom:.5px solid var(--border)">
                <td style="padding:.6rem .75rem;font-weight:500">${_esc(p.nom)}</td>
                <td style="padding:.6rem .75rem;color:var(--text-muted)">${_esc(p.categoria || "—")}</td>
                <td style="padding:.6rem .75rem;text-align:right">${parseFloat(p.preu).toFixed(2)} €</td>
                <td style="padding:.6rem .75rem;text-align:right">
                  <span style="color:${p.estoc <= 5 ? "var(--danger)" : "var(--text)"}">
                    ${p.estoc}
                  </span>
                </td>
                <td style="padding:.6rem .75rem;text-align:center;display:flex;gap:.5rem;justify-content:center">
                  <button onclick="obreFormProducte(${p.id})"
                    style="padding:.3rem .65rem;border:1px solid var(--border);border-radius:6px;
                           background:transparent;color:var(--text-muted);cursor:pointer;font-size:.75rem">
                    ✏️
                  </button>
                  <button onclick="eliminaProducte(${p.id}, '${_escAttr(p.nom)}')"
                    style="padding:.3rem .65rem;border:1px solid var(--danger);border-radius:6px;
                           background:transparent;color:var(--danger);cursor:pointer;font-size:.75rem">
                    🗑️
                  </button>
                </td>
              </tr>`).join("")
          }
        </tbody>
      </table>
    </div>
  `;
}

function obreFormProducte(id) {
  _editProducte = id ? _productes.find(p => p.id === id) : null;
  const p = _editProducte;
  const isNou = !p;
  const titol = isNou
    ? (I18n.idioma === "ca" ? "Nou producte" : "New product")
    : (I18n.idioma === "ca" ? "Editar producte" : "Edit product");

  const container = document.getElementById("form-producte-container");
  if (!container) return;

  const catsOptions = CATEGORIES.map(c =>
    `<option value="${c}" ${p?.categoria === c ? "selected" : ""}>${c}</option>`
  ).join("");

  container.innerHTML = `
    <div style="background:var(--bg-secondary);border:1px solid var(--border);
                border-radius:12px;padding:1.25rem;margin-bottom:1.5rem">
      <h4 style="font-size:.9rem;font-weight:600;margin-bottom:1rem">${titol}</h4>

      <div style="display:grid;grid-template-columns:1fr 1fr;gap:.75rem">

        <div style="grid-column:1/-1">
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Nom *" : "Name *"}
          </label>
          <input id="fp-nom" type="text" value="${_esc(p?.nom || "")}"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
        </div>

        <div>
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Preu (€) *" : "Price (€) *"}
          </label>
          <input id="fp-preu" type="number" step="0.01" min="0"
            value="${p?.preu || ""}"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
        </div>

        <div>
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Estoc *" : "Stock *"}
          </label>
          <input id="fp-estoc" type="number" min="0"
            value="${p?.estoc ?? ""}"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
        </div>

        <div>
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Categoria *" : "Category *"}
          </label>
          <select id="fp-categoria"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
            ${catsOptions}
          </select>
        </div>

        <div style="grid-column:1/-1">
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Descripció" : "Description"}
          </label>
          <input id="fp-descripcio" type="text" value="${_esc(p?.descripcio || "")}"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
        </div>

        <div style="grid-column:1/-1">
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Imatge — URL o fitxer local" : "Image — URL or local file"}
          </label>
          <div style="display:flex;gap:.5rem;align-items:center;flex-wrap:wrap">
            <input id="fp-imatge-url" type="text"
              placeholder="${I18n.idioma === "ca" ? "https://..." : "https://..."}"
              value="${p?.imatgeUrl && !p.imatgeUrl.startsWith("data:") ? _esc(p.imatgeUrl) : ""}"
              oninput="previsuImatgeUrl()"
              style="flex:1;min-width:180px;padding:.5rem .75rem;background:var(--bg-card);
                     border:1px solid var(--border);border-radius:8px;
                     color:var(--text);font-size:.85rem">
            <label style="padding:.45rem .85rem;background:var(--bg-card);
                          border:1px solid var(--border);border-radius:8px;
                          font-size:.78rem;color:var(--text-muted);cursor:pointer;
                          white-space:nowrap">
              📁 ${I18n.idioma === "ca" ? "Selecciona fitxer" : "Select file"}
              <input type="file" accept="image/*" id="fp-imatge-file"
                style="display:none" onchange="carregaImatgeLocal(this)">
            </label>
          </div>
          <div id="fp-imatge-preview" style="margin-top:.5rem">
            ${p?.imatgeUrl ? `<img src="${p.imatgeUrl}" style="height:80px;border-radius:6px;object-fit:cover">` : ""}
          </div>
        </div>

      </div>

      <div style="display:flex;gap:.75rem;justify-content:flex-end;margin-top:1rem">
        <button onclick="tancaFormProducte()"
          class="btn btn-secondary" style="font-size:.82rem;padding:.45rem 1rem">
          ${I18n.idioma === "ca" ? "Cancel·lar" : "Cancel"}
        </button>
        <button onclick="desaProducte()"
          class="btn btn-primary" style="font-size:.82rem;padding:.45rem 1rem">
          ${I18n.idioma === "ca" ? "Desar" : "Save"}
        </button>
      </div>
    </div>
  `;

  container.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

function previsuImatgeUrl() {
  const url = document.getElementById("fp-imatge-url")?.value.trim();
  const preview = document.getElementById("fp-imatge-preview");
  if (!preview) return;
  preview.innerHTML = url
    ? `<img src="${url}" style="height:80px;border-radius:6px;object-fit:cover"
            onerror="this.style.display='none'">`
    : "";
}

// Fragment suggerit per assistent IA - revisar i adaptar
function carregaImatgeLocal(input) {
  const file = input.files[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = function(e) {
    const base64 = e.target.result;
    // Neteja la URL manual i mostra la previsualització
    const urlInput = document.getElementById("fp-imatge-url");
    if (urlInput) urlInput.value = "";
    const preview = document.getElementById("fp-imatge-preview");
    if (preview) {
      preview.innerHTML = `<img src="${base64}" style="height:80px;border-radius:6px;object-fit:cover">`;
    }
    // Guardem el base64 com a data-attribute per recuperar-lo al desar
    input.setAttribute("data-base64", base64);
  };
  reader.readAsDataURL(file);
}

function tancaFormProducte() {
  const container = document.getElementById("form-producte-container");
  if (container) container.innerHTML = "";
  _editProducte = null;
}

async function desaProducte() {
  const nom       = document.getElementById("fp-nom")?.value.trim();
  const preu      = parseFloat(document.getElementById("fp-preu")?.value);
  const estoc     = parseInt(document.getElementById("fp-estoc")?.value);
  const categoria = document.getElementById("fp-categoria")?.value;
  const descripcio= document.getElementById("fp-descripcio")?.value.trim();
  const urlInput  = document.getElementById("fp-imatge-url")?.value.trim();
  const fileInput = document.getElementById("fp-imatge-file");
  const base64    = fileInput?.getAttribute("data-base64") || null;

  if (!nom || isNaN(preu) || isNaN(estoc)) {
    showToast(t("toast.campsBuits"), "error");
    return;
  }

  const imatgeUrl = base64 || urlInput || null;

  const dto = { nom, descripcio, preu, categoria, estoc, imatgeUrl };

  let resultat;
  if (_editProducte) {
    resultat = await ApiProductes.actualitzar(_editProducte.id, dto);
  } else {
    resultat = await ApiProductes.crear(dto);
  }

  if (resultat) {
    showToast(
      I18n.idioma === "ca" ? "Producte desat correctament." : "Product saved successfully.",
      "success"
    );
    tancaFormProducte();
    await carregaProductes();
  } else {
    showToast(
      I18n.idioma === "ca" ? "Error en desar el producte." : "Error saving product.",
      "error"
    );
  }
}

async function eliminaProducte(id, nom) {
  const confirmat = confirm(
    I18n.idioma === "ca"
      ? `Segur que vols eliminar "${nom}"?`
      : `Are you sure you want to delete "${nom}"?`
  );
  if (!confirmat) return;

  const ok = await ApiProductes.eliminar(id);
  if (ok) {
    showToast(
      I18n.idioma === "ca" ? `"${nom}" eliminat.` : `"${nom}" deleted.`,
      "success"
    );
    await carregaProductes();
  } else {
    showToast(
      I18n.idioma === "ca" ? "No s'ha pogut eliminar." : "Could not delete.",
      "error"
    );
  }
}

/* =====================================================
   CLASSES
   ===================================================== */

async function carregaClasses() {
  const container = document.getElementById("admin-subtab-classes");
  if (!container) return;

  try {
    const res = await apiFetch("/api/classes");
    _classes = res && res.ok ? await res.json() : [];
  } catch (e) { _classes = []; }

  pintaClasses(container);
}

function pintaClasses(container) {
  const btnText = I18n.idioma === "ca" ? "Nova classe" : "New class";

  container.innerHTML = `
    <div style="display:flex;justify-content:flex-end;margin-bottom:1rem">
      <button onclick="obreFormClasse(null)"
        class="btn btn-primary" style="font-size:.82rem;padding:.45rem 1rem">
        + ${btnText}
      </button>
    </div>

    <div id="form-classe-container"></div>

    <div style="overflow-x:auto">
      <table style="width:100%;border-collapse:collapse;font-size:.85rem">
        <thead>
          <tr style="border-bottom:1px solid var(--border)">
            <th style="text-align:left;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Nom" : "Name"}
            </th>
            <th style="text-align:left;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Horari" : "Schedule"}
            </th>
            <th style="text-align:right;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Capacitat" : "Capacity"}
            </th>
            <th style="text-align:left;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Gimnas" : "Gym"}
            </th>
            <th style="text-align:center;padding:.5rem .75rem;color:var(--text-muted);
                       font-size:.72rem;text-transform:uppercase;letter-spacing:.06em">
              ${I18n.idioma === "ca" ? "Accions" : "Actions"}
            </th>
          </tr>
        </thead>
        <tbody>
          ${_classes.length === 0
            ? `<tr><td colspan="5" style="text-align:center;padding:2rem;color:var(--text-muted)">
                ${I18n.idioma === "ca" ? "No hi ha classes." : "No classes found."}
               </td></tr>`
            : _classes.map(c => {
                const horari = c.horari
                  ? new Date(c.horari).toLocaleString(I18n.idioma === "ca" ? "ca-ES" : "en-GB",
                      { day:"2-digit", month:"2-digit", year:"numeric", hour:"2-digit", minute:"2-digit" })
                  : "—";
                const gimnasNom = GIMNASOS.find(g => g.id === c.gimnasId)?.nom || "—";
                return `
                  <tr style="border-bottom:.5px solid var(--border)">
                    <td style="padding:.6rem .75rem;font-weight:500">${_esc(c.nom)}</td>
                    <td style="padding:.6rem .75rem;color:var(--text-muted);font-size:.8rem">${horari}</td>
                    <td style="padding:.6rem .75rem;text-align:right">${c.capacitat}</td>
                    <td style="padding:.6rem .75rem;color:var(--text-muted);font-size:.8rem">${gimnasNom}</td>
                    <td style="padding:.6rem .75rem;text-align:center;display:flex;gap:.5rem;justify-content:center">
                      <button onclick="obreFormClasse(${c.id})"
                        style="padding:.3rem .65rem;border:1px solid var(--border);border-radius:6px;
                               background:transparent;color:var(--text-muted);cursor:pointer;font-size:.75rem">
                        ✏️
                      </button>
                      <button onclick="eliminaClasse(${c.id}, '${_escAttr(c.nom)}')"
                        style="padding:.3rem .65rem;border:1px solid var(--danger);border-radius:6px;
                               background:transparent;color:var(--danger);cursor:pointer;font-size:.75rem">
                        🗑️
                      </button>
                    </td>
                  </tr>`;
              }).join("")
          }
        </tbody>
      </table>
    </div>
  `;
}

function obreFormClasse(id) {
  _editClasse = id ? _classes.find(c => c.id === id) : null;
  const c = _editClasse;
  const isNou = !c;
  const titol = isNou
    ? (I18n.idioma === "ca" ? "Nova classe" : "New class")
    : (I18n.idioma === "ca" ? "Editar classe" : "Edit class");

  // Formatem el datetime-local
  let horariVal = "";
  if (c?.horari) {
    const d = new Date(c.horari);
    const pad = n => String(n).padStart(2,"0");
    horariVal = `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
  }

  const gimnasOptions = GIMNASOS.map(g =>
    `<option value="${g.id}" ${c?.gimnasId === g.id ? "selected" : ""}>${g.nom}</option>`
  ).join("");

  const container = document.getElementById("form-classe-container");
  if (!container) return;

  container.innerHTML = `
    <div style="background:var(--bg-secondary);border:1px solid var(--border);
                border-radius:12px;padding:1.25rem;margin-bottom:1.5rem">
      <h4 style="font-size:.9rem;font-weight:600;margin-bottom:1rem">${titol}</h4>

      <div style="display:grid;grid-template-columns:1fr 1fr;gap:.75rem">

        <div style="grid-column:1/-1">
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Nom *" : "Name *"}
          </label>
          <input id="fc-nom" type="text" value="${_esc(c?.nom || "")}"
            placeholder="${I18n.idioma === "ca" ? "ex: Spinning matinal" : "e.g. Morning Spinning"}"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
        </div>

        <div>
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Data i hora *" : "Date & time *"}
          </label>
          <input id="fc-horari" type="datetime-local" value="${horariVal}"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
        </div>

        <div>
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Capacitat *" : "Capacity *"}
          </label>
          <input id="fc-capacitat" type="number" min="1" value="${c?.capacitat || ""}"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
        </div>

        <div>
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Gimnas *" : "Gym *"}
          </label>
          <select id="fc-gimnas"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
            ${gimnasOptions}
          </select>
        </div>

        <div style="grid-column:1/-1">
          <label style="font-size:.78rem;color:var(--text-muted);display:block;margin-bottom:.3rem">
            ${I18n.idioma === "ca" ? "Descripció" : "Description"}
          </label>
          <input id="fc-descripcio" type="text" value="${_esc(c?.descripcio || "")}"
            style="width:100%;padding:.5rem .75rem;background:var(--bg-card);
                   border:1px solid var(--border);border-radius:8px;
                   color:var(--text);font-size:.85rem">
        </div>

      </div>

      <div style="display:flex;gap:.75rem;justify-content:flex-end;margin-top:1rem">
        <button onclick="tancaFormClasse()"
          class="btn btn-secondary" style="font-size:.82rem;padding:.45rem 1rem">
          ${I18n.idioma === "ca" ? "Cancel·lar" : "Cancel"}
        </button>
        <button onclick="desaClasse()"
          class="btn btn-primary" style="font-size:.82rem;padding:.45rem 1rem">
          ${I18n.idioma === "ca" ? "Desar" : "Save"}
        </button>
      </div>
    </div>
  `;

  container.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

function tancaFormClasse() {
  const container = document.getElementById("form-classe-container");
  if (container) container.innerHTML = "";
  _editClasse = null;
}

async function desaClasse() {
  const nom       = document.getElementById("fc-nom")?.value.trim();
  const horariRaw = document.getElementById("fc-horari")?.value;
  const capacitat = parseInt(document.getElementById("fc-capacitat")?.value);
  const gimnasId  = parseInt(document.getElementById("fc-gimnas")?.value);
  const descripcio= document.getElementById("fc-descripcio")?.value.trim();

  if (!nom || !horariRaw || isNaN(capacitat)) {
    showToast(t("toast.campsBuits"), "error");
    return;
  }

  const dto = { nom, descripcio, horari: horariRaw, capacitat, gimnasId };

  let resultat;
  if (_editClasse) {
    resultat = await ApiClassesAdmin.actualitzar(_editClasse.id, dto);
  } else {
    resultat = await ApiClassesAdmin.crear(dto);
  }

  if (resultat) {
    showToast(
      I18n.idioma === "ca" ? "Classe desada correctament." : "Class saved successfully.",
      "success"
    );
    tancaFormClasse();
    await carregaClasses();
  } else {
    showToast(
      I18n.idioma === "ca" ? "Error en desar la classe." : "Error saving class.",
      "error"
    );
  }
}

async function eliminaClasse(id, nom) {
  const confirmat = confirm(
    I18n.idioma === "ca"
      ? `Segur que vols eliminar la classe "${nom}"?\nS'eliminaran totes les reserves associades.`
      : `Are you sure you want to delete "${nom}"?\nAll associated bookings will be removed.`
  );
  if (!confirmat) return;

  const ok = await ApiClassesAdmin.eliminar(id);
  if (ok) {
    showToast(
      I18n.idioma === "ca" ? `Classe "${nom}" eliminada.` : `Class "${nom}" deleted.`,
      "success"
    );
    await carregaClasses();
  } else {
    showToast(
      I18n.idioma === "ca" ? "No s'ha pogut eliminar." : "Could not delete.",
      "error"
    );
  }
}

/* ── Utilitats ───────────────────────────────────── */
function _esc(str) {
  return String(str || "")
    .replace(/&/g,"&amp;").replace(/</g,"&lt;")
    .replace(/>/g,"&gt;").replace(/"/g,"&quot;");
}
function _escAttr(str) {
  return String(str || "").replace(/'/g,"\\'");
}

/* Re-renderitza quan canvia l'idioma */
document.addEventListener("idioma:canvi", function() {
  renderGestioAdmin();
});