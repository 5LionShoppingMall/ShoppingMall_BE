package com.ll.lion.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocialLoginDto {
    private String email;
    private String profileImageUrl;
    private String oauthId;
    private String nickname;
}
