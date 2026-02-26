document.getElementById("formRegistro").addEventListener("submit", (e) => {
  e.preventDefault(); // evita que la página recargue

  const nombre = document.getElementById("nombre").value.trim();
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();

  fetch("http://localhost:8080/api/usuarios/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre, email, password }),
  })
    .then(res => res.ok ? res.json() : Promise.reject("Error al registrar"))
    .then(() => {
      alert("Cuenta creada correctamente");
      window.location.href = "login.html"; // 👉 te envía al login
    })
    .catch(err => alert(err));
});

