package com.ll.lion.user.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.IfPointcut.IfFalsePointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtTokenProvider.resolveToken(request, "accessToken");
        String refreshToken = jwtTokenProvider.resolveToken(request, "refreshToken");

        try {
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new JwtException("로그인을 안한 유저의 요청");
            }
        } catch (JwtException ex) {
            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                String email = jwtTokenProvider.getEmail(refreshToken);
                String newAccessToken = jwtTokenProvider.createAccessToken(email, List.of("USER"));
                Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
                newAccessTokenCookie.setHttpOnly(true);
                newAccessTokenCookie.setPath("/");
                newAccessTokenCookie.setSecure(true);
                String accessTokenCookieHeader = newAccessTokenCookie.getName() + "=" + newAccessTokenCookie.getValue()
                        + "; HttpOnly; Secure; SameSite=None"; // SameSite 설정
                response.addHeader("Set-Cookie", accessTokenCookieHeader);


                Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                // Here you can add some response to the client about no token
                ex.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);

    }
}
