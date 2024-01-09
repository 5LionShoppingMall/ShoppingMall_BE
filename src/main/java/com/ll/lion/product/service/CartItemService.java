package com.ll.lion.product.service;

import com.ll.lion.product.dto.CartItemDto;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItemDto addCartItem(CartItemDto dto){
        CartItem item = cartItemRepository.save(CartItemDto.dtoToEntity(dto));
        return new CartItemDto(item);
    }

    @Transactional
    public void deleteItem(CartItemDto item) {
        CartItem cartItem = cartItemRepository.findById(item.getId()).orElseThrow(RuntimeException::new);
        cartItemRepository.delete(cartItem);
    }
}
