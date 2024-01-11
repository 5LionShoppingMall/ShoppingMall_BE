package com.ll.lion.product.service;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.dto.CartItemDto;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.repository.CartItemRepository;
import com.ll.lion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemService {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserService userService;

//    @Transactional
//    public CartItemDto addCartItem(CartItemDto dto){
//        CartItem item = cartItemRepository.save(CartItemDto.dtoToEntity(dto));
//        return new CartItemDto(item);
//    }

    @Transactional
    public CartItemDto addCartItem(String userEmail,
                                   Long productId,
                                   int quantity) throws RuntimeException {
        // 수량입력 오류 처리
        if (quantity < 0 ){
            throw new IllegalArgumentException("잘못된 수량입니다.");
        }

        CartDto cartDto = cartService.getCartByEmail(userEmail);
        Product product = productService.findProduct(productId);
        CartItem item = new CartItem(cartDto.toEntity(), product, quantity);
        cartService.addItem(item);
//        CartItem item = cartItemRepository.save(cartItem); // 불필요
        return new CartItemDto(item);
    }

    @Transactional
    public void deleteItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 입력입니다."));
        cartItemRepository.delete(cartItem); // delete는 ophanremoval 설정해둠.
    }

    public List<CartItem> getCartItems(CartDto cartDto) {
        Cart cart = cartDto.toEntity();
        return cartItemRepository.findAllByCart(cart);
    }

    @Transactional
    public CartItemDto modifyItem(Long cartItemId, int quantity){
        // 수량입력 오류 처리
        if (quantity < 0 ){
            throw new IllegalArgumentException("잘못된 수량입니다.");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(()-> new IllegalArgumentException("상품이 없습니다."));
        cartItem.setQuantity(quantity);
        CartItem item =cartItemRepository.save(cartItem);
        return new CartItemDto(item);
    }
}
