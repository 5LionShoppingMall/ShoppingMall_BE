package com.ll.lion.user.dto;

import lombok.Data;

@Data
public class RefreshTokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
