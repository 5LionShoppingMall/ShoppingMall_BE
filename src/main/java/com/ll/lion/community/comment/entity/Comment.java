package com.ll.lion.community.comment.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


import com.ll.lion.common.entity.DateEntity;
import com.ll.lion.community.post.entity.Post;
import com.ll.lion.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@SuperBuilder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
public class Comment extends DateEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = LAZY)
    private Comment parentComment; // 부모 댓글

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> replies = new ArrayList<>(); // 대댓글 목록

    // 대댓글을 위한 생성자
    private Comment(String content, User user, Post post, Comment parentComment) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.parentComment = parentComment;
    }

    // 정적 팩토리 메소드
    public static Comment createReply(String content, User user, Post post, Comment parentComment) {
        return new Comment(content, user, post, parentComment);
    }

    // 대댓글 추가 메소드
    public void addReply(Comment reply) {
        this.replies.add(reply);
        reply.parentComment = this;
    }

    public void update(String content) {
        this.content = content;
    }
}
