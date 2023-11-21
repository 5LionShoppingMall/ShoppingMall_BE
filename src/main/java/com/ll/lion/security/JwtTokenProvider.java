package com.ll.lion.security;

import com.ll.lion.common.dto.RefreshTokenDto;
import com.ll.lion.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    private final RefreshTokenService refreshTokenService;

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

    public String refreshAccessToken(String expiredAccessToken, String refreshToken) {
        try {
            // Access Token 검증 및 만료 확인
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(expiredAccessToken)
                    .getBody();

            if (!claims.getExpiration().before(new Date())) {
                throw new Exception("Access Token이 아직 만료되지 않았습니다.");
            }

            // Redis에서 Refresh Token 확인
            Optional<RefreshTokenDto> foundRefreshToken = refreshTokenService.getToken(refreshToken);
            if (foundRefreshToken.isEmpty()) {
                throw new Exception("유효하지 않은 Refresh Token입니다.");
            }

            // 새로운 Access Token 발급
            String email = claims.getSubject();
            String newAccessToken = createAccessToken(email, List.of("USER"));

            return newAccessToken;
        } catch (Exception e) {
            // Access Token 검증 실패 또는 만료되지 않은 경우, 또는 Refresh Token이 유효하지 않은 경우
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
