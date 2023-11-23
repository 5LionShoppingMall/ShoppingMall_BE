package com.ll.lion.product.controller;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.service.CartItemService;
import com.ll.lion.product.service.CartService;
import com.ll.lion.user.entity.User;
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
    public void showCart(){
        List<CartItem> testCart; //테스트용 카트
        CartDto cartDto = new CartDto();
        // 유저 정보를 받아와야함
        cartDto.setCartOwner(new User());
        cartDto.setCart(cartService.getCart(cartDto.getCartOwner()).get());
        testCart = cartItemService.getItemList(cartDto);
        System.out.println(testCart);

    }
}
