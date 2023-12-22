package com.ll.lion.product.repository;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(Cart cart);
}
