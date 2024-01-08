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

/*@Component
@RequiredArgsConstructor
@Profile("dev")
public class ProductInit implements ApplicationRunner {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (productRepository.count() <= 0) {
            IntStream.rangeClosed(1, 5).forEach(i -> {
                String username = "test" + i + "@test.com";
                User user = userRepository.findByEmail(username).orElse(null);

                IntStream.rangeClosed(1, 30).forEach(j -> {
                    Product product = Product.builder()
                            .title("상품 test" + j)
                            .price(20000L)
                            .description("test" + j + " 상품 설명 " + j)
                            .seller(user)
                            .build();

                    //productService.create(product);
                    productRepository.save(product);
                });
            });
        }
    }
}*/
