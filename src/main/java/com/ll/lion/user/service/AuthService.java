package com.ll.lion.user.service;

import com.ll.lion.user.dto.LoginResponseDto;
import com.ll.lion.user.entity.RefreshToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.entity.VerificationToken;
import com.ll.lion.user.repository.VerificationTokenRepository;
import com.ll.lion.user.security.JwtTokenUtil;
import com.ll.lion.user.security.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDetailsServiceImpl userDetailsService;
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
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setSecure(true);
        String accessTokenCookieHeader = accessTokenCookie.getName() + "=" + accessTokenCookie.getValue()
                + "; Path=/; HttpOnly; Secure; SameSite=None"; // SameSite 설정

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        String refreshTokenCookieHeader = refreshTokenCookie.getName() + "=" + refreshTokenCookie.getValue()
                + ";  Path=/; HttpOnly; Secure; SameSite=None"; // SameSite 설정

        response.addHeader("Set-Cookie", accessTokenCookieHeader);
        response.addHeader("Set-Cookie", refreshTokenCookieHeader);
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 accessToken과 refreshToken을 찾아서 삭제합니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken") || cookie.getName().equals("refreshToken")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    String deleteCookie = cookie.getName() + "="
                            + "; Expires=Thu, 01 Jan 1970 00:00:00 GMT"
                            + "; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=None";
                    response.addHeader("Set-Cookie", deleteCookie);
                }
            }
        }

        // 로그아웃에 성공했음을 알리는 응답을 반환합니다.
        return ResponseEntity.ok().body("로그아웃 성공");
    }

}
