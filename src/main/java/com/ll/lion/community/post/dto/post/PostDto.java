package com.ll.lion.community.post.dto.post;

import com.ll.lion.community.comment.entity.Comment;
import com.ll.lion.community.post.entity.Post;
import com.ll.lion.user.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private Integer viewCount;
    private User user;
    private List<Comment> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PostDto(Long id, String title, String content,
                   Integer viewCount, User user, List<Comment> comments,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.user = user;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 요청받은 PostReqDto 타입을 PostDto 타입으로 바꾸기
    public PostDto(final PostReqDto dto, User user) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.user = user;
        this.viewCount = 0;
    }

    public PostDto(final Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.viewCount = entity.getViewCount();
        this.user = entity.getUser();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    // PostDto 타입을 Post 타입으로 바꾸기
    public static Post toEntity(final PostDto dto) {
        return Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(dto.getViewCount())
                .user(dto.getUser())
                .build();
    }
}
