package com.ll.lion.product.service;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.repository.CartRepository;
import com.ll.lion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Optional<Cart> getCart(CartDto cartDto) {
        final User user = cartDto.getCartOwner();
        return cartRepository.findByUser(user)
                .or(() -> Optional.of(save(user)));
    }

    private Cart save(final User user) {
        Cart cart = Cart.builder()
                .user(user)
                .cartItems(new ArrayList<>())
                .build();
        return cartRepository.save(cart);
    }

    public List<CartItem> getCartItems(CartDto cartDto) {
        return cartRepository.findByUser(cartDto.getCartOwner())
                .filter(cart -> !cart.getCartItems().isEmpty())
                .map(Cart::getCartItems)
                .orElseThrow(() -> new NoSuchElementException("No elements found in the cart"));
    }
}
