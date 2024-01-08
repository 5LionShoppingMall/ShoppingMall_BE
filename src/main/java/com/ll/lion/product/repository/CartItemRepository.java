package com.ll.lion.product.repository;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(Cart cart);

}
