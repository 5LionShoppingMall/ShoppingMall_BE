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

        switch (providerTypeCode) {
            case "KAKAO" :
                socialLoginDto = extractKakaoData(providerTypeCode, oAuth2User);
                break;
            case "GOOGLE":
                socialLoginDto = extractGoogleData(providerTypeCode, oAuth2User);
                break;
            case "GITHUB":
                socialLoginDto = extractGitHubData(providerTypeCode, oAuth2User);
                break;
        }

        User user = oauthService.whenSocialLogin(socialLoginDto);

        return new SecurityUser(user.getId(), user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
    }


    public SocialLoginDto extractGoogleData(String providerTypeCode, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String profileImageUrl = (String) attributes.get("picture");
        String oauthId = oAuth2User.getName();
        String nickname = (String) attributes.get("name");
        String providerId = providerTypeCode + "__%s".formatted(oauthId); // 비밀번호로 설정

        return new SocialLoginDto(providerTypeCode, email, profileImageUrl, providerId, nickname);
    }

    public SocialLoginDto extractGitHubData(String providerTypeCode, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String profileImageUrl = (String) attributes.get("avatar_url");
        String oauthId = oAuth2User.getName();
        String nickname = (String) attributes.get("login");
        String providerId = providerTypeCode + "__%s".formatted(oauthId); // 비밀번호로 설정
        String email = (String) attributes.get("email");
        if(email == null) email = nickname + "@" + providerTypeCode;

        return new SocialLoginDto(providerTypeCode, email, profileImageUrl, providerId, nickname);
    }

    public SocialLoginDto extractKakaoData(String providerTypeCode, OAuth2User oAuth2User) {
        String oauthId = oAuth2User.getName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map attributesProperties = (Map) attributes.get("properties");

        String nickname = (String) attributesProperties.get("nickname");
        String profileImageUrl = (String) attributesProperties.get("profile_image");
        String providerId = providerTypeCode + "__" + oauthId;
        String email = nickname + "@" + providerTypeCode;

        return new SocialLoginDto(providerTypeCode, email, profileImageUrl, providerId, nickname);
    }
}

