package com.ll.lion.product.service;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.repository.CartRepository;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.security.JwtTokenUtil;
import com.ll.lion.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

//    미사용
//    public CartDto getCart(CartDto cartDto) {
//        final User user = cartDto.getCartOwner();
//        Optional<Cart> cart = cartRepository.findByUser(user)
//                .or(() -> Optional.of(save(user)));
//        return new CartDto(cart.get());
//    }

    @Transactional
    public Cart createCart(User user) {
        Cart cart = new Cart(user);
        user.createCart(cart);
        return cartRepository.save(cart);
    }

    public List<CartItem> getCartItems(CartDto cartDto) {
        return cartRepository.findByUser(cartDto.getCartOwner())
                .filter(cart -> !cart.getCartItems().isEmpty())
                .map(Cart::getCartItems)
                .orElseThrow(() -> new NoSuchElementException("카트에 아이템이 없습니다."));
    }

    public CartDto getCartByEmail(String email) throws RuntimeException {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저가 없습니다."));

        return new CartDto(createCartIfEmpty(user));
    }

    public Cart createCartIfEmpty(User user){
            // 카트가 안 만들어져 있을 경우 새로 생성
            Optional<Cart> cart = cartRepository.findByUser(user);
            return cart.orElseGet(() -> createCart(user));
    }

    @Transactional
    public void addItem(CartItem item) {
        Cart cart = item.getCart();
        cartRepository.save(cart.addItem(item));
    }

    @Transactional
    public void deleteItem(CartItem item) {
        Cart cart = item.getCart();
        cart.getCartItems().remove(item);
        cartRepository.save(cart);
    }

    //현재 사용자와 email이 일치하는지 확인
    public boolean confirmUser(HttpServletRequest request, String email){
        String refreshToken = jwtTokenUtil.resolveToken(request, "refreshToken");
        String emailInToken = jwtTokenUtil.getEmail(refreshToken);
        return emailInToken.equals(email);
    }
}
