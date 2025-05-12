package com.mijaelsegura.eCommerceSpring.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private long id;
    private long userDni;
    private List<CartItemDto> cartItems;
}
