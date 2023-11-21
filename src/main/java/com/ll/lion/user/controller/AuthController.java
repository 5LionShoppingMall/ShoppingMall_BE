package com.ll.lion.user.controller;

import com.ll.lion.user.dto.LoginRequestDto;
import com.ll.lion.user.dto.LoginResponseDto;
import com.ll.lion.user.dto.RefreshTokenRequestDto;
import com.ll.lion.user.security.JwtTokenProvider;
import com.ll.lion.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 로그인 인증 및 Access Token, Refresh Token 발급
        LoginResponseDto loginResp = authService.authenticate(email, password);
        String accessToken = loginResp.getAccessToken();

        if (accessToken != null) {
            // 클라이언트에게 Access Token과 Refresh Token을 전달
            return ResponseEntity.ok(loginResp);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<String> refreshAccessToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String expiredAccessToken = refreshTokenRequestDto.getAccessToken();
        String refreshToken = refreshTokenRequestDto.getRefreshToken();
        String newAccessToken = jwtTokenProvider.refreshAccessToken(expiredAccessToken, refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/check")
    public String check(){
        return "JWT SUCCESS";
    }
}
