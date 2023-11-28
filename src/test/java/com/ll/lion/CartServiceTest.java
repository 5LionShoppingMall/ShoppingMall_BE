package com.ll.lion;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.service.CartService;
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
    @DisplayName("user만들고 cart만들고 cartitem만들고")
    public void t3(){
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

        assertThat(cartService.getCart(cartDto).get().getUser())
                .isEqualTo(user);
        assertThat(cartService.getCart(cartDto).get().getCartItems())
                .isEqualTo(null);

        cartDto.setCartItemList(cartService.getCartItems(cartDto));
        assertThat(cartDto.getCartItemList()).isNull();
    }
}
