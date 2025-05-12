const USERS_URL = "http://localhost:8080/users";
const SUPPLEMENTS_URL = "http://localhost:8080/supplements";

document.addEventListener("DOMContentLoaded", () => {
    const jwt = sessionStorage.getItem("access_token")
    const userRole = extractRole(jwt)

    if (!jwt) {
        window.location.href = "../auth/login/login.html"
        return
    }
    if (userRole != "ROLE_ADMIN") {
        window.location.href = "../home/home.html"
        return
    }

    document.getElementById("users-button").addEventListener("click", () => {
        getUsers(jwt)
    })

    document.getElementById("supplements-button").addEventListener("click", () => {
        getSupplements(jwt)
    })
});

async function getUsers(jwt) {
    const response = await fetch(USERS_URL, {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        }
    })
    const data = await response.json()
    const users = data.users
    const existingAddContainer = document.getElementById("add-supplement-container")
    if (existingAddContainer) {
        existingAddContainer.remove()
    }
    const table = document.getElementById("main-table")
    table.innerHTML = ""
    const row = table.insertRow()
    row.innerHTML = `<th>DNI</th><th>Name</th><th>Lastname</th><th>Birthdate</th><th>Username</th>`
    users.forEach(user => {
        const row = table.insertRow()
        row.innerHTML = `
        <td>${user.dni}</td>
        <td>${user.name}</td>
        <td>${user.lastName}</td>
        <td>${user.birthDate}</td>
        <td>${user.username}</td>
        <button id="delete-user-${user.dni}">
            <i class="fa-solid fa-trash"></i>
        </button>
        <button id="edit-user-${user.dni}">
            <i class="fa-solid fa-pen-to-square"></i>
        </button>
        <dialog id="edit-dialog-${user.dni}">
        </dialog>
        `
        
        document.getElementById(`delete-user-${user.dni}`).addEventListener("click", () => {
            deleteMethod(jwt, user.dni, USERS_URL)
        })

        document.getElementById(`edit-user-${user.dni}`).addEventListener("click", () => {
            const dialog = document.getElementById(`edit-dialog-${user.dni}`)
            dialog.innerHTML = `
            <form method="dialog" id="edit-user-form-${user.dni}">
                <div class="description-container-dialog">
                    <h1>Edit User</h1>
                </div>
                <div class = "form-group">
                    <i class="fa-solid fa-user"></i>
                    <input type="text" placeholder="Name" class="form-group-input" id="edit-user-name-${user.dni}" maxlength="15" value="${user.name}" required>
                </div>
                <div class = "form-group">
                    <i class="fa-solid fa-user"></i>
                    <input type="text" placeholder="Lastname" class="form-group-input" id="edit-user-lastname-${user.dni}" maxlength="15" value="${user.lastName}" required>
                </div>
                <div class = "form-group">
                    <i class="fa-solid fa-calendar"></i>
                    <input type="date" placeholder="Birthdate" class="form-group-input birthdate-input" id="edit-user-birthdate-${user.dni}" value="${user.birthDate}" required>
                </div>
                <div class = "form-group">
                    <i class="fa-solid fa-circle-user"></i>
                    <input type="text" placeholder="Username" class="form-group-input" id="edit-user-username-${user.dni}" maxlength="20" value="${user.username}" required>
                </div>
                <div class="dialog-buttons-container">
                    <button class="button" type="submit">Confirm</button>
                    <button class="button" type="button" id="edit-user-cancel-${user.dni}">Cancel</button>
                </div>
            </form>
            `
            dialog.showModal()

            // Agregar event listener al botón de cancelar
            document.getElementById(`edit-user-cancel-${user.dni}`).addEventListener("click", () => {
                dialog.close()
            })

            // Agregar event listener al formulario
            document.getElementById(`edit-user-form-${user.dni}`).addEventListener("submit", (event) => {
                event.preventDefault()
                const editBody = {
                    name: document.getElementById(`edit-user-name-${user.dni}`).value,
                    lastName: document.getElementById(`edit-user-lastname-${user.dni}`).value,
                    birthDate: document.getElementById(`edit-user-birthdate-${user.dni}`).value,
                    username: document.getElementById(`edit-user-username-${user.dni}`).value
                }
                if (editBody.name == user.name && editBody.lastName == user.lastName && editBody.birthDate == user.birthDate && editBody.username == user.username) {
                    dialog.close()
                    return
                }
                editMethod(jwt, user.dni, editBody, USERS_URL)
            })
        })
    })
}

async function getSupplements(jwt) {
    const response = await fetch(SUPPLEMENTS_URL, {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        }
    })
    const data = await response.json()
    const supplements = data.supplements
    const table = document.getElementById("main-table")
    table.innerHTML = ""
    const row = table.insertRow()
    row.innerHTML = `<th>ID</th><th>Name</th><th>Description</th><th>Unitary Price</th><th>Unitary Cost</th>`
    supplements.forEach(supplement => {
        const row = table.insertRow()
        row.innerHTML = `
        <td>${supplement.id}</td>
        <td>${supplement.name}</td>
        <td class="description-cell">${supplement.description}</td>
        <td>${supplement.unitaryPrice}</td>
        <td>${supplement.unitaryCost}</td>
        <button id="delete-supplement-${supplement.id}">
            <i class="fa-solid fa-trash"></i>
        </button>
        <button id="edit-supplement-${supplement.id}">
            <i class="fa-solid fa-pen-to-square"></i>
        </button>
        <dialog id="edit-dialog-${supplement.id}">
        </dialog>
        `

        document.getElementById(`delete-supplement-${supplement.id}`).addEventListener("click", () => {
            deleteMethod(jwt, supplement.id, SUPPLEMENTS_URL)
        })

        document.getElementById(`edit-supplement-${supplement.id}`).addEventListener("click", () => {
            const dialog = document.getElementById(`edit-dialog-${supplement.id}`)
            dialog.innerHTML = `
            <form method="dialog" id="edit-supplement-form-${supplement.id}">
                <div class="description-container-dialog">
                    <h1>Edit Supplement</h1>
                </div>
                <div class = "form-group">
                    <i class="fa-solid fa-jar"></i>
                    <input type="text" placeholder="Name" class="form-group-input" id="edit-supplement-name-${supplement.id}" maxlength="35" value="${supplement.name}" required>
                </div>
                <div class = "form-group">
                    <i class="fa-solid fa-circle-info"></i>
                    <input type="text" placeholder="Description" class="form-group-input" id="edit-supplement-description-${supplement.id}" value="${supplement.description}" required>
                </div>
                <div class = "form-group">
                    <i class="fa-solid fa-dollar-sign"></i>
                    <input type="number" placeholder="Unitary Price" class="form-group-input" id="edit-supplement-unitaryprice-${supplement.id}" value="${supplement.unitaryPrice}" required>
                </div>
                <div class = "form-group">
                    <i class="fa-solid fa-dollar-sign"></i>
                    <input type="number" placeholder="Unitary Cost" class="form-group-input" id="edit-supplement-unitarycost-${supplement.id}" value="${supplement.unitaryCost}" required>
                </div>
                <div class="form-group">
                    <i class="fa-solid fa-image"></i>
                    <input type="file" class="form-group-input" id="edit-supplement-image-${supplement.id}" accept="image/png, image/jpeg">
                </div>
                <div class="dialog-buttons-container">
                    <button class="button" type="submit">Confirm</button>
                    <button class="button" type="button" id="edit-supplement-cancel-${supplement.id}">Cancel</button>
                </div>
            </form>
            `
            dialog.showModal()
            document.getElementById(`edit-supplement-form-${supplement.id}`).addEventListener("submit", (event) => {
                event.preventDefault()
                const editBody = {
                    name: document.getElementById(`edit-supplement-name-${supplement.id}`).value,
                    description: document.getElementById(`edit-supplement-description-${supplement.id}`).value,
                    unitaryPrice: document.getElementById(`edit-supplement-unitaryprice-${supplement.id}`).value,
                    unitaryCost: document.getElementById(`edit-supplement-unitarycost-${supplement.id}`).value,
                }
                const image = document.getElementById(`edit-supplement-image-${supplement.id}`).files[0]
                if (image) {
                    editSupplementImage(jwt, image, supplement.id)
                }
                if (editBody.name == supplement.name && editBody.description == supplement.description && editBody.unitaryPrice == supplement.unitaryPrice && editBody.unitaryCost == supplement.unitaryCost) {
                    dialog.close()
                    return
                }
                editMethod(jwt, supplement.id, editBody, SUPPLEMENTS_URL)
            })

            document.getElementById(`edit-supplement-cancel-${supplement.id}`).addEventListener("click", () => {
                dialog.close()
            })
        })
    })

    const existingAddContainer = document.getElementById("add-supplement-container")
    if (existingAddContainer) {
        existingAddContainer.remove()
    }

    const informationContainer = document.getElementById("information-container")
    const addSupplementContainer = document.createElement("div")
    addSupplementContainer.id = "add-supplement-container"
    const addSupplementButton = document.createElement("button")
    addSupplementContainer.appendChild(addSupplementButton)
    addSupplementButton.classList.add("button")
    addSupplementButton.textContent = "Add Supplement"
    addSupplementButton.id = "add-supplement-button"
    informationContainer.appendChild(addSupplementContainer)

    const handleAddSupplement = () => {
        const dialog = document.getElementById("create-supplement-dialog")
        dialog.showModal()

        const form = document.getElementById("create-supplement-dialog-form")
        const newForm = form.cloneNode(true)
        form.parentNode.replaceChild(newForm, form)

        newForm.addEventListener("submit", (event) => {
            event.preventDefault()
            const createBody = {
                name: document.getElementById("create-supplement-name").value,
                description: document.getElementById("create-supplement-description").value,
                unitaryPrice: document.getElementById("create-supplement-unitaryprice").value,
                unitaryCost: document.getElementById("create-supplement-unitarycost").value,
                image: document.getElementById("create-supplement-image").files[0]
            }
            const formData = new FormData()
            formData.append("supplementDto", new Blob(
                [JSON.stringify(createBody)], 
                { type: "application/json" }
            ));
            formData.append("imageFile", document.getElementById("create-supplement-image").files[0]);
            createMethod(jwt, formData, SUPPLEMENTS_URL)
        })

        document.getElementById("create-supplement-dialog-button-cancel").addEventListener("click", () => {
            dialog.close()
        })
    }

    const newButton = addSupplementButton.cloneNode(true)
    addSupplementButton.parentNode.replaceChild(newButton, addSupplementButton)
    
    newButton.addEventListener("click", handleAddSupplement)
}

async function deleteMethod(jwt, id, URL) {
    const response = await fetch(`${URL}/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        }
    })

    if (response.ok) {
        URL == USERS_URL ? getUsers(jwt) : getSupplements(jwt)
    } else {
        alert("There was an error deleting")
    }
}

async function editMethod(jwt, id, body, URL) {
    const response = await fetch(`${URL}/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        },
        body: JSON.stringify(body)
    })
    if (response.ok) {
        URL == USERS_URL ? getUsers(jwt) : getSupplements(jwt)
    } else {
        alert("There was an error editing.")
    }
}

async function editSupplementImage(jwt, image, id) {
    const formData = new FormData()
    formData.append("imageFile", image);
    const response = await fetch(`${SUPPLEMENTS_URL}/${id}/image`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${jwt}`
        },
        body: formData
    })

    if (response.ok) {
        getSupplements(jwt)
    } else {
        alert("There was an error editing supplement image.")
    }
}

async function createMethod(jwt, body, URL) {
    const response = await fetch(URL, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${jwt}`
        },
        body
    })
    
    if (response.ok) {
        URL == USERS_URL ? getUsers(jwt) : getSupplements(jwt)
    } else {
        alert("There was an error creating.")
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
        console.error("Error extracting attribute:", error);
        return null;
      }
}

