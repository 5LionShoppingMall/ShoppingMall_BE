package com.ll.lion.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.lion.common.entity.DateEntity;
import com.ll.lion.user.entity.User;
import jakarta.persistence.*;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Post extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String content;

    private Integer viewCount = 0;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Post(Long id, String title, String content,
                Integer viewCount, User user, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.user = user;
        this.comments = comments;
    }
}