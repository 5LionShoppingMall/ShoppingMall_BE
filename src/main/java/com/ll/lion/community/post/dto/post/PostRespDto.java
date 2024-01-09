package com.ll.lion.community.post.dto.post;

import com.ll.lion.community.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostRespDto {
    private Long id;
    private String title;
    private String content;
    private Integer viewCount;
    private PostUserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostRespDto(final Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.viewCount = entity.getViewCount();
        this.user = new PostUserDto(entity.getUser());
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
