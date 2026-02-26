// ... CONFIGURACIÓN ...
const API = "http://localhost:8080/api";
const messageEl = document.getElementById("message");
const token = localStorage.getItem("token");

if (!token) window.location.href = "login.html";

let user = null;
let miGrafico = null; 
let globalGastos = [];
let globalIngresos = [];

function authHeaders() {
  return { "Content-Type": "application/json", "Authorization": `Bearer ${token}` };
}

function showMessage(text) {
  if(messageEl) {
      messageEl.textContent = text;
      messageEl.style.opacity = "1";
      setTimeout(() => { messageEl.style.opacity = "0"; }, 3000);
  }
}

function formatoMoneda(valor) {
  return new Intl.NumberFormat('es-AR', {
    style: 'currency',
    currency: 'ARS',
    minimumFractionDigits: 2
  }).format(valor);
}

/* --- GRÁFICOS --- */
function generarGrafico(gastos) {
  const canvas = document.getElementById('gastosChart');
  if (!canvas) return;
  if (miGrafico) { miGrafico.destroy(); miGrafico = null; }
  const ctx = canvas.getContext('2d');
  const datosAgrupados = {};
  gastos.forEach(g => {
    const cat = g.categoriaNombre || "Sin categoría";
    datosAgrupados[cat] = (datosAgrupados[cat] || 0) + Number(g.monto);
  });
  miGrafico = new Chart(ctx, {
    type: 'doughnut', 
    data: {
      labels: Object.keys(datosAgrupados),
      datasets: [{
        data: Object.values(datosAgrupados),
        backgroundColor: ['#2ac9bb', '#ff6384', '#36a2eb', '#ffce56', '#9966ff'],
        borderWidth: 2, borderColor: '#1a1a1a'
      }]
    },
    options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom', labels: { color: '#ffffff' } } } }
  });
}

function calcularSaldosPorCuenta(gastos, ingresos) {
  const saldos = { "BNA": 0, "MERCADO_PAGO": 0, "EFECTIVO": 0 };
  ingresos.forEach(i => { const m = i.medioPago || "EFECTIVO"; if (saldos.hasOwnProperty(m)) saldos[m] += Number(i.monto); });
  gastos.forEach(g => { const m = g.medioPago || "EFECTIVO"; if (saldos.hasOwnProperty(m)) saldos[m] -= Number(g.monto); });
  if(document.getElementById("saldoBNA")) document.getElementById("saldoBNA").textContent = formatoMoneda(saldos["BNA"]);
  if(document.getElementById("saldoMP")) document.getElementById("saldoMP").textContent = formatoMoneda(saldos["MERCADO_PAGO"]);
  if(document.getElementById("saldoEfectivo")) document.getElementById("saldoEfectivo").textContent = formatoMoneda(saldos["EFECTIVO"]);
}

function cargarSelectorFechas() {
  const selector = document.getElementById("filtroFechaMes");
  if (!selector) return;
  const meses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
  const anios = [2025, 2026];
  selector.innerHTML = "";
  anios.forEach(anio => {
    meses.forEach((mes, index) => {
      const option = document.createElement("option");
      const mesNum = (index + 1).toString().padStart(2, '0');
      option.value = `${anio}-${mesNum}`;
      option.textContent = `${mes} ${anio}`;
      selector.appendChild(option);
    });
  });
  const hoy = new Date();
  const mesActual = (hoy.getMonth() + 1).toString().padStart(2, '0');
  selector.value = `${hoy.getFullYear()}-${mesActual}`;
  selector.onchange = () => refreshAll();
}

async function fetchUserInfo() {
  try {
    const res = await fetch(`${API}/usuarios/me`, { headers: authHeaders() });
    if (!res.ok) throw new Error("Error auth");
    user = await res.json();
    if(document.getElementById("userEmail")) document.getElementById("userEmail").textContent = user.email;
  } catch (e) { window.location.href = "login.html"; }
}
async function fetchCategorias() { try { const res = await fetch(`${API}/categorias`, { headers: authHeaders() }); const data = await res.json(); renderCategorias(data); return data; } catch (e) { return []; } }
async function fetchGastos() { const res = await fetch(`${API}/gastos/usuario/${user.id}`, { headers: authHeaders() }); const data = await res.json(); globalGastos = data; return data; }
async function fetchIngresos() { const res = await fetch(`${API}/ingresos/usuario/${user.id}`, { headers: authHeaders() }); const data = await res.json(); globalIngresos = data; return data; }

function renderCategorias(categorias) {
  const gSelect = document.getElementById("gastoCategoria");
  const iSelect = document.getElementById("ingresoCategoria");
  const filtroSel = document.getElementById("filtroCategoriaSelect");
  const lista = document.getElementById("listaCategorias");
  [gSelect, iSelect, filtroSel].forEach(select => {
    if (!select) return;
    const valPrevio = select.value;
    select.innerHTML = select === filtroSel ? '<option value="all">Mostrar todas</option>' : '<option value="">Sin categoría</option>';
    categorias.forEach(cat => { const opt = document.createElement("option"); opt.value = cat.id; opt.textContent = cat.nombre; select.appendChild(opt); });
    if(valPrevio) select.value = valPrevio;
  });
  if (document.getElementById("countCategorias")) document.getElementById("countCategorias").textContent = categorias.length;
  if (lista) {
    lista.innerHTML = "";
    categorias.forEach(cat => {
      const li = document.createElement("li");
      li.style.cssText = "display: flex; justify-content: space-between; padding: 5px; border-bottom: 1px solid #333; color: white;";
      li.innerHTML = `<span>${cat.nombre}</span><button onclick="eliminarCategoria(${cat.id})" style="background: #ff4444; color: white; border: none; cursor: pointer;">X</button>`;
      lista.appendChild(li);
    });
  }
}

function renderGastosFijos(lista) {
  const tbody = document.querySelector("#tablaGastosFijos tbody");
  if (!tbody) return; tbody.innerHTML = "";
  lista.forEach(g => {
    const acciones = `<button onclick="abrirModalEditarGasto(${g.id})" style="margin-right:5px;cursor:pointer;background:none;border:none;font-size:1.2rem;">✏️</button><button class="btn-delete" onclick="eliminarGasto(${g.id})">🗑️</button>`;
    const estadoPago = g.pagado ? '<span style="color:#2ac9bb;font-weight:bold;">SÍ</span>' : '<span style="color:#ff6384;font-weight:bold;">NO</span>';
    tbody.innerHTML += `<tr><td>${g.descripcion||"-"}</td><td>${formatoMoneda(g.monto)}</td><td style="color:#ffce56;">${g.fechaVencimiento||"-"}</td><td><span class="badge-categoria">${g.categoriaNombre||"-"}</span></td><td>${estadoPago}</td><td>${g.fecha||"-"}</td><td>${g.medioPago||"EFECTIVO"}</td><td>${acciones}</td></tr>`;
  });
}
function renderGastosVariables(lista) {
  const tbody = document.querySelector("#tablaGastosVariables tbody");
  if (!tbody) return; tbody.innerHTML = "";
  lista.forEach(g => {
    const acciones = `<button onclick="abrirModalEditarGasto(${g.id})" style="margin-right:5px;cursor:pointer;background:none;border:none;font-size:1.2rem;">✏️</button><button class="btn-delete" onclick="eliminarGasto(${g.id})">🗑️</button>`;
    tbody.innerHTML += `<tr><td>${g.fecha}</td><td>${g.descripcion||"-"}</td><td><span class="badge-categoria">${g.categoriaNombre||"-"}</span></td><td>${g.medioPago||"EFECTIVO"}</td><td>${formatoMoneda(g.monto)}</td><td>${acciones}</td></tr>`;
  });
}
function renderIngresos(ingresos) {
  const tbody = document.querySelector('#tablaIngresos tbody');
  if (tbody) tbody.innerHTML = '';
  ingresos.forEach(i => {
    const acciones = `<button onclick="abrirModalEditarIngreso(${i.id})" style="margin-right:5px;cursor:pointer;background:none;border:none;font-size:1.2rem;">✏️</button><button class="btn btn-danger" onclick="eliminarIngreso(${i.id})">🗑️</button>`;
    tbody.innerHTML += `<tr><td>${i.fecha}</td><td>${i.descripcion||'-'}</td><td>${i.medioPago||'EFECTIVO'}</td><td>${i.categoriaNombre||'-'}</td><td>${formatoMoneda(i.monto)}</td><td>${acciones}</td></tr>`;
  });
}

async function refreshAll() {
  await fetchCategorias(); if(!user) return; 
  const gTodos = await fetchGastos(); const iTodos = await fetchIngresos();
  const selector = document.getElementById("filtroFechaMes");
  const mesSeleccionado = selector ? selector.value : new Date().toISOString().slice(0, 7);
  const gFiltrados = gTodos.filter(g => (g.fecha||g.fechaVencimiento||"").startsWith(mesSeleccionado));
  const iFiltrados = iTodos.filter(i => i.fecha.startsWith(mesSeleccionado));
  const gFijos = gFiltrados.filter(g => g.esFijo);
  const gVariables = gFiltrados.filter(g => !g.esFijo);
  const totalG = gFijos.reduce((s,x)=>s+Number(x.monto),0) + gVariables.reduce((s,x)=>s+Number(x.monto),0);
  const totalI = iFiltrados.reduce((s,x)=>s+Number(x.monto),0);
  
  if(document.getElementById("totalGastado")) document.getElementById("totalGastado").textContent = formatoMoneda(totalG);
  if(document.getElementById("totalFijos")) document.getElementById("totalFijos").textContent = formatoMoneda(gFijos.reduce((s,x)=>s+Number(x.monto),0));
  if(document.getElementById("totalVariables")) document.getElementById("totalVariables").textContent = formatoMoneda(gVariables.reduce((s,x)=>s+Number(x.monto),0));
  const elBal = document.getElementById("balanceTotal");
  if(elBal) {
    const bal = totalI - totalG;
    elBal.textContent = formatoMoneda(bal);
    elBal.className = "highlight " + (bal >= 0 ? "positivo" : "negativo");
  }
  renderGastosFijos(gFijos); renderGastosVariables(gVariables); renderIngresos(iFiltrados); calcularSaldosPorCuenta(gFiltrados, iFiltrados); generarGrafico(gFiltrados);
}

document.getElementById("gastoEsFijo").onchange = (e) => { document.getElementById("camposFijos").style.display = e.target.checked ? "block" : "none"; };

// --- ACCIONES MODALES Y BORRADO ---
window.abrirModalEditarGasto = function(id) {
  const g = globalGastos.find(x => x.id === id); if(!g) return;
  document.getElementById("gastoId").value = g.id; document.getElementById("gastoDescripcion").value = g.descripcion;
  document.getElementById("gastoMonto").value = g.monto; document.getElementById("gastoMedio").value = g.medioPago || "EFECTIVO";
  document.getElementById("gastoFecha").value = g.fecha;
  const esFijo = !!g.esFijo; document.getElementById("gastoEsFijo").checked = esFijo;
  document.getElementById("camposFijos").style.display = esFijo ? "block" : "none";
  if(esFijo) { document.getElementById("gastoVencimiento").value = g.fechaVencimiento||""; document.getElementById("gastoPagado").checked = !!g.pagado; }
  if(g.categoriaId) document.getElementById("gastoCategoria").value = g.categoriaId;
  document.querySelector("#modalGasto h3").textContent = "Editar Gasto";
  document.getElementById("modalGasto").style.display = 'flex';
};
window.abrirModalEditarIngreso = function(id) {
  const i = globalIngresos.find(x => x.id === id); if(!i) return;
  document.getElementById("ingresoId").value = i.id; document.getElementById("ingresoDescripcion").value = i.descripcion;
  document.getElementById("ingresoMonto").value = i.monto; document.getElementById("ingresoMedio").value = i.medioPago || "EFECTIVO";
  document.getElementById("ingresoFecha").value = i.fecha;
  if(i.categoriaId) document.getElementById("ingresoCategoria").value = i.categoriaId;
  document.querySelector("#modalIngreso h3").textContent = "Editar Ingreso";
  document.getElementById("modalIngreso").style.display = 'flex';
};

window.eliminarGasto = async function(id) { if(confirm("¿Eliminar gasto?")) { await fetch(`${API}/gastos/${id}`, {method:"DELETE",headers:authHeaders()}); await refreshAll(); }};
window.eliminarIngreso = async function(id) { if(confirm("¿Eliminar ingreso?")) { await fetch(`${API}/ingresos/${id}`, {method:"DELETE",headers:authHeaders()}); await refreshAll(); }};
window.eliminarCategoria = async function(id) { if(confirm("¿Borrar categoría?")) { const res=await fetch(`${API}/categorias/${id}`, {method:"DELETE",headers:authHeaders()}); if(res.ok) await refreshAll(); else alert("Error al borrar."); }};

// --- FAB BOTÓN FLOTANTE ---
const fabMain = document.getElementById("fabMain");
const fabOptions = document.getElementById("fabOptions");
if(fabMain) {
    fabMain.onclick = () => fabOptions.classList.toggle("show");
}

document.getElementById("btnFabGasto").onclick = () => {
    document.getElementById("formGasto").reset(); document.getElementById("gastoId").value = "";
    document.getElementById("gastoEsFijo").checked = false; document.getElementById("camposFijos").style.display = "none";
    document.querySelector("#modalGasto h3").textContent = "Agregar Gasto";
    const mes = document.getElementById("filtroFechaMes").value; if(mes) document.getElementById("gastoFecha").value = mes+"-01";
    document.getElementById("modalGasto").style.display = 'flex'; fabOptions.classList.remove("show");
};

document.getElementById("btnFabIngreso").onclick = () => {
    document.getElementById("formIngreso").reset(); document.getElementById("ingresoId").value = "";
    document.querySelector("#modalIngreso h3").textContent = "Agregar Ingreso";
    const mes = document.getElementById("filtroFechaMes").value; if(mes) document.getElementById("ingresoFecha").value = mes+"-01";
    document.getElementById("modalIngreso").style.display = 'flex'; fabOptions.classList.remove("show");
};

// --- GESTIÓN CATEGORIAS ---
const btnGestionarCat = document.getElementById("btnGestionarCategorias");
if(btnGestionarCat) btnGestionarCat.onclick = () => { refreshAll(); document.getElementById("modalCategorias").style.display = 'flex'; };
const btnCrearCat = document.getElementById("btnCrearCategoria");
if(btnCrearCat) btnCrearCat.onclick = async () => {
    const nom = document.getElementById("categoriaNombre").value.trim();
    if(!nom) return alert("Escribe un nombre.");
    try {
        const res = await fetch(`${API}/categorias`, {method:"POST",headers:authHeaders(),body:JSON.stringify({nombre:nom,usuarioId:user.id})});
        if(res.ok) { document.getElementById("categoriaNombre").value=""; await fetchCategorias(); } else alert("Error o duplicado.");
    } catch(e) { console.error(e); }
};

// --- NAVEGACIÓN ---
document.querySelectorAll(".sidebar li[data-section]").forEach(li => {
    const sec = li.getAttribute("data-section");
    li.onclick = async () => {
        document.querySelectorAll(".sidebar li").forEach(n => n.classList.remove("active")); li.classList.add("active");
        document.querySelectorAll(".page").forEach(p => p.classList.remove("visible"));
        if(sec === "proyeccion") {
            await refreshAll();
            const mesActual = document.getElementById("filtroFechaMes").value;
            const ing = globalIngresos.filter(i=>i.fecha.startsWith(mesActual)).reduce((s,x)=>s+Number(x.monto),0);
            const clean = (t) => Number(t.replace(/[^0-9,-]+/g,"").replace(",","."));
            const fijos = clean(document.getElementById("totalFijos").textContent);
            const variables = clean(document.getElementById("totalVariables").textContent);
            
            const tbody = document.getElementById("tablaProyeccionBody");
            if(ing===0) tbody.innerHTML = `<tr><td colspan="5" style="text-align:center;color:#ffce56;">⚠️ Carga un Ingreso primero.</td></tr>`;
            else {
                tbody.innerHTML = `
                    <tr><td>Gastos FIJOS</td><td>50%</td><td>${formatoMoneda(ing*0.5)}</td><td style="color:${fijos>ing*0.5?'#ff6384':'#2ac9bb'}">${formatoMoneda(fijos)}</td><td>${fijos>ing*0.5?'⚠️ Te pasaste':'✅ Bien'}</td></tr>
                    <tr><td>Gastos VARIABLES</td><td>30%</td><td>${formatoMoneda(ing*0.3)}</td><td style="color:${variables>ing*0.3?'#ff6384':'#2ac9bb'}">${formatoMoneda(variables)}</td><td>${variables>ing*0.3?'⚠️ Controlar':'✅ Bien'}</td></tr>
                    <tr style="background:#333;"><td>AHORRO</td><td>20%</td><td>${formatoMoneda(ing*0.2)}</td><td>-</td><td>Meta</td></tr>
                `;
            }
            document.getElementById("resumenBNA").textContent = document.getElementById("saldoBNA").textContent;
            document.getElementById("resumenMP").textContent = document.getElementById("saldoMP").textContent;
            document.getElementById("resumenEfectivo").textContent = document.getElementById("saldoEfectivo").textContent;
            document.getElementById("modalProyeccion").style.display='flex';
            document.getElementById("inicio").classList.add("visible"); document.querySelector('[data-section="inicio"]').classList.add("active"); li.classList.remove("active");
        } else {
            const target = document.getElementById(sec);
            if(target) { target.classList.add("visible"); const c=document.querySelector('.content'); if(c) c.scrollTo({top:0,behavior:'smooth'}); }
            await refreshAll();
        }
    };
});

document.getElementById("formGasto").onsubmit = async (e) => { e.preventDefault(); /* ...Lógica submit igual que antes... */ 
  // (Resumida para no repetir tanto, la lógica de submit ya estaba bien en la versión anterior y se mantiene intacta si copiaste el bloque anterior)
  // ...Simplemente asegúrate de que al guardar llame a refreshAll() y cierre el modal...
  const id=document.getElementById("gastoId").value;
  const esFijo=document.getElementById("gastoEsFijo").checked;
  const body={descripcion:document.getElementById("gastoDescripcion").value,monto:document.getElementById("gastoMonto").value,medioPago:document.getElementById("gastoMedio").value,fecha:document.getElementById("gastoFecha").value,esFijo:esFijo,fechaVencimiento:esFijo?document.getElementById("gastoVencimiento").value:null,pagado:esFijo?document.getElementById("gastoPagado").checked:false,usuarioId:user.id,categoriaId:document.getElementById("gastoCategoria").value||null};
  await fetch(`${API}/gastos`+(id?`/${id}`:""),{method:id?"PUT":"POST",headers:authHeaders(),body:JSON.stringify(body)});
  document.getElementById("modalGasto").style.display="none"; document.getElementById("formGasto").reset(); await refreshAll();
};

document.getElementById("formIngreso").onsubmit = async (e) => { e.preventDefault(); 
  const id=document.getElementById("ingresoId").value;
  const body={descripcion:document.getElementById("ingresoDescripcion").value,monto:document.getElementById("ingresoMonto").value,medioPago:document.getElementById("ingresoMedio").value,fecha:document.getElementById("ingresoFecha").value,usuarioId:user.id,categoriaId:document.getElementById("ingresoCategoria").value||null};
  await fetch(`${API}/ingresos`+(id?`/${id}`:""),{method:id?"PUT":"POST",headers:authHeaders(),body:JSON.stringify(body)});
  document.getElementById("modalIngreso").style.display="none"; document.getElementById("formIngreso").reset(); await refreshAll();
};

document.querySelectorAll(".close").forEach(el => el.onclick = () => el.closest(".modal").style.display = 'none');
document.getElementById("logoutBtn").onclick = () => { localStorage.removeItem("token"); window.location.href="login.html"; };

(async function init() { await fetchUserInfo(); cargarSelectorFechas(); await refreshAll(); })();