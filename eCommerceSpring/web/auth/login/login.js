import { showAlert } from "../../utils/alert.js";
const LOGIN_URL = "http://localhost:8080/auth/login";

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("login-form").addEventListener("submit", (event) => {
        event.preventDefault();
        const dni = document.getElementById("dni").value;
        const password = document.getElementById("password").value;

        const userBody = {
            dni,
            password
        }
        login(userBody)
    })
})

async function login(body) {
    const response = await fetch(`${LOGIN_URL}`, {
        method: "POST",
        body: JSON.stringify(body),
        headers: {"Content-Type": "application/json"}
    })
    if (response.status == 400 || response.status == 404) {
        const error = await response.text()
        showAlert(error, 'warning')
        return
    }
    const data = await response.json()
    const token = data.access_token
    sessionStorage.setItem("access_token", token);
    history.back()
}
