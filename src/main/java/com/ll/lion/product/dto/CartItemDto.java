package com.ll.lion.product.dto;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
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
    private Long cartId;
    private ProductListDto productListDto;
    private Integer quantity;

    public CartItemDto(CartItem item){
        this.id = item.getId();
        this.cartId = item.getCart().getId();
        this.productListDto = ProductListDto.from(item.getProduct());
        this.quantity = item.getQuantity();
    }

    public CartItem toEntity(Cart cart) {
        if (!cart.getId().equals(this.cartId)){
            throw new IllegalArgumentException("잘못된 카트입니다.");
        }
        return CartItem.builder()
                .id(this.id)
                .cart(cart)
                .product(this.productListDto.toEntity())
                .quantity(this.quantity)
                .build();
    }
}
