package com.ll.lion.product.service;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    public Cart getCart() {
        return cartRepository
    }
}
