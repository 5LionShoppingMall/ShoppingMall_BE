package com.ll.lion.product.repository;

import com.ll.lion.product.entity.Product;
import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findProductsByKeywordAfterId(String keyword, Long lastId, int limit);
}
