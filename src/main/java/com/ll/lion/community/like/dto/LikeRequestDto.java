package com.ll.lion.community.like.dto;

import lombok.Data;

@Data
public class LikeRequestDto {
    private Long userId;
    private Long postId;
}
