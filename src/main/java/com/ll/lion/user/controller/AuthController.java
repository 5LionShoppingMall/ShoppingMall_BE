package com.ll.lion.user.controller;

import com.ll.lion.user.dto.LoginRequestDto;
import com.ll.lion.user.dto.LoginResponseDto;
import com.ll.lion.user.dto.PasswordResetDto;
import com.ll.lion.user.dto.PasswordResetRequestDto;
import com.ll.lion.user.entity.PasswordResetToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.service.AuthService;
import com.ll.lion.user.service.EmailService;
import com.ll.lion.user.service.PasswordResetTokenService;
import com.ll.lion.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                                  HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        try {

            // 로그인 인증 및 Access Token, Refresh Token 발급
            LoginResponseDto loginResp = authService.authenticate(email, password);
            if (loginResp == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

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
    public ResponseEntity<String> confirmAccount(@RequestParam String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        try {
            authService.confirmAccount(token);

            String successHtml = authService.generateHtmlResponse("이메일 인증이 완료되었습니다!",
                    "https://previews.123rf.com/images/lineartestpilot/lineartestpilot1803/lineartestpilot180307030/96672834-%EC%9B%83%EB%8A%94-%EC%82%AC%EC%9E%90-%EB%A7%8C%ED%99%94.jpg");
            return new ResponseEntity<>(successHtml, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            String failureHtml = authService.generateHtmlResponse("이메일 인증에 실패하였습니다.",
                    "https://us.123rf.com/450wm/marconi/marconi0807/marconi080700010/3322493-%EC%9A%B0%EB%8A%94-%EC%82%AC%EC%9E%90.jpg");
            return new ResponseEntity<>(failureHtml, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/check")
    public String check() {
        return "JWT SUCCESS";

    }

    @PostMapping("/request-reset-password")
    public ResponseEntity<?> requestResetPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto) {
        Optional<User> user = userService.getUserByEmail(passwordResetRequestDto.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
        PasswordResetToken resetToken = passwordResetTokenService.createTokenForUser(user.get());
        String tokenValue = resetToken.getToken(); // 실제 토큰 문자열을 가져옵니다.
        emailService.sendPasswordResetEmail(passwordResetRequestDto.getEmail(), tokenValue);

        return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 발송되었습니다.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto resetRequest) {
        PasswordResetToken resetToken = passwordResetTokenService.getTokenByToken(resetRequest.getToken());
        if (resetToken == null || resetToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않거나 만료된 토큰입니다.");
        }

        User user = resetToken.getUser();
        authService.updatePassword(user, resetRequest.getNewPassword());
        passwordResetTokenService.deleteToken(resetToken);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

}
