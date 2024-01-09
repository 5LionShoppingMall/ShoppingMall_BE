package com.ll.lion.product.service;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.repository.CartRepository;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.service.UserService;
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
    private final UserService userService;

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
    }

    public CartDto getCartByEmail(String email) {
        Optional<Cart> opCart = cartRepository.findByUserEmail(email)
                .or(() -> {
                    // user를 찾기 필요
                    Optional<User> opUser = userService.getUserByEmail(email);
                    if (opUser.isEmpty()) throw new RuntimeException("유저를 찾을 수 없습니다.");

                    // user의 새로운 cart생성
                    return Optional.of(save(opUser.get()));
                });

        if (opCart.isEmpty()) throw new NoSuchElementException("카트가 없습니다.");

        return new CartDto(opCart.get());

    }
}
