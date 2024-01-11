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

    @Transactional
    public CartItemDto addCartItem(String userEmail,
                                   Long productId,
                                   int quantity) throws RuntimeException {
        // 수량입력 오류 처리
        if (quantity < 0 ){
            throw new IllegalArgumentException("잘못된 수량입니다.");
        }

        Cart cart = cartService.getCartByEmail(userEmail).toEntity();
        Product product = productService.findProduct(productId);
        CartItem item = cart.addItem(product, quantity);
        return new CartItemDto(item);
    }

    @Transactional
    public void deleteItem(String userEmail,
                           Long cartItemId) {
        Cart cart = cartService.getCartByEmail(userEmail).toEntity();
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 입력입니다."));
        cart.removeItem(cartItem);
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
