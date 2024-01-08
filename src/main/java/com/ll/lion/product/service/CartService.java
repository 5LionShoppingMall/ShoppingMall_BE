package com.ll.lion.product.service;

<<<<<<< HEAD
import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
=======
import com.ll.lion.product.entity.Cart;
>>>>>>> heeyeong
import com.ll.lion.product.repository.CartRepository;
import com.ll.lion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
<<<<<<< HEAD
import java.util.List;
import java.util.NoSuchElementException;
=======
>>>>>>> heeyeong
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
<<<<<<< HEAD

    public CartDto getCart(CartDto cartDto) {
        final User user = cartDto.getCartOwner();
        Optional<Cart> cart = cartRepository.findByUser(user)
                .or(() -> Optional.of(save(user)));
        return new CartDto(cart.get());
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
=======
    public Optional<Cart> getCart(User user) {
        Optional<Cart> opcart = Optional.ofNullable(cartRepository.findByUser(user));
        if (opcart.isEmpty()){
            save();
            return Optional.of(cartRepository.findByUser(user));
        }else return opcart;
    }

    public void save() {
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setUser(new User());
        cartRepository.save(cart);
>>>>>>> heeyeong
    }
}
