package com.ll.lion.product.repository;

import com.ll.lion.product.entity.Product;
import com.ll.lion.product.entity.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> findProductsByKeywordAfterId(String keyword, Long lastId, int limit) {
        QProduct product = QProduct.product;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        BooleanExpression keywordPredicate = product.title.containsIgnoreCase(keyword)
                .or(product.description.containsIgnoreCase(keyword));
        BooleanExpression idPredicate = (lastId == null) ? null : product.id.gt(lastId);

        return queryFactory.selectFrom(product)
                .where(keywordPredicate, idPredicate)
                .orderBy(product.id.asc())
                .limit(limit)
                .fetch();
    }
}
