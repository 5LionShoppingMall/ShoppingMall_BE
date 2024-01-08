package com.ll.lion.user.service;

import com.ll.lion.user.entity.RefreshToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.RefreshTokenRepository;
import com.ll.lion.user.repository.UserRepository;
import com.ll.lion.user.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public User whenSocialLogin(String providerTypeCode, String providerId, String nickname, String profileImgUrl) {
        Optional<User> optUser = userService.getUserByProviderId(providerId);

        if (optUser.isPresent()) return optUser.get();

        String refreshToken = jwtTokenUtil.createRefreshToken(providerId, List.of("USER")); //email대신 providerId
        saveRefreshToken(providerId, refreshToken);

        return new User().toBuilder()
                .socialProvider(providerTypeCode)
                .providerId(providerId)
                .nickname(nickname)
                .profilePhotoUrl(profileImgUrl)
                .build();
    }

    public void saveRefreshToken(String providerId, String tokenKey) {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        RefreshToken refreshToken = RefreshToken.builder()
                .keyValue(tokenKey)
                .build();

        refreshTokenRepository.save(refreshToken);

        user = user.toBuilder()
                .refreshToken(refreshToken)
                .build();

        userRepository.save(user);
    }
}
