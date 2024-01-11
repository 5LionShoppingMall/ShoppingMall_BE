package com.ll.lion.product.repository;

import com.ll.lion.product.entity.Product;
import com.ll.lion.product.entity.QProduct;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Product> findAllByKeyword(Pageable pageable, String keyword) {
        QProduct product = QProduct.product;

        JPAQuery<Product> query = new JPAQuery<>(em);
        JPAQuery<Long> countQuery = new JPAQuery<>(em);

        List<Product> products = query.from(product)
                .where(product.title.contains(keyword)
                        .or(product.description.contains(keyword))
                )
                .orderBy(product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = countQuery.from(product)
                .where(product.title.contains(keyword)
                        .or(product.description.contains(keyword))
                )
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }
}
