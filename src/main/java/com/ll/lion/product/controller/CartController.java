package com.ll.lion.product.controller;

import com.ll.lion.common.dto.ResponseDto;
import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.dto.CartItemDto;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.service.CartItemService;
import com.ll.lion.product.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

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

    @PostMapping("/{email}")
    public ResponseEntity<?> addCartItem(@PathVariable("email") String email,
                                         @RequestParam(value = "proId") Long proId,
                                         @RequestParam(value = "q") int quantity,
                                         HttpServletRequest req){
        try{
            if(!cartService.confirmUser(req, email)){
                throw new RuntimeException("사용자가 일치하지 않습니다.");
            }

            CartItemDto cartItemDto = cartItemService.addCartItem(email, proId, quantity);
            ResponseDto<CartItemDto> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                    "아이템 카트에 추가 완료", null, null,
                    cartItemDto);
            return ResponseEntity.ok(responseDto);
        }catch(Exception e){
            ResponseDto<CartItemDto> responseDto = ResponseDto.<CartItemDto>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<?> modifyCartItem(@PathVariable("email") String email,
                                         @RequestParam(value = "itemId") Long itemId,
                                         @RequestParam(value = "q") int quantity,
                                            HttpServletRequest req){

        try{
            if(!cartService.confirmUser(req, email)){
                throw new RuntimeException("사용자가 일치하지 않습니다.");
            }

            CartItemDto cartItemDto = cartItemService.modifyItem(itemId, quantity);
            ResponseDto<CartItem> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                    "아이템 수정 완료", null, null,
                    cartItemDto.toEntity());
            return ResponseEntity.ok(responseDto);
        }catch(Exception e){
            ResponseDto<CartItem> responseDto = ResponseDto.<CartItem>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("email") String email,
                                            @RequestParam(value = "itemId") Long itemId,
                                            HttpServletRequest req){

        try{
            if(!cartService.confirmUser(req, email)){
                throw new RuntimeException("사용자가 일치하지 않습니다.");
            }

            cartItemService.deleteItem(email, itemId);
            ResponseDto<CartItem> responseDto = new ResponseDto<>(HttpStatus.OK.value(),
                    "아이템 삭제 완료", null, null,
                    null);
            return ResponseEntity.ok(responseDto);
        }catch(Exception e){
            ResponseDto<CartItem> responseDto = ResponseDto.<CartItem>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}
