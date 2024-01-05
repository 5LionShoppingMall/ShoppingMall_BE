package com.ll.lion.community.repository;

import com.ll.lion.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 페이징
    Page<Post> findAll(Pageable pageable);

    // 모든 게시글 작성일시가 가장 최근인 순서로 조회
    List<Post> findAllByOrderByCreatedAtDesc();
}
