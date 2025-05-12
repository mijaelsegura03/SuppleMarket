package com.mijaelsegura.eCommerceSpring.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDto {
    private long cartId;
    private long supplementId;
    private int quantity;
}
