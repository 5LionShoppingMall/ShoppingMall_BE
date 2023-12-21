package com.ll.lion.product.service;

import com.ll.lion.common.exception.DataNotFoundException;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> findPageList(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending());

        Page<Product> pageProduct = productRepository.findAllByOrderByCreatedAtDesc(sortedPageable);

        return Optional.of(pageProduct)
                .filter(Slice::hasContent)
                .orElseThrow(() -> new DataNotFoundException("등록된 상품이 없습니다."));
    }

    public List<Product> findListAll() {
        return Optional.of(productRepository.findAll())
                .filter(products -> !products.isEmpty())
                .orElseThrow(() -> new NoSuchElementException(""));
    }

    @Transactional
    public Product create(final Product product) {
        return Optional.of(productRepository.save(product))
                .orElseThrow(() -> new DataNotFoundException("데이터 등록에 실패했습니다."));
    }
}
