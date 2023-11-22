package com.ll.lion.product.controller;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    @GetMapping("")
    public List<Product> showCart(){
        Cart cart = cartService.getCart();
        return
    }
}
