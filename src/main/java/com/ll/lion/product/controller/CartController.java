package com.ll.lion.product.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.service.CartItemService;
import com.ll.lion.product.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/{email}")
    public ResponseEntity<?> showCart(@PathVariable("email") String email) {

        try {
            CartDto cartDto1 = cartService.getCartByEmail(email);
            List<CartItem> list = cartItemService.getCartItems(cartDto1);
            ResponseDto<CartItem> responseDto = new ResponseDto<CartItem>(
                    HttpStatus.OK.value(),
                    "카트 및 카트 아이템 불러오기 성공",
                    null, list, null);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            ResponseDto<CartItem> responseDto = ResponseDto.<CartItem>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}
