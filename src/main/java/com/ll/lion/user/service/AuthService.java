package com.ll.lion.user.service;

import com.ll.lion.user.dto.LoginResponseDto;
import com.ll.lion.user.dto.RefreshTokenDto;
import com.ll.lion.user.security.InvalidPasswordException;
import com.ll.lion.user.security.JwtTokenProvider;
import com.ll.lion.user.security.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public LoginResponseDto authenticate(String email, String password) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            return null; // 사용자가 존재하지 않는 경우
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            // 로그인 성공 시 JWT 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken(email, List.of("USER"));
            String refreshToken = jwtTokenProvider.createRefreshToken(email, List.of("USER"));

            // RefreshToken 저장
            saveRefreshToken(email, refreshToken);

            return new LoginResponseDto(accessToken, refreshToken);
        } else {
            throw new InvalidPasswordException("비밀번호를 확인해주세요."); // 비밀번호가 일치하지 않는 경우
        }
    }

    public void setTokenInCookie(String accessToken, String refreshToken, HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setSecure(true);
        String accessTokenCookieHeader = accessTokenCookie.getName() + "=" + accessTokenCookie.getValue()
                + "; HttpOnly; Secure; SameSite=None"; // SameSite 설정


        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        String refreshTokenCookieHeader = refreshTokenCookie.getName() + "=" + refreshTokenCookie.getValue()
                + "; HttpOnly; Secure; SameSite=None"; // SameSite 설정


        response.addHeader("Set-Cookie", accessTokenCookieHeader);
        response.addHeader("Set-Cookie", refreshTokenCookieHeader);
    }

    private void saveRefreshToken(String email, String refreshToken) {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken, email);
        refreshTokenService.saveToken(refreshTokenDto);
    }

}
