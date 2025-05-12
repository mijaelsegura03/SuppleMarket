const USER_URL = "http://localhost:8080/users"

document.addEventListener('DOMContentLoaded', () => {
    const jwt = sessionStorage.getItem('access_token');
    if (!jwt) {
        window.location.href = '../auth/login/login.html';
    }

    getData(jwt)
})

async function getData (jwt) {
    const dni = extractDni(jwt)
    const response = await fetch(`${USER_URL}/${dni}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${jwt}`
        },
    });
    const data = await response.json()
    const user = data.user
    const fields = [
        { key: "dni", label: "dni" },
        { key: "name", label: "name" },
        { key: "lastName", label: "lastname" },
        { key: "birthDate", label: "birthdate" },
        { key: "username", label: "username" }
      ];
      
      fields.forEach(field => {
        const item = document.getElementById(`${field.label}-item`);
        const text = document.createElement("h2");
        text.textContent = user[field.key];
        text.id = `${field.label}-text`;
        text.classList.add("userinfo-item-description");
        item.appendChild(text);
      });

      const userRole = extractRole(jwt)
      if (userRole == "ROLE_ADMIN") {
        const mainContainer = document.getElementById("main-container")
        const getAllUsersButton = document.createElement("button")
        getAllUsersButton.textContent = "Admin Panel"
        getAllUsersButton.classList.add("button")
        mainContainer.appendChild(getAllUsersButton)
        getAllUsersButton.addEventListener("click", () => {
            window.location.href = "./adminpanel.html"
        })
        
      }
}

function extractRole(jwt) {
    try {
        const base64Url = jwt.split(".")[1]; // Extrae el payload
        const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/"); // Convierte a Base64 estándar
        const jsonString = atob(base64); // Decodifica Base64 a texto plano
  
        const payload = JSON.parse(jsonString)
        return payload.ROLE || null
      } catch (error) {
        console.error("Error al extraer el atributo:", error);
        return null;
      }
}

function extractDni(jwt) {
    try {
        const base64Url = jwt.split(".")[1]; // Extrae el payload
        const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/"); // Convierte a Base64 estándar
        const jsonString = atob(base64); // Decodifica Base64 a texto plano
  
        const payload = JSON.parse(jsonString)
        return payload.sub || null
    } catch (error) {
        console.error("Error al extraer el atributo:", error);
        return null;
    }
}

document.getElementById("edit-button").addEventListener("click", () => {
    document.getElementById("edit-dialog").showModal()
    document.getElementById("edit-name").value = document.getElementById("name-text").textContent
    document.getElementById("edit-lastname").value = document.getElementById("lastname-text").textContent
    document.getElementById("edit-birthdate").value = document.getElementById("birthdate-text").textContent
    document.getElementById("edit-username").value = document.getElementById("username-text").textContent
    
    document.getElementById("edit-dialog-form").addEventListener("submit", (event) => {
        event.preventDefault()
        const nameNotChanged = document.getElementById("name-text").textContent
        const lastnameNotChanged = document.getElementById("lastname-text").textContent
        const birthdateNotChanged = document.getElementById("birthdate-text").textContent
        const usernameNotChanged = document.getElementById("username-text").textContent
        const name = document.getElementById("edit-name").value
        const lastname = document.getElementById("edit-lastname").value
        const birthdate = document.getElementById("edit-birthdate").value
        const username = document.getElementById("edit-username").value
            
        if (name == nameNotChanged && lastname == lastnameNotChanged && birthdate == birthdateNotChanged && username == usernameNotChanged) {
            document.getElementById("edit-dialog").close()
            return
        }
        const body = {
            name: name,
            lastName: lastname,
            birthDate: birthdate,
            username: username
        }

        putData(body)
    })
})

async function putData(body) {
    const jwt = sessionStorage.getItem('access_token')
    const dni = extractDni(jwt)
    const response = await fetch(`${USER_URL}/${dni}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        },
        body: JSON.stringify(body)
    })
    if (response.ok) {
        window.location.href = '../user/userinfo.html'
    }
}

document.getElementById("delete-button").addEventListener("click", () => {
    document.getElementById("delete-dialog").showModal()
    document.getElementById("delete-button-yes").addEventListener("click", () => {
        const jwt = sessionStorage.getItem("access_token")
        deleteData(jwt)
    })
    document.getElementById("delete-button-no").addEventListener("click", () => {
        document.getElementById("delete-dialog").close()
    })
})

async function deleteData(jwt) {
    const dni = extractDni(jwt)
    const response = await fetch(`${USER_URL}/${dni}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        }
    })
    if (response.ok) {
        sessionStorage.removeItem("access_token")
        window.location.href = '../home/home.html'
    }
}

document.getElementById("logout-button").addEventListener("click", () => {
    sessionStorage.removeItem("access_token")
    window.location.href = '../home/home.html'
})

