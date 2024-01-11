package com.ll.lion.product.repository;

import com.ll.lion.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
