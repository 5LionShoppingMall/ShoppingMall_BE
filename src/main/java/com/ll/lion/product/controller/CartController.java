package com.ll.lion.product.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.service.CartItemService;
import com.ll.lion.product.service.CartService;
import com.ll.lion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public ResponseEntity<?> showCart(CartDto cartDto){
        ResponseDto<CartItem> responseDto;
        try{
        List<CartItem> cartItems = cartService.getCartItems(cartDto);
        responseDto = ResponseDto.<CartItem>builder().listData(cartItems).build();
        return ResponseEntity.ok(responseDto);
        } catch (Exception e){
            responseDto = ResponseDto.<CartItem>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

}
