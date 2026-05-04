/* =====================================================
   IMPERIUM FITNESS — checkout.js
   Página de Pago (Checkout)
   -------------------------------------------------------
   Este archivo solo se carga en checkout.html.
   Gestiona:
     1. Mostrar el resumen del pedido
     2. Tarjeta de crédito decorativa en tiempo real
     3. Validación del formulario
     4. Guardar el pedido en localStorage
     5. Mostrar la pantalla de confirmación
   ===================================================== */


/* =====================================================
   1. RESUMEN DEL PEDIDO (columna derecha)
   ===================================================== */

/*
  renderOrderSummary() — lee el carrito y pinta las líneas
  de productos en la columna derecha del checkout.
  HTML generado en render.js → renderResumenPedido()
*/
function renderOrderSummary() {
  renderResumenPedido();
}


/* =====================================================
   2. TARJETA DECORATIVA (se actualiza en tiempo real)
   ===================================================== */

/*
  updateCardPreview() — se llama con oninput en los campos
  del formulario de tarjeta. Actualiza la tarjeta decorativa.
*/
function updateCardPreview() {
  const titular = document.getElementById("ch-titular");
  const numero  = document.getElementById("ch-tarjeta");
  const expira  = document.getElementById("ch-expira");

  // Nombre en la tarjeta
  const nameDisplay = document.getElementById("cc-name-display");
  if (nameDisplay && titular) {
    const val = titular.value.toUpperCase().trim();
    nameDisplay.textContent = val || "NOMBRE TITULAR";
  }

  // Número: muestra lo que hay y enmascara el resto con *
  const numDisplay = document.getElementById("cc-number-display");
  if (numDisplay && numero) {
    const digits = numero.value.replace(/\s/g, "");
    const padded = digits.padEnd(16, "*");
    // Agrupa en bloques de 4
    numDisplay.textContent =
      padded.slice(0,4) + " " +
      padded.slice(4,8) + " " +
      padded.slice(8,12) + " " +
      padded.slice(12,16);
  }

  // Fecha de caducidad
  const expDisplay = document.getElementById("cc-exp-display");
  if (expDisplay && expira) {
    expDisplay.textContent = expira.value || "MM/AA";
  }
}

/*
  formatCardNumber() — formatea el input del número de tarjeta
  para que muestre grupos de 4 dígitos separados por espacios.
  Ej: "1234567890123456" → "1234 5678 9012 3456"
*/
function formatCardNumber(input) {
  // Quita todo lo que no sea dígito
  let val = input.value.replace(/\D/g, "");
  // Agrupa en bloques de 4
  val = val.match(/.{1,4}/g)?.join(" ") || val;
  input.value = val;
}

/*
  formatExpiry() — formatea la fecha de caducidad "MMAA" → "MM/AA"
*/
function formatExpiry(input) {
  let val = input.value.replace(/\D/g, "");  // solo dígitos
  if (val.length > 2) {
    val = val.slice(0, 2) + "/" + val.slice(2, 4);
  }
  input.value = val;
}


/* =====================================================
   3. VALIDACIÓN DEL FORMULARIO
   ===================================================== */

/*
  validateField(id, condicion, mensajeError) — marca o desmarca
  un campo como erróneo.
  Devuelve true si el campo es válido, false si no.
*/
function validateField(id, isValid) {
  const group = document.getElementById(id)?.closest(".form-field");
  if (!group) return isValid;

  if (!isValid) {
    group.classList.add("error");
  } else {
    group.classList.remove("error");
  }
  return isValid;
}

/*
  validateForm() — valida todos los campos del checkout.
  Devuelve true si todo está correcto, false si hay errores.
*/
function validateForm() {
  let valid = true;

  // Nombre completo (no vacío)
  const nombre = document.getElementById("ch-nombre").value.trim();
  if (!validateField("ch-nombre", nombre.length >= 3)) valid = false;

  // Dirección (no vacía)
  const dir = document.getElementById("ch-direccion").value.trim();
  if (!validateField("ch-direccion", dir.length >= 5)) valid = false;

  // Ciudad (no vacía)
  const ciudad = document.getElementById("ch-ciudad").value.trim();
  if (!validateField("ch-ciudad", ciudad.length >= 2)) valid = false;

  // Código postal (5 dígitos)
  const cp = document.getElementById("ch-cp").value.trim();
  if (!validateField("ch-cp", /^\d{5}$/.test(cp))) valid = false;

  // Titular (no vacío)
  const titular = document.getElementById("ch-titular").value.trim();
  if (!validateField("ch-titular", titular.length >= 3)) valid = false;

  // Número de tarjeta (16 dígitos, ignorando espacios)
  const tarjeta = document.getElementById("ch-tarjeta").value.replace(/\s/g, "");
  if (!validateField("ch-tarjeta", /^\d{16}$/.test(tarjeta))) valid = false;

  // Fecha caducidad (formato MM/AA)
  const expira = document.getElementById("ch-expira").value.trim();
  if (!validateField("ch-expira", /^\d{2}\/\d{2}$/.test(expira))) valid = false;

  // CVV (3 dígitos)
  const cvv = document.getElementById("ch-cvv").value.trim();
  if (!validateField("ch-cvv", /^\d{3}$/.test(cvv))) valid = false;

  return valid;
}


/* =====================================================
   4. GUARDAR PEDIDO EN LOCALSTORAGE
   ===================================================== */

/*
  La clave en localStorage es "imperium_pedidos".
  Es un array de objetos:
  {
    pedidoId:  "PED-1713091200000",
    userEmail: "usuario@email.com",
    fecha:     "14/04/2026",
    productos: [ { id, name, price, quantity } ],
    total:     79.98
  }
*/
function saveOrder(userEmail) {
  const carrito = getCarrito();
  const total   = getCarritoTotalFinal();   // usa total con descuento

  const pedidoId = "PED-" + Date.now();

  const pedido = {
    pedidoId:  pedidoId,
    userEmail: userEmail,
    fecha:     new Date().toLocaleDateString("es-ES"),
    productos: carrito.map(function(item) {
      return {
        id:       item.id,
        name:     item.name,
        price:    item.price,
        quantity: item.quantity
      };
    }),
    total: total
  };

  const pedidosData = localStorage.getItem("imperium_pedidos");
  const pedidos = pedidosData ? JSON.parse(pedidosData) : [];
  pedidos.push(pedido);
  localStorage.setItem("imperium_pedidos", JSON.stringify(pedidos));

  // Si el carrito contiene un plan, actualiza el plan del usuario
  const itemPlan = carrito.find(function(item) { return item.esPlan; });
  if (itemPlan) {
    const user = Auth.getUser();
    if (user) {
      user.plan = itemPlan.planName;
      Auth.updateUser(user);
    }
  }

  return pedidoId;
}


/* =====================================================
   5. PROCESO DE PAGO COMPLETO
   ===================================================== */

/*
  processPay() — función llamada al pulsar "Pagar ahora".
  Pasos:
    1. Comprueba si hay sesión activa
    2. Valida el formulario
    3. Guarda el pedido
    4. Vacía el carrito
    5. Muestra la pantalla de confirmación
*/
function processPay() {
  // Paso 1: ¿Está logueado?
  const user = Auth.getUser();   // función de app.js
  if (!user) {
    showToast("Debes iniciar sesión para pagar.", "error");
    setTimeout(function() { window.location.href = "login.html"; }, 1500);
    return;
  }

  // Paso 2: ¿El formulario es válido?
  if (!validateForm()) {
    showToast("Revisa los campos marcados en rojo.", "error");
    return;
  }

  // Paso 3: Guardar el pedido en localStorage
  const pedidoId = saveOrder(user.email);

  // Paso 4: Vaciar el carrito
  vaciarCarrito();   // función de carrito.js

  // Paso 5: Mostrar pantalla de confirmación
  document.getElementById("checkout-form-section").style.display = "none";
  document.getElementById("checkout-success").style.display      = "block";
  document.getElementById("success-order-id").textContent        = "Pedido " + pedidoId;

  // Scroll al inicio de la sección para que se vea la confirmación
  window.scrollTo({ top: 0, behavior: "smooth" });
}


/* =====================================================
   6. ARRANQUE DEL CHECKOUT
   ===================================================== */

document.addEventListener("DOMContentLoaded", function() {

  // Solo ejecutamos si estamos en checkout.html
  if (!document.getElementById("pay-btn")) return;

  const user = Auth.getUser();

  /* Si el usuario NO está logueado: muestra el aviso y oculta el form */
  if (!user) {
    document.getElementById("login-required-box").style.display = "block";
    document.getElementById("checkout-layout").style.display    = "none";
    document.getElementById("pay-btn").style.display            = "none";
    return;   // No continuamos con el resto
  }

  /* El usuario SÍ está logueado: oculta el aviso de login */
  document.getElementById("login-required-box").style.display = "none";

  /* Precarga el nombre del usuario en el campo de envío */
  const nombreInput = document.getElementById("ch-nombre");
  if (nombreInput && user.name) {
    nombreInput.value = user.name;
  }

  /* Dibuja el resumen del pedido */
  renderOrderSummary();

  /* Conecta el botón de pago */
  document.getElementById("pay-btn").addEventListener("click", processPay);

  /* Quita el estado de error al escribir en un campo */
  document.querySelectorAll(".form-field input").forEach(function(input) {
    input.addEventListener("input", function() {
      this.closest(".form-field")?.classList.remove("error");
    });
  });

});
