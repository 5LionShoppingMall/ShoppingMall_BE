package com.ll.lion;

import com.ll.lion.product.dto.CartDto;
import com.ll.lion.product.dto.CartItemDto;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.service.CartItemService;
import com.ll.lion.product.service.CartService;
import com.ll.lion.product.service.ProductService;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
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

    @BeforeEach
    public void init() {
        User seller = userService.getUserByEmail("test1@test.com").get();
        User buyer = userService.getUserByEmail("test2@test.com").get();

        //상품등록
        IntStream.range(1, 4).mapToObj(num -> {
            Product pro = Product.builder()
                    .title("상품" + num)
                    .price(num * 1000L)
                    .seller(seller)
                    .build();
            List<MultipartFile> files = new ArrayList<>();
            return productService.create(pro, files, seller);
        })
                .forEach(pro -> System.out.println(pro.getId()));
    }

    @Test
    @DisplayName("테스트 시작")
    public void t1() {
        System.out.println("ActiveProfiles('test')를 사용하여 테스트 활성화 완료");
    }

    @Test
    @DisplayName("카트 만들기")
    public void t2() {
        String email = "test1@test.com";
        User user = userService.getUserByEmail(email).get();

        CartDto cart = cartService.getCartByEmail(email);

        assertThat(cart.getCartOwner())
                .as("카트 주인 확인")
                .isEqualTo(user);
    }

    @Test
    @DisplayName("상품 등록 확인")
    public void t3() {
        Product pro = productService.findProduct(7L);

        assertThat(pro.getTitle()).as("상품 등록 확인").isIn("상품1");
    }

    @Test
    @DisplayName("cartItem 만들기")
    public void t4() {
        String email = "test2@test.com";

        CartItemDto dto1 = cartItemService.addCartItem(email ,10L,2);

        System.out.println(dto1.getId());
        System.out.println(dto1.getCart());

        assertThat(dto1.getId()).as("아이템 생성 확인")
                .isEqualTo(1L);
        assertThat(dto1.getProduct().getPrice()).as("아이템 가격확인")
                .isEqualTo(1000L);

        CartDto cartDto = cartService.getCartByEmail(email);
        assertThat(cartDto.getCartItems().size()).as("카트 아이템종류 확인")
                .isEqualTo(1);
    }

    @Test
    @DisplayName("cartItem제거")
    public void t5() {
        String email = "test2@test.com";
        Optional<User> buyer = userService.getUserByEmail(email);
        CartDto cart = cartService.getCartByEmail(email);

        CartItemDto item = cartItemService.addCartItem(email, 13L, 1);

        List<CartItem> items1 = cartItemService.getCartItems(cart);
//        long id = items1.get(0).getId();
//        System.out.println(id);

        assertThat(items1.size()).as("cart 상품 확인").isEqualTo(1);

        CartItemDto cartItemDto = new CartItemDto(items1.get(0));
        cartItemService.deleteItem(cartItemDto.getId());
        System.out.printf("delete item id : %d", 1);

        List<CartItem> items2 = cartItemService.getCartItems(cart);
        assertThat(items2.size()).as("삭제여부 확인 size로 체크").isEqualTo(0);
    }

    @Test
    @DisplayName("cartitem 수정")
    public void t6() {
        String email = "test2@test.com";

        CartItemDto item = cartItemService.addCartItem(email, 16L, 1);
        CartDto cart = cartService.getCartByEmail(email);
        List<CartItem> items1 = cartItemService.getCartItems(cart);

        assertThat(items1.size()).as("cart 상품 확인").isEqualTo(1);

        cartItemService.modifyItem(item.getId(), 3);

        assertThat(cart.getCartItems().get(0).getQuantity()).as("변경된 수량 확인").isEqualTo(3);
    }
}


//    @Test
//    @DisplayName("user만들고 cart만들고 cartitem만들고")
//    public void t7(){
//        UserRegisterDto userRegisterDto = new UserRegisterDto();
//        userRegisterDto.setEmail("tempouser1@user.com");
//        userRegisterDto.setPassword("1234");
//        userRegisterDto.setPhoneNumber("010-0000-0000");
//        userRegisterDto.setAddress("한국");
//
//        User user;
//        user = userService.register(userRegisterDto);
//
//        assertThat(user.getEmail()).isEqualTo("tempouser1@user.com");
//
//        CartDto cartDto = new CartDto();
//        cartDto.setCartOwner(user);
//
//        assertThat(cartService.getCart(cartDto).get().getUser())
//                .isEqualTo(user);
//        assertThat(cartService.getCart(cartDto).get().getCartItems().isEmpty())
//                .isEqualTo(true);
//
//        cartDto.setCartItemList(cartService.getCartItems(cartDto));
//        assertThat(cartDto.getCartItemList()).isNull();
//    }
//}
