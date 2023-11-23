package com.ll.lion.product.service;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.repository.CartRepository;
import com.ll.lion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
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
    }
}
