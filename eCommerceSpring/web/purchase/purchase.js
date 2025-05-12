const CART_URL = "http://localhost:8080/cart"
const SUPPLEMENTS_URL = "http://localhost:8080/supplements"
const PURCHASE_URL = "http://localhost:8080/purchases"
const cart = []
document.addEventListener('DOMContentLoaded', () => {
    const jwt = sessionStorage.getItem('access_token');
    if (!jwt) {
        window.location.href = '../auth/login/login.html';
    }
    findCart(jwt)
})

async function findCart(jwt) {
    const userDni = extractDni(jwt)
    const response = await fetch(`${CART_URL}/${userDni}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${jwt}`
        }
    })

    if (!response.ok) {
        alert("Error fetching cart.")
    }

    const data = await response.json()
    const cartItems = data.cartItems
    cartItems.forEach(item => {
        cart.push(item)
    })
    console.log(cart)
    showCart(jwt)
}

function extractDni(jwt) {
    try {
        const base64Url = jwt.split(".")[1]; // Extrae el payload
        const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/"); // Convierte a Base64 estándar
        const jsonString = atob(base64); // Decodifica Base64 a texto plano
  
        const payload = JSON.parse(jsonString)
        return payload.sub || null
    } catch (error) {
        console.error("Error extracting attribute:", error);
        return null;
    }
}

async function showCart(jwt) {
    const cartContainer = document.getElementById('cart-container')
    const totalAmount = document.getElementById('purchase-total-amount')
    let integerTotalAmount = 0
    for (const cartItem of cart) {
        const itemContainer = document.createElement('div')
        itemContainer.classList.add('item')

        const itemImageContainer = document.createElement('div')
        itemImageContainer.classList.add('item-image-container')
        const itemImage = document.createElement('img')
        const itemImageData = await getSupplementImage(jwt, cartItem.supplementId)
        itemImage.classList.add('item-image')
        itemImage.src = `data:image/jpeg;base64,${itemImageData}`;

        const itemInfoContainer = document.createElement('div')
        itemInfoContainer.classList.add('item-info')
        const itemName = document.createElement('p')
        const itemQuantity = document.createElement('p')
        const itemPrice = document.createElement('p')

        const itemDB = await getSupplement(jwt, cartItem.supplementId)
        itemName.textContent = itemDB.name
        itemQuantity.textContent = cartItem.quantity
        itemPrice.textContent = `$${cartItem.quantity * itemDB.unitaryPrice}`
        integerTotalAmount += cartItem.quantity * itemDB.unitaryPrice

        itemImageContainer.appendChild(itemImage)
        itemContainer.appendChild(itemImageContainer)
        itemContainer.appendChild(itemInfoContainer)
        itemInfoContainer.appendChild(itemName)
        itemInfoContainer.appendChild(itemQuantity)
        itemInfoContainer.appendChild(itemPrice)

        cartContainer.appendChild(itemContainer)
    }

    totalAmount.textContent = `$${integerTotalAmount}`
}

async function getSupplementImage(jwt, id) {
    const response = await fetch(`${SUPPLEMENTS_URL}/${id}/image`, {
        method: "GET",
        headers: {
            "Authorization" : `Bearer ${jwt}`
        }
    })
    const data = await response.json()
    return data.imageData
}

async function getSupplement(jwt, id) {
    const response = await fetch(`${SUPPLEMENTS_URL}/${id}`, {
        method: "GET",
        headers: {
            "Authorization" : `Bearer ${jwt}`
        }
    })
    const data = await response.json()
    return data.supplement
}

document.getElementById('make-purchase-button').addEventListener('click', () => {
    postPurchase()
})

async function postPurchase() {
    const jwt = sessionStorage.getItem('access_token')
    const purchaseBody = makePurchaseBody(jwt)
    const response = await fetch(`${PURCHASE_URL}`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${jwt}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(purchaseBody)
    })
    if (!response.ok) {
        alert("Failed to make purchase. Try again")
    } else {
        alert("Purchase made successfully!")
        window.location.href = '../home/home.html'
    } 
}

function makePurchaseBody(jwt) {
    const userDni = extractDni(jwt)
    const totalPrice = parseInt(document.getElementById('purchase-total-amount').textContent.substring(1))
    const purchaseDate = new Date()
    const purchaseDetails = []
    cart.forEach(item => purchaseDetails.push({
            supplementId: item.supplementId, 
            quantity: item.quantity
        }
    ))

    return {
        totalPrice,
        purchaseDate,
        userDNI: userDni,
        purchaseDetails
    }
}


