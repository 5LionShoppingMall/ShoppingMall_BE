package com.ll.lion.product.repository;

import com.ll.lion.community.post.entity.Post;
import com.ll.lion.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> findAllByKeyword(Pageable pageable, String keyword);
}
