package com.ll.lion.product.service;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private CartItemRepository cartItemRepository;

    public List<CartItem> getItemList(CartDto cartDto) {
        Cart cart = cartDto.getCart();
        return cartItemRepository.findAllByCart(cart);
    }
}
