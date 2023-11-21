package com.ll.lion.community.repository;

import com.ll.lion.community.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
