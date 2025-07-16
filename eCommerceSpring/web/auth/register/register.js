import {showAlert} from "../../utils/alert.js"
const REGISTER_URL = "http://localhost:8080/auth/register"
const REGISTER_ADMIN_URL = "http://localhost:8080/auth/register/admin"

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
        const checkbox = document.getElementById("user-role")
        const role = checkbox.checked ? "admin" : "user"

        register(userBody, role)
    })
})

async function register(body, role) {
    const response = await fetch(role == 'admin'? REGISTER_ADMIN_URL : REGISTER_URL, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(body)
    })
    if (response.status == 400) {
        const error = await response.text()
        showAlert(error, 'warning')
        return
    }
    const data = await response.json()
    const token = data.access_token
    sessionStorage.setItem("access_token", token)
    window.location.href = "../../home/home.html"
}

