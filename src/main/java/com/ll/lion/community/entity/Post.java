package com.ll.lion.community.entity;

import com.ll.lion.common.entity.DateEntity;
import com.ll.lion.community.dto.post.PostReqDto;
import com.ll.lion.user.entity.User;
import jakarta.persistence.*;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public Post(PostReqDto postReqDto, User user) {
        this.title = postReqDto.getTitle();
        this.content = postReqDto.getContent();
        this.user = user;
    }
}