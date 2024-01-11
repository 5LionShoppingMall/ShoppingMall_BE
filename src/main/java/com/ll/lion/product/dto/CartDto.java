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

    public Cart toEntity(){
        return Cart.builder()
                .id(this.id)
                .user(this.cartOwner)
                .cartItems(this.cartItems)
                .build();
    }
}
