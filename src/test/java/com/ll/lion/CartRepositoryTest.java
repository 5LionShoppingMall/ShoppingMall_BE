package com.ll.lion;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(DataSourceConfig.class)
public class CartRepositoryTest {
// 최악이다 뭐해도 안된다..... 외부 데이터베이스에 연결하는 글을 찾아봤지만 안된다.
    // 됐다!!!! 2시간
    @Autowired
    private CartRepository cartRepository;

    @Test
    @DisplayName("테스트 실행 성공 여부")
    void t1(){
        for (int i = 0; i < 3; i++){
        Cart cart = new Cart();
        cartRepository.save(cart);}
        System.out.println(cartRepository.findAll());
        // 결과물 [com.ll.lion.product.entity.Cart@73633230, com.ll.lion.product.entity.Cart@7528089c, com.ll.lion.product.entity.Cart@4a225014]
    }
}
