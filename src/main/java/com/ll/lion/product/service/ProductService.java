package com.ll.lion.product.service;

import com.ll.lion.common.exception.DataNotFoundException;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findListAll() {
        return Optional.of(productRepository.findAll())
                .filter(products -> !products.isEmpty())
                .orElseThrow(() -> new NoSuchElementException(""));
    }

    public Product create(final Product product) {
        return Optional.of(productRepository.save(product))
                .orElseThrow(() -> new DataNotFoundException("데이터 등록에 실패했습니다."));
    }
}
