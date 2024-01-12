//package com.ll.lion.product.controller;
//
//import com.ll.lion.common.dto.ResponseDto;
//import com.ll.lion.product.dto.CartItemDto;
//import com.ll.lion.product.service.CartItemService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//
//@Controller
//@RequiredArgsConstructor
//public class CartItemController {
//    private final CartItemService cartItemService;
//
//    public ResponseEntity<?> addCartItem(CartItemDto dto){
//        ResponseDto<CartItemDto> responseDto;
//        try{
//            CartItemDto dto1 = cartItemService.addCartItem(dto);
//            responseDto = ResponseDto.<CartItemDto>builder().objData(dto1).build();
//            return ResponseEntity.ok(responseDto);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//}
