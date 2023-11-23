package com.ll.lion.product.dto;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.user.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class CartDto {

    private Cart cart;

    private List<CartItem> cartItemList;

    private User cartOwner;
}
