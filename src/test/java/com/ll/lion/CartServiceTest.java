package com.ll.lion;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.dto.CartItemDto;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.service.CartItemService;
import com.ll.lion.product.service.CartService;
import com.ll.lion.product.service.ProductService;
import com.ll.lion.user.dto.UserInfoDto;
import com.ll.lion.user.dto.UserRegisterDto;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Rollback
@Transactional
public class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("t1")
    public void t1(){
        System.out.println("ActiveProfiles('test')를 사용하여 테스트 활성화 완료");
    }

    @Test
    @DisplayName("user만들기")
    public void t2(){
        UserRegisterDto dto = new UserRegisterDto();
        dto.setEmail("tempouser1@user.com");
        dto.setPassword("1234");
        dto.setPhoneNumber("010-0000-0000");
        dto.setAddress("한국");
        userService.register(dto);
    }

    @Test
    @DisplayName("cart만들기")
    @Rollback(value = false)
    public void t3(){
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("tempouser1@user.com");
        userRegisterDto.setPassword("1234");
        userRegisterDto.setPhoneNumber("010-0000-0000");
        userRegisterDto.setAddress("한국");

        User user;
        user = userService.register(userRegisterDto);

        assertThat(user.getEmail()).isEqualTo("tempouser1@user.com");

        CartDto dto = new CartDto(); // request에서 받은 객체
        CartDto cartDTO = cartService.getCart(dto);

        assertThat(cartDTO.getId()).isEqualTo(1L);
        Product pro = Product.builder()
                .title("상품")
                .price(1000L)
                .seller(user)
                .build();
        pro = productService.create(pro);
        CartItemDto dto1 = CartItemDto.builder()
                .cart(CartDto.dtoToEntity(cartDTO))
                .product(pro)
                .quantity(2)
                .build();

        dto1 = cartItemService.addCartItem(dto1);

        assertThat(dto1.getId()).isEqualTo(1L);
        assertThat(dto1.getProduct().getPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("cartItem제거")
    public void t5(){
        UserInfoDto user1 = userService.getUserByEmailAndMakeDto("tempouser1@user.com");
        CartDto cart = cartService.getCartByEmail(user1.getEmail());
        List<CartItem> items = cart.getCartItems();
        CartItemDto cartItemDto = new CartItemDto(items.get(0));
        cartItemService.deleteItem(cartItemDto);
        System.out.println("complete delete");

    }



    @Test
    @DisplayName("user만들고 cart만들고 cartitem만들고")
    public void t7(){
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("tempouser1@user.com");
        userRegisterDto.setPassword("1234");
        userRegisterDto.setPhoneNumber("010-0000-0000");
        userRegisterDto.setAddress("한국");

        User user;
        user = userService.register(userRegisterDto);

        assertThat(user.getEmail()).isEqualTo("tempouser1@user.com");

        CartDto cartDto = new CartDto();
        cartDto.setCartOwner(user);
//
//        assertThat(cartService.getCart(cartDto).get().getUser())
//                .isEqualTo(user);
//        assertThat(cartService.getCart(cartDto).get().getCartItems().isEmpty())
//                .isEqualTo(true);
//
//        cartDto.setCartItemList(cartService.getCartItems(cartDto));
//        assertThat(cartDto.getCartItemList()).isNull();
    }
}
