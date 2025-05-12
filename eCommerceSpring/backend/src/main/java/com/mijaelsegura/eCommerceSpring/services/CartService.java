package com.mijaelsegura.eCommerceSpring.services;

import com.mijaelsegura.eCommerceSpring.dto.cart.CartDto;
import com.mijaelsegura.eCommerceSpring.dto.cart.CartItemDto;
import com.mijaelsegura.eCommerceSpring.exceptions.ResourceExistingException;
import com.mijaelsegura.eCommerceSpring.exceptions.ResourceNotFoundException;
import com.mijaelsegura.eCommerceSpring.models.*;
import com.mijaelsegura.eCommerceSpring.repositories.ICartRepository;
import com.mijaelsegura.eCommerceSpring.repositories.ISupplementRepository;
import com.mijaelsegura.eCommerceSpring.repositories.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService{

    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final ISupplementRepository supplementRepository;

    public CartService(ICartRepository cartRepository, IUserRepository userRepository, ISupplementRepository supplementRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.supplementRepository = supplementRepository;
    }

    @Override
    public CartDto CreateCart(long dni) {
        Optional<User> userOptional = userRepository.findById(dni);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any user with DNI: " + dni + " to create a cart.");
        }
        User user = userOptional.get();
        if (user.getCart() != null) {
            throw new ResourceExistingException("This user already has a cart created.");
        }
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        cartRepository.save(cart);
        return mapCartToDto(cart);
    }

    @Override
    public CartDto GetCartByUserDni(long dni) {
        Optional<Cart> optionalCart = cartRepository.findByUserDni(dni);
        if (optionalCart.isEmpty()) {
            throw new ResourceNotFoundException("A cart for this user was not created yet.");
        }
        Cart cart = optionalCart.get();
        return mapCartToDto(cart);
    }

    @Override
    public CartDto AddOrModifyCartItem(CartItemDto itemDto) {
        Optional<Cart> optionalCart = cartRepository.findById(itemDto.getCartId());
        if (optionalCart.isEmpty()) {
            throw new ResourceNotFoundException("A cart for this user was not created yet.");
        }
        Optional<Supplement> optionalSupplement = supplementRepository.findById(itemDto.getSupplementId());
        if (optionalSupplement.isEmpty()) {
            throw new ResourceNotFoundException("Not found any supplement with ID " + itemDto.getSupplementId() + " for adding to cart.");
        }
        Cart cart = optionalCart.get();
        Supplement supplement = optionalSupplement.get();
        CartItem item = null;
        for (CartItem itemInCart : cart.getCartItems()) {
            if (itemInCart.getId().getSupplementId() == supplement.getId()) {
                item = itemInCart;
            }
        }
        if (item == null) {
            item = new CartItem();
            IdCartItem itemId = new IdCartItem();
            itemId.setCartId(cart.getId());
            itemId.setSupplementId(supplement.getId());
            item.setId(itemId);
            item.setCart(cart);
            item.setSupplement(supplement);
            item.setQuantity(itemDto.getQuantity());
            List<CartItem> cartItems = cart.getCartItems();
            cartItems.add(item);
        } else {
            item.setQuantity(item.getQuantity() + itemDto.getQuantity());
        }
        cartRepository.save(cart);
        return mapCartToDto(cart);
    }

    @Override
    public CartDto DeleteCartItem(long cartId, long itemId) {
        Optional<Cart> cartToDeleteItemOpt = cartRepository.findById(cartId);
        if (cartToDeleteItemOpt.isEmpty()) {
            throw new ResourceNotFoundException("Not found any cart with ID: " + cartId + " to remove an item");
        }

        Cart cartToDeleteItem = cartToDeleteItemOpt.get();
        List<CartItem> cartItems = cartToDeleteItem.getCartItems();
        CartItem itemToDelete = null;
        for (CartItem item : cartItems) {
            if (item.getId().getSupplementId() == itemId) {
                itemToDelete = item;
            }
        }

        if (itemToDelete == null) {
            throw new ResourceNotFoundException("Not found any supplement with ID: " + itemId + " to remove from cart.");
        }
        cartItems.remove(itemToDelete);
        cartRepository.save(cartToDeleteItem);
        List<CartItemDto> remainingItems = cartItems.stream().map(item -> new CartItemDto(cartId, item.getSupplement().getId(), item.getQuantity())).toList();
        return new CartDto(cartId, cartToDeleteItem.getUser().getDNI(), remainingItems);
    }

    private CartDto mapCartToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserDni(cart.getUser().getDNI());
        cartDto.setCartItems(cart.getCartItems().stream().map(item -> new CartItemDto(item.getId().getCartId(),item.getSupplement().getId(), item.getQuantity())).collect(Collectors.toList()));
        return cartDto;
    }

    public void emptyCartAfterPurchase(long userDni) {
        Optional<Cart> userCartPurchasedOptional = cartRepository.findByUserDni(userDni);
        if (userCartPurchasedOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found cart for user with DNI: " + userDni + " to empty.");
        }
        Cart userCartPurchased = userCartPurchasedOptional.get();
        userCartPurchased.getCartItems().clear();
        cartRepository.save(userCartPurchased);
    }
}
