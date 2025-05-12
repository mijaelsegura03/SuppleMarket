const REGISTER_URL = "http://localhost:8080/auth/register"
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("register-form").addEventListener("submit", (event) => {
        event.preventDefault()
        const dni = document.getElementById("DNI").value
        const name = document.getElementById("name").value
        const lastName = document.getElementById("lastname").value
        const birthDate = document.getElementById("birthdate").value
        const username = document.getElementById("username").value
        const password = document.getElementById("password").value

        const userBody = {
            dni,
            name,
            lastName,
            birthDate,
            username,
            password
        }
        register(userBody)
    })
})

async function register(body) {
    const response = await fetch(REGISTER_URL, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(body)
    })
    if (response.status == 400) {
        const error = await response.text()
        showAlert(error)
        return
    }
    const data = await response.json()
    const token = data.access_token
    sessionStorage.setItem("access_token", token)
    window.location.href = "../../../home/home.html"
}

function showAlert(text) {
    alert(text)
}

