package com.ll.lion.product;

import com.ll.lion.product.entity.Product;
import com.ll.lion.product.repository.ProductRepository;
import com.ll.lion.product.service.ProductService;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
//@Profile("dev")
public class ProductInit implements ApplicationRunner {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (productRepository.count() <= 0) {
            IntStream.rangeClosed(1, 50).forEach(i -> {
                String username = "test" + ((i - 1) % 5 + 1) + "@test.com"; // 5명의 사용자에게 번갈아가며 상품을 할당합니다.
                User user = userRepository.findByEmail(username).orElse(null);

                Product product = Product.builder()
                        .title("상품 test" + i)
                        .price(20000L)
                        .description("test" + i + " 상품 설명 " + i)
                        .seller(user)
                        .build();

                //productService.create(product);
                productRepository.save(product);
            });
        }
    }
}
