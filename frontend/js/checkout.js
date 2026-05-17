/* =====================================================
   IMPERIUM FITNESS — checkout.js
   ===================================================== */


/* ── 1. RESUMEN DEL PEDIDO ── */

function renderOrderSummary() {
  renderResumenPedido();
}


/* ── 2. TARJETA DECORATIVA ── */

function updateCardPreview() {
  const titular = document.getElementById("ch-titular");
  const numero  = document.getElementById("ch-tarjeta");
  const expira  = document.getElementById("ch-expira");

  const nameDisplay = document.getElementById("cc-name-display");
  if (nameDisplay && titular) {
    nameDisplay.textContent = titular.value.toUpperCase().trim() || 
  (I18n.idioma === "ca" ? "NOM TITULAR" : "CARDHOLDER NAME");
  }

  const numDisplay = document.getElementById("cc-number-display");
  if (numDisplay && numero) {
    const digits = numero.value.replace(/\s/g, "").padEnd(16, "*");
    numDisplay.textContent = digits.slice(0,4) + " " + digits.slice(4,8) + " " +
                             digits.slice(8,12) + " " + digits.slice(12,16);
  }

  const expDisplay = document.getElementById("cc-exp-display");
  if (expDisplay && expira) {
    expDisplay.textContent = expira.value || "MM/AA";
  }
}

function formatCardNumber(input) {
  let val = input.value.replace(/\D/g, "");
  val = val.match(/.{1,4}/g)?.join(" ") || val;
  input.value = val;
}

function formatExpiry(input) {
  let val = input.value.replace(/\D/g, "");
  if (val.length > 2) val = val.slice(0, 2) + "/" + val.slice(2, 4);
  input.value = val;
}


/* ── 3. VALIDACIÓ ── */

function validateField(id, isValid) {
  const group = document.getElementById(id)?.closest(".form-field");
  if (!group) return isValid;
  group.classList.toggle("error", !isValid);
  return isValid;
}

function validateForm() {
  let valid = true;
  const nom     = document.getElementById("ch-nombre").value.trim();
  const dir     = document.getElementById("ch-direccion").value.trim();
  const ciudad  = document.getElementById("ch-ciudad").value.trim();
  const cp      = document.getElementById("ch-cp").value.trim();
  const titular = document.getElementById("ch-titular").value.trim();
  const tarjeta = document.getElementById("ch-tarjeta").value.replace(/\s/g, "");
  const expira  = document.getElementById("ch-expira").value.trim();
  const cvv     = document.getElementById("ch-cvv").value.trim();

  if (!validateField("ch-nombre",    nom.length >= 3))          valid = false;
  if (!validateField("ch-direccion", dir.length >= 5))          valid = false;
  if (!validateField("ch-ciudad",    ciudad.length >= 2))       valid = false;
  if (!validateField("ch-cp",        /^\d{5}$/.test(cp)))       valid = false;
  if (!validateField("ch-titular",   titular.length >= 3))      valid = false;
  if (!validateField("ch-tarjeta",   /^\d{16}$/.test(tarjeta))) valid = false;
  if (!validateField("ch-expira",    /^\d{2}\/\d{2}$/.test(expira))) valid = false;
  if (!validateField("ch-cvv",       /^\d{3}$/.test(cvv)))      valid = false;

  return valid;
}


/* ── 4. GUARDAR PEDIDO ── */

function saveOrder(userEmail) {
  const carrito  = getCarrito();
  const total    = getCarritoTotalFinal();
  const pedidoId = "PED-" + Date.now();

  const pedido = {
    pedidoId,
    userEmail,
    fecha: new Date().toLocaleDateString(I18n.idioma === "ca" ? "ca-ES" : "en-GB"),
    productos: carrito.map(item => ({
      id:       item.id,
      name:     item.name,
      price:    item.price,
      quantity: item.quantity
    })),
    total
  };

  const pedidos = JSON.parse(localStorage.getItem("imperium_pedidos") || "[]");
  pedidos.push(pedido);
  localStorage.setItem("imperium_pedidos", JSON.stringify(pedidos));

  return pedidoId;
}

/* Registra les vendes a la BD via API */
async function registrarVendesALaBD(usuariId) {
  const carrito = getCarrito();
  const user    = Auth.getUser();
  if (!user || !usuariId) return;

  // Per cada producte del carrito creem una venda a la BD
  for (const item of carrito) {
    try {
      await apiFetch("/api/vendes", {
        method: "POST",
        body: JSON.stringify({
          usuariId:   parseInt(usuariId),
          producteId: parseInt(item.id),
          quantitat:  item.quantity
        })
      });
    } catch (e) {
      console.error("Error registrant venda:", e);
    }
  }
}


/* ── 5. PROCÉS DE PAGAMENT ── */

async function processPay() {
  const user = Auth.getUser();
  if (!user) {
    showToast(I18n.idioma === "ca" ? "Has d'iniciar sessió per pagar." : "You need to log in to pay.", "error");
    setTimeout(() => { window.location.href = "login.html"; }, 1500);
    return;
  }

  if (!validateForm()) {
    showToast(I18n.idioma === "ca" ? "Revisa els camps marcats en vermell." : "Please check the fields marked in red.", "error");
    return;
  }

  // Deshabilitem el botó mentre procesem
  const payBtn = document.getElementById("pay-btn");
  if (payBtn) {
    payBtn.disabled = true;
    payBtn.textContent = I18n.idioma === "ca" ? "Processant..." : "Processing...";
  }

  // Guardem a localStorage
  const pedidoId = saveOrder(user.email);

  // Registrem les vendes a la BD
  await registrarVendesALaBD(user.id);

  // Buidem el carrito
  vaciarCarrito();

  // Mostrem confirmació
  document.getElementById("checkout-form-section").style.display = "none";
  document.getElementById("checkout-success").style.display      = "block";
  document.getElementById("success-order-id").textContent = 
  (I18n.idioma === "ca" ? "Comanda " : "Order ") + pedidoId;

  window.scrollTo({ top: 0, behavior: "smooth" });
}


/* ── 6. ARRANQUE ── */

document.addEventListener("DOMContentLoaded", function() {
  if (!document.getElementById("pay-btn")) return;

  const user = Auth.getUser();

  if (!user) {
    document.getElementById("login-required-box").style.display = "block";
    document.getElementById("checkout-layout").style.display    = "none";
    document.getElementById("pay-btn").style.display            = "none";
    return;
  }

  document.getElementById("login-required-box").style.display = "none";

  const nombreInput = document.getElementById("ch-nombre");
  if (nombreInput && user.name) nombreInput.value = user.name;

  renderOrderSummary();

  // Connectem el botó de pagament (ara és async)
  document.getElementById("pay-btn").addEventListener("click", processPay);

  document.querySelectorAll(".form-field input").forEach(function(input) {
    input.addEventListener("input", function() {
      this.closest(".form-field")?.classList.remove("error");
    });
  });
  document.addEventListener("idioma:canvi", function() {
    renderOrderSummary();
  });
});