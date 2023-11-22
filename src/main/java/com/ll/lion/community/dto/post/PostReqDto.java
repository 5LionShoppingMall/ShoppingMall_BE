package com.ll.lion.community.dto.post;

import com.ll.lion.user.entity.User;
import lombok.Getter;

@Getter
public class PostReqDto {
    private String title;
    private String content;
    private String email;
}