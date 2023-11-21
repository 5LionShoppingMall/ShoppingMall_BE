package com.ll.lion.service;

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

    public String authenticate(String email, String password) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            return null; // 사용자가 존재하지 않는 경우
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            // 로그인 성공 시 JWT 토큰 생성
            return jwtTokenProvider.createToken(email, List.of("USER"));
        } else {
            throw new InvalidPasswordException("비밀번호를 확인해주세요."); // 비밀번호가 일치하지 않는 경우
        }
    }

}
