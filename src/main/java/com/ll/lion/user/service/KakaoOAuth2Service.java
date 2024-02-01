package com.ll.lion.user.service;


import com.ll.lion.user.dto.KakaoPropertiesDto;
import com.ll.lion.user.dto.KakaoTokenResponseDto;
import com.ll.lion.user.dto.KakaoUserInfoDto;
import com.ll.lion.user.entity.RefreshToken;
import com.ll.lion.user.entity.SocialProvider;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.RefreshTokenRepository;
import com.ll.lion.user.repository.UserRepository;
import com.ll.lion.user.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2Service {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserService userService;

    private final AuthService authService;

    private final JwtTokenUtil jwtTokenUtil;

    private final WebClient webClient;
    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";

    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;


    public Mono<KakaoTokenResponseDto> getToken(String code) {
        String uri = TOKEN_URI + "?grant_type=" + GRANT_TYPE + "&client_id=" + clientId + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class);

    }


    public Mono<KakaoUserInfoDto> getUserInfo(String token) {
        String uri = USER_INFO_URI;

        return webClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(KakaoUserInfoDto.class)
                .next(); // Flux 스트림의 첫 번째 항목을 반환
    }

    public void registerUser(KakaoPropertiesDto properties, HttpServletResponse response) {
        String nickname = properties.getNickname();
        String profileImageUrl = properties.getProfile_image();

        Optional<User> byUsername = userRepository.findByNickname(nickname);

        String email = nickname + "@kakao.com";

        String newAccessToken = jwtTokenUtil.createAccessToken(email, List.of("USER"));
        String refreshToken;

        if (byUsername.isPresent()) {
            RefreshToken foundRefreshToken = userService.findRefreshToken(email);
            refreshToken = foundRefreshToken.getKeyValue();

        } else {
            String tokenKey = jwtTokenUtil.createRefreshToken(email, List.of("USER"));

            RefreshToken createdRefreshToken = RefreshToken.builder()
                    .keyValue(tokenKey)
                    .build();

            User user = User
                    .builder()
                    .nickname(nickname)
                    .email(email)
                    .role("USER")
                    .emailVerified(true)
                    .profilePhotoUrl(profileImageUrl)
                    .provider(SocialProvider.KAKAO)
                    .refreshToken(createdRefreshToken)
                    .build();

            userRepository.save(user);

            refreshToken = createdRefreshToken.getKeyValue();
        }

        authService.setTokenInCookie(newAccessToken, refreshToken, response);

    }
}
