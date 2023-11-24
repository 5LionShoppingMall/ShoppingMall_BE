package com.ll.lion.user.controller;

import com.ll.lion.user.dto.LoginRequestDto;
import com.ll.lion.user.dto.LoginResponseDto;
import com.ll.lion.user.dto.VerificationRequestDto;
import com.ll.lion.user.entity.VerificationToken;
import com.ll.lion.user.repository.VerificationTokenRepository;
import com.ll.lion.user.security.JwtTokenUtil;
import com.ll.lion.user.service.AuthService;
import com.ll.lion.user.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                                  HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        try {
            // 로그인 인증 및 Access Token, Refresh Token 발급
            LoginResponseDto loginResp = authService.authenticate(email, password);

            String accessToken = loginResp.getAccessToken();
            // 클라이언트에게 Access Token과 Refresh Token을 전달
            authService.setTokenInCookie(accessToken, loginResp.getRefreshToken(), response);
            return ResponseEntity.ok(loginResp);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 사용자가 존재하지 않는 경우
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 이메일이 인증되지 않은 경우
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 비밀번호가 틀린 경우
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<Void> confirmAccount(@RequestParam String token) {
        try {
            authService.confirmAccount(token);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/check")
    public String check() {
        return "JWT SUCCESS";
    }
}
