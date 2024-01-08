package com.ll.lion.user.service;

import com.ll.lion.user.dto.LoginResponseDto;
import com.ll.lion.user.entity.RefreshToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.entity.VerificationToken;
import com.ll.lion.user.repository.VerificationTokenRepository;
import com.ll.lion.user.security.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final VerificationTokenRepository verificationTokenRepository;

    public LoginResponseDto authenticate(String email, String password) {
        Optional<User> userByEmail = userService.getUserByEmail(email);
        if (!userByEmail.isPresent()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다."); // 사용자가 존재하지 않는 경우
        }

        User user = userByEmail.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀립니다."); // 비밀번호가 틀린 경우
        }

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException("이메일이 아직 인증되지 않았습니다."); // 이메일이 인증되지 않은 경우
        }

        // 로그인 성공 시 JWT 토큰 생성
        String accessToken = jwtTokenUtil.createAccessToken(email, List.of("USER"));

        RefreshToken foundRefreshToken = userService.findRefreshToken(email);
        String refreshToken;
        if (foundRefreshToken != null) {
            refreshToken = foundRefreshToken.getKeyValue();
        } else {
            refreshToken = jwtTokenUtil.createRefreshToken(email, List.of("USER"));
            userService.saveRefreshToken(email, refreshToken);
        }

        return new LoginResponseDto(accessToken, refreshToken);
    }

    public void confirmAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        userService.verifyEmail(verificationToken.getUser());
    }

    public void setTokenInCookie(String accessToken, String refreshToken, HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None") // SameSite 설정
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None") // SameSite 설정
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 accessToken과 refreshToken을 찾아서 삭제합니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken") || cookie.getName().equals("refreshToken")) {
                    ResponseCookie deleteCookie = ResponseCookie.from(cookie.getName(), "")
                            .httpOnly(true)
                            .path("/")
                            .secure(true)
                            .sameSite("None") // SameSite 설정
                            .maxAge(0) // 쿠키의 유효기간을 0으로 설정하여 쿠키를 삭제
                            .build();

                    response.addHeader("Set-Cookie", deleteCookie.toString());
                }
            }
        }

        // 로그아웃에 성공했음을 알리는 응답을 반환합니다.
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    public String generateHtmlResponse(String message, String imageUrl) {
        return "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "</head>"
                + "<body>"
                + "<h1>" + message + "</h1>"
                + "<img src='" + imageUrl + "' alt='Lion Image'>"
                + "</body>"
                + "</html>";
    }

    public boolean checkIfEmailExist(String email) {
        Optional<User> userByEmail = userService.getUserByEmail(email);
        return userByEmail.isPresent();
    }



}
