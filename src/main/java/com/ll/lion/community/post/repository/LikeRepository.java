package com.ll.lion.community.post.repository;

import com.ll.lion.community.post.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
