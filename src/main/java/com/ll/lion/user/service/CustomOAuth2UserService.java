package com.ll.lion.user.service;

import com.ll.lion.user.dto.SocialLoginDto;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final OAuthService oauthService;
    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        SocialLoginDto socialLoginDto = null;

        switch (providerTypeCode){
            case "GOOGLE" : socialLoginDto = extractGoogleData(oAuth2User);
            case "GITHUB" : socialLoginDto = extractGitHubData(oAuth2User);
        }

        String email = socialLoginDto.getEmail();
        String oauthId = socialLoginDto.getOauthId(); //provider마다 unique한 식별자
        String nickname = socialLoginDto.getNickname();
        String profileImgUrl = socialLoginDto.getProfileImageUrl();

        String providerId = providerTypeCode + "__%s".formatted(oauthId);
        User user = oauthService.whenSocialLogin(providerTypeCode, providerId, nickname, profileImgUrl);

        return new SecurityUser(user.getId(), providerId, "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        } //providerId가 username으로 등록됨

    public SocialLoginDto extractGoogleData(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String profileImageUrl = (String) attributes.get("picture");
        String oauthId = oAuth2User.getName();
        String nickname = (String) attributes.get("name");

        return new SocialLoginDto(email, profileImageUrl, oauthId, nickname);
    }

    public SocialLoginDto extractGitHubData(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        if (attributes.containsKey("email")) {
            email = (String) attributes.get("email");
        }
        String profileImageUrl = (String) attributes.get("avatar_url");
        String oauthId = oAuth2User.getName();
        String nickname = (String) attributes.get("login");

        return new SocialLoginDto(email, profileImageUrl, oauthId, nickname);
    }
}

