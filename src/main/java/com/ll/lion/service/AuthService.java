package com.ll.lion.service;

import com.ll.lion.common.dto.LoginResponseDto;
import com.ll.lion.common.dto.RefreshTokenDto;
import com.ll.lion.security.InvalidPasswordException;
import com.ll.lion.security.JwtTokenProvider;
import com.ll.lion.security.UserDetailsServiceImpl;
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

    private void saveRefreshToken(String email, String refreshToken) {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken, email);
        refreshTokenService.saveToken(refreshTokenDto);
    }

}
