package com.ll.lion.common.dto;

import lombok.Data;

@Data
public class RefreshTokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
