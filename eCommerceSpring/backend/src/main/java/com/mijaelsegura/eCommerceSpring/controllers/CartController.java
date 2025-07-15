package com.mijaelsegura.eCommerceSpring.controllers;

import com.mijaelsegura.eCommerceSpring.dto.cart.CartDto;
import com.mijaelsegura.eCommerceSpring.dto.cart.CartItemDto;
import com.mijaelsegura.eCommerceSpring.models.Cart;
import com.mijaelsegura.eCommerceSpring.services.ICartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final ICartService cartService;
    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartDto> PostCart(@RequestBody Long userDni) {
        CartDto cartDto  = cartService.CreateCart(userDni);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PostMapping("/cartitem")
    public ResponseEntity<CartDto> PostCartItem(@RequestBody CartItemDto itemDto) {
        CartDto cartDto = cartService.AddOrModifyCartItem(itemDto);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/cartitem/{itemId}")
    public ResponseEntity<CartDto> DeleteCartItem(@PathVariable long cartId, @PathVariable long itemId) {
        CartDto cartDto = cartService.DeleteCartItem(cartId, itemId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<CartDto> GetUserCart(@PathVariable long dni) {
        CartDto cartDto = cartService.GetCartByUserDni(dni);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
