package com.ll.lion.community.post.repository;

import com.ll.lion.community.post.dto.post.PostDto;
import com.ll.lion.community.post.entity.Post;
import com.ll.lion.community.post.entity.QPost;
import com.ll.lion.user.entity.QUser;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public class PostRepositoryImpl implements PostRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Post> findAllByKeyword(Pageable pageable, String keyword) {
        QPost post = QPost.post;
        QUser user = new QUser("user2");

        JPAQuery<Post> query = new JPAQuery<>(em);
        JPAQuery<Long> countQuery = new JPAQuery<>(em);

        List<Post> posts = query.from(post)
                .leftJoin(post.user, user)
                .where(post.title.contains(keyword)
                        .or(post.content.contains(keyword))
                        .or(user.nickname.contains(keyword)))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = countQuery.from(post)
                .leftJoin(post.user, user)
                .where(post.title.contains(keyword)
                        .or(post.content.contains(keyword))
                        .or(user.nickname.contains(keyword)))
                .fetchCount();

        return new PageImpl<>(posts, pageable, total);
    }
}
