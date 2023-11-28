package com.ll.lion.community.repository;

import com.ll.lion.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 페이징
    Page<Post> findAll(Pageable pageable);
}
