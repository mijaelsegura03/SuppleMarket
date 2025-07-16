import { showAlert } from '../utils/alert.js'

const SUPPLEMENTS_URL = "http://localhost:8080/supplements"
const CART_URL = "http://localhost:8080/cart"
let cart = []
const cartList = document.getElementById('cart-list')
const saveCartButton = document.getElementById('save-cart-button')

document.addEventListener('DOMContentLoaded', () => {
    const jwt = sessionStorage.getItem('access_token');
    if (!jwt) {
        window.location.href = '../auth/login/login.html';
        return
    }
        
    const supplements = showSupplements(jwt)
    findOrCreateCart(jwt, supplements)
})

async function getSupplements(jwt) {
    const response = await fetch(SUPPLEMENTS_URL, {
        method: "GET",
        headers: {
            "Authorization" : `Bearer ${jwt}`
        }
    })
    const data = await response.json()
    return data.supplements
}

async function showSupplements(jwt) {
    const supplements = await getSupplements(jwt)
    const cardContainer = document.getElementById("catalog-container")
    let row = 2
    let col = 2
    for (const supplement of supplements) {
        const card = document.createElement('div')
        card.classList.add('supplement-card')
        if (col > 4) {
            col = 2
            row = row + 1
        }
        card.style.setProperty('grid-column-start',col)
        card.style.setProperty('grid-row-start',row)

        const cardInfo = document.createElement('div')
        cardInfo.classList.add('supplement-card-info')

        const imageData = await getSupplementImage(jwt,supplement.id)
        const cardImage = document.createElement('img');
        cardImage.src = `data:image/jpeg;base64,${imageData}`; 
        cardImage.alt = 'Supplement Image'
        cardImage.classList.add('supplement-card-image')

        const cardName = document.createElement('p')
        cardName.classList.add('supplement-card-name')
        cardName.textContent = supplement.name

        const cardPrice = document.createElement('p')
        cardPrice.classList.add('supplement-card-price')
        cardPrice.textContent = '$' + supplement.unitaryPrice

        const buyContainer = document.createElement('div')
        buyContainer.classList.add('supplement-card-buyinfo')
        const quantityInput = document.createElement('input')
        quantityInput.type = 'number'
        quantityInput.classList.add('quantity-input')
        quantityInput.id = `quantity-input-${supplement.id}`
        quantityInput.min = 0
        quantityInput.defaultValue = 0

        const cardButton = document.createElement('button')
        cardButton.classList.add('supplement-card-button')
        cardButton.textContent = 'Add'
        cardButton.id = `add-supplement-${supplement.id}`

        card.appendChild(cardImage)
        card.appendChild(cardInfo)
        cardInfo.appendChild(cardName)
        cardInfo.appendChild(cardPrice)
        buyContainer.appendChild(quantityInput)
        buyContainer.appendChild(cardButton)
        cardInfo.appendChild(buyContainer)
        cardContainer.appendChild(card)
        col = col + 1
        addEvent(supplement.id, supplement.name, supplement.unitaryPrice, imageData)
    }
    return supplements
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

function addEvent(supplementId, supplementName, supplementUnitaryPrice, imageData) {
    document.getElementById(`add-supplement-${supplementId}`).addEventListener('click', () => {
        let supplementIndexInCart = cart.findIndex((value) => value.id == supplementId)
        const quantity = parseInt(document.getElementById(`quantity-input-${supplementId}`).value)
        if (quantity > 0 && (cart.length <= 0 || supplementIndexInCart < 0)) {
            const item = {
                id: supplementId,
                name: supplementName,
                price: supplementUnitaryPrice,
                img: imageData,
                quantity
            }
            cart.push(item)
            addItemsToCart()
        } else if (supplementIndexInCart > -1) {
            cart[supplementIndexInCart].quantity = cart[supplementIndexInCart].quantity + parseInt(document.getElementById(`quantity-input-${supplementId}`).value)
            addItemsToCart()
        }
        if (quantity > 0) {
            const cartId = parseInt(sessionStorage.getItem('cart-id'))
            const itemBody = {
                cartId,
                supplementId,
                quantity
            }
            postCartItem(itemBody)
        }  
    })
}

const cartIcon = document.getElementById('show-cart-button')
const closeCartButton = document.getElementById('close-cart-button')
const cartContainer = document.getElementById('cart-container')
cartIcon.addEventListener('click', () => {
    cartContainer.classList.toggle('show-cart')
})

closeCartButton.addEventListener('click', () => {
    cartContainer.classList.toggle('show-cart')
})

async function findOrCreateCart(jwt, supplementsPromise) {
    try {
        const supplements = await supplementsPromise;
        const dni = extractDni(jwt);
        const response = await fetch(`${CART_URL}/${dni}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${jwt}`
            }
        });
        if (response.ok) {
            const data = await response.json();
            const cartId = data.id
            sessionStorage.setItem('cart-id' , cartId)
            const cartItems = data.cartItems;
            for (const item of cartItems) {
                const imageData = await getSupplementImage(jwt, item.supplementId);
                const supplementIndex = supplements.findIndex((value) => value.id == item.supplementId);
                const itemBody = {
                    id: item.supplementId,
                    name: supplements[supplementIndex].name,
                    price: supplements[supplementIndex].unitaryPrice,
                    img: imageData,
                    quantity: item.quantity
                };
                cart.push(itemBody);
            }
            addItemsToCart();
        } else {
            postCart(dni)
        }
    } catch (error) {
        console.error("Error al cargar el carrito:", error);
    }
}

async function postCart(dni) {
    const jwt = sessionStorage.getItem('access_token')

    const response = await fetch(`${CART_URL}`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${jwt}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dni)
        
    })

    if (!response.ok) {
        alert('Failed to create a cart for this user.')
    }
}

function addItemsToCart() {
    cartList.innerHTML = ''
    let totalQuantity = 0
    let cartTotal = document.getElementById('cart-total')
    cartTotal.textContent = 0
    cart.forEach(item => {
        if (item.quantity != 0) {
            totalQuantity += item.quantity
            const cartItem = document.createElement('div')
            cartItem.classList.add('cart-item')
            cartItem.id = item.id
            cartItem.innerHTML = 
            `
                <div class="cart-item-image">
                    <img src=data:image/jpeg;base64,${item.img} alt="Supplement on cart image">
                </div>
                <div class="item-attribute-name">${item.name}</div>
                <div class="item-attribute-price">$${item.price * item.quantity}</div>
                <div class="cart-item-quantity">
                    <span class="minus"><</span>
                    <span class="quantity">${item.quantity}</span>
                    <span class="plus">></span>
                </div>
            `
            cartTotal.textContent = parseInt(cartTotal.textContent) + (item.price * item.quantity)
            cartList.appendChild(cartItem)
        }
    })
    modifyProductsOnCartNumber(totalQuantity, 'total')
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

async function postCartItem(cartBody) {
    const jwt = sessionStorage.getItem('access_token')
    const response = await fetch(`${CART_URL}/cartitem`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization" : `Bearer ${jwt}`
        },
        body: JSON.stringify(cartBody)
    })
    if (!response.ok) {
        alert("Failed to add item to cart.")
    }

}

cartList.addEventListener('click', (event) => {
    const clickPosition = event.target
    if (clickPosition.classList.contains('minus') || clickPosition.classList.contains('plus')) {
        let type = 'minus'
        const productId = clickPosition.parentElement.parentElement.id
        if (clickPosition.classList.contains('plus')) {
            type = 'plus'
        }
        changeQuantity(productId, type)
    }
})

function changeQuantity(productId, type) {
    const cartItem = document.getElementById(productId)
    const cartItemQuantity = cartItem.querySelector('.quantity')
    const quantity = parseInt(cartItemQuantity.textContent)
    const cartItemUnitaryPrice = cartItem.querySelector('.item-attribute-price')
    const integerPrice = parseInt(cartItemUnitaryPrice.textContent.substring(1))
    const item = cart.find(item => item.id === parseInt(productId))
    const cartTotal = document.getElementById('cart-total')
    if (type == 'plus') {
        cartItemQuantity.textContent = quantity + 1
        cartItemUnitaryPrice.textContent = `$${integerPrice + item.price}`
        cartTotal.textContent = parseInt(cartTotal.textContent) + item.price
        modifyProductsOnCartNumber(1, 'modified')
        return
    }

    if (parseInt(cartItemQuantity.textContent) > 0) {
        cartItemQuantity.textContent = quantity - 1
        cartItemUnitaryPrice.textContent = `$${integerPrice - item.price}`
        cartTotal.textContent = parseInt(cartTotal.textContent) - item.price
        modifyProductsOnCartNumber(-1, 'modified')
        if (parseInt(cartItemQuantity.textContent) == 0) {
            deleteCartItem(cartItem)
            const indexToRemove = cart.findIndex((value) => value.id == cartItem.id)
            cart.splice(indexToRemove, 1)
            cartItem.parentElement.removeChild(cartItem)
        }
        return
    }

}

function saveCart() {
    const modifiedCart = document.getElementById('cart-list')
    const modifiedCartItems = Array.from(modifiedCart.children).map(cartItem => {
        const quantity = parseInt(cartItem.querySelector('.quantity').textContent)
        const id = cartItem.id
        return {
            id: parseInt(id),
            quantity: quantity
        }
    })
    console.log(modifiedCartItems)
    console.log(cart)
    for (let i = 0; i < cart.length; i++) {
        if (cart[i].quantity !=  modifiedCartItems[i].quantity) {
            const body = {
                cartId: sessionStorage.getItem('cart-id'),
                supplementId: modifiedCartItems[i].id,
                quantity: modifiedCartItems[i].quantity - cart[i].quantity
            }
            postCartItem(body)
        }
    }
}

const savedCartModal = document.getElementById('cart-saved-dialog')
saveCartButton.addEventListener('click', () => {
    if (cart.length == 0) {
        showAlert('Your cart is empty.', 'warning')
        return
    }
    saveCart()
    savedCartModal.showModal()
})

document.getElementById('cart-saved-dialog-accept').addEventListener('click', () => {
    savedCartModal.close()
})

async function deleteCartItem(cartItem) {
    const jwt = sessionStorage.getItem('access_token')
    const cartId = sessionStorage.getItem('cart-id')
    const response = await fetch(`${CART_URL}/${cartId}/cartitem/${cartItem.id}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${jwt}`
        },
    })

    if (response.status != 200) {
        alert("Failed to remove item from cart.")
    }  
}

function modifyProductsOnCartNumber(quantity, type) {
    const productsOnCartNumber = document.getElementById('products-cart-number')
    if (type == 'total') {
        productsOnCartNumber.textContent = quantity
    } else if (type == 'modified') {
        productsOnCartNumber.textContent = parseInt(productsOnCartNumber.textContent) + quantity
    }
}

document.getElementById('confirm-purchase-button').addEventListener('click', () => {
    if (cart.length == 0) {
        showAlert('Your cart is empty.', 'warning')
        return
    }
    saveCart()
    window.location = '../purchase/purchase.html'
})