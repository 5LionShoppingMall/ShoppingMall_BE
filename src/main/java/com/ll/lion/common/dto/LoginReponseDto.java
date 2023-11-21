package com.ll.lion.common.dto;

import lombok.Data;

@Data
public class LoginReponseDto {
    private String accessToken;
    private String refreshToken;
}
