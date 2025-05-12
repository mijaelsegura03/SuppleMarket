package com.mijaelsegura.eCommerceSpring.services;

import com.mijaelsegura.eCommerceSpring.dto.cart.CartDto;
import com.mijaelsegura.eCommerceSpring.dto.cart.CartItemDto;
import com.mijaelsegura.eCommerceSpring.models.Cart;
import com.mijaelsegura.eCommerceSpring.models.CartItem;

public interface ICartService {
    CartDto CreateCart(long dni);
    CartDto GetCartByUserDni(long dni);
    CartDto AddOrModifyCartItem(CartItemDto itemDto);
    CartDto DeleteCartItem(long itemId, long cartId);
}
