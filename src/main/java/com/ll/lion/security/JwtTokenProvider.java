package com.ll.lion.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value(value = "${jwt.secret}")
    private String secretKey;

    @Value(value = "${jwt.access-token-time}")
    private long accessTokenValidityInMilliseconds;

    @Value(value = "${jwt.refresh-token-time}")
    private long refreshTokenValidityInMilliseconds;

    private final UserDetailsServiceImpl userDetailsService;

    public String createRefreshToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            // Refresh Token 검증
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Refresh Token이 만료되었는지 확인
            if (claims.getExpiration().before(new Date())) {
                throw new Exception("Refresh Token이 만료되었습니다.");
            }

            // Access Token 발급
            String email = claims.getSubject();
            String accessToken = createAccessToken(email, List.of("USER"));

            return accessToken;
        } catch (Exception e) {
            // Refresh Token 검증 실패 또는 만료된 경우
            e.printStackTrace();
            return null;
        }
    }

    public String createAccessToken(String email, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String email) {

        Claims claims = Jwts.claims().setSubject(email);

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setClaims(claims) // 필요한 정보를 클레임에 담습니다.
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return refreshToken;
    }


    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("유효하지 않은 JWT Token입니다.");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
