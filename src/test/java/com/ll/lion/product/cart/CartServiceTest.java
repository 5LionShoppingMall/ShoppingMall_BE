package com.ll.lion.product.cart;

import com.ll.lion.Application;
import com.ll.lion.product.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    void saveCart(){
    }
}
