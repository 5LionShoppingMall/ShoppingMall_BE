package com.ll.lion.community.dto.post;

import com.ll.lion.community.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostRespDto {
    private Long id;
    private String title;
    private String content;
    private Integer viewCount;
//    private User user;
//    private List<Comment> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostRespDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
//        this.user = post.getUser();
//        this.comments = post.getComments();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
