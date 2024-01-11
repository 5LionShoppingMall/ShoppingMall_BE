package com.ll.lion.community.post.repository;

import com.ll.lion.community.post.entity.Post;
import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findPostsByKeywordAfterId(String keyword, Long lastId, int limit);
}
