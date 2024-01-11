package com.ll.lion.community.post.repository;

import com.ll.lion.community.post.dto.post.PostDto;
import com.ll.lion.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> findAllByKeyword(Pageable pageable, String keyword);
}
