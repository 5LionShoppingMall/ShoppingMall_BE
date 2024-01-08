package com.ll.lion.product.service;

import com.ll.lion.product.dto.CartItemDto;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemDto addCartItem(CartItemDto dto){
        CartItem item = cartItemRepository.save(CartItemDto.dtoToEntity(dto));
        return new CartItemDto(item);
    }
}
