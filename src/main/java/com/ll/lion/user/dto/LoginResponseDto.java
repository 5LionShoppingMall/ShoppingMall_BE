package com.ll.lion.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//AuthService가 AuthController의 요구에 응답하는 정보를 담는 객체
@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;

}
