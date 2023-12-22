package com.ll.lion.product.service;

import com.ll.lion.common.entity.Image;
import com.ll.lion.common.exception.DataNotFoundException;
import com.ll.lion.common.repository.ImageRepository;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.repository.ProductRepository;
import com.ll.lion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

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
    public Product create(List<Map> maps, final Product product, final User user) {
        List<Image> images = new ArrayList<>();

        for (Map map : maps) {
            Image image = Image.builder()
                    .imageId((String) map.get("public_id"))
                    .name((String) map.get("original_filename"))
                    .url((String) map.get("url"))
                    .size((Long) map.get("bytes"))
                    .product(product)
                    .build();

            images.add(imageRepository.save(image));
        }

        Product finalProduct = product.toBuilder()
                .images(images)
                .seller(user)
                .build();

        return Optional.of(productRepository.save(finalProduct))
                .orElseThrow(() -> new DataNotFoundException("데이터 등록에 실패했습니다."));
    }
}
