export function showAlert(message, type) {
    const alertDialog = document.getElementById("alert-dialog")
    const icon = document.createElement("i")
    if (type === 'warning') {
        icon.classList.add("fa-solid" ,"fa-triangle-exclamation", "fa-2x")
    } else if (type === 'confirmation') {
        icon.classList.add("fa-solid", "fa-credit-card", "fa-2x")
    }
    const alertMessage = document.createElement('p')
    const alertButton = document.createElement('button')
    alertButton.id = 'alert-button'
    alertMessage.textContent = message
    alertButton.textContent = 'Accept'
    alertDialog.appendChild(icon)
    alertDialog.appendChild(alertMessage)
    alertDialog.appendChild(alertButton)
    alertDialog.classList.add("alert-dialog")
    alertDialog.showModal()

    alertButton.addEventListener(('click'), () => {
        alertDialog.innerHTML = ''
        alertDialog.close()
        alertDialog.classList.remove("alert-dialog")
    })
    alertDialog.addEventListener('cancel', () => {
        alertDialog.innerHTML = ''
        alertDialog.close()
        alertDialog.classList.remove("alert-dialog")
    })
}