package com.ll.lion.community.dto.post;

import com.ll.lion.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostReqDto {
    @NotNull
    private String title;
    @NotNull
    private String content;
    private String email;
}