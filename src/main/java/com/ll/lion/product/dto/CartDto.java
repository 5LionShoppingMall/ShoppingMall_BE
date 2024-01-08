package com.ll.lion.product.dto;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartDto {

    private long id;

    private List<CartItem> cartItems;

    private User cartOwner;

    public CartDto(Cart cart){
        this.id = cart.getId();
        this.cartItems = cart.getCartItems();
        this.cartOwner = cart.getUser();

    }

    public static Cart dtoToEntity(CartDto dto){
        return Cart.builder()
                .id(dto.getId())
                .user(dto.getCartOwner())
                .cartItems(dto.getCartItems())
                .build();
    }
}
