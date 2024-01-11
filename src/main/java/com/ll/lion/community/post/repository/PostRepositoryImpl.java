package com.ll.lion.community.post.repository;

import com.ll.lion.community.post.entity.Post;
import com.ll.lion.community.post.entity.QPost;
import com.ll.lion.product.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public class PostRepositoryImpl implements PostRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Post> findPostsByKeywordAfterId(String keyword, Long lastId, int limit) {
        QPost post = QPost.post;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        BooleanExpression keywordPredicate = post.title.containsIgnoreCase(keyword)
                .or(post.content.containsIgnoreCase(keyword));
        BooleanExpression idPredicate = (lastId == null) ? null : post.id.gt(lastId);

        return queryFactory.selectFrom(post)
                .where(keywordPredicate, idPredicate)
                .orderBy(post.id.asc())
                .limit(limit)
                .fetch();
    }

    ;
}
