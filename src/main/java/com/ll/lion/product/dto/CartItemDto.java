package com.ll.lion.product.dto;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private Cart cart;
    private Product product;
    private Integer quantity;

    public CartItemDto(CartItem item){
        this.id = item.getId();
        this.cart = item.getCart();
        this.product = item.getProduct();
        this.quantity = item.getQuantity();
    }

    public static CartItem dtoToEntity(CartItemDto dto){
        return CartItem.builder()
                .id(dto.getId())
                .cart(dto.getCart())
                .product(dto.getProduct())
                .quantity(dto.getQuantity())
                .build();
    }
}
