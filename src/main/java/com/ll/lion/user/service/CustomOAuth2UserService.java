package com.ll.lion.user.service;

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

        String oauthId = oAuth2User.getName(); //provider마다 unique한 식별자
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map attributesProperties = (Map) attributes.get("properties");

        String nickname = (String) attributesProperties.get("nickname");
        String profileImgUrl = (String) attributesProperties.get("profile_image");
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String providerId = providerTypeCode + "__%s".formatted(oauthId);
        User user = oauthService.whenSocialLogin(providerTypeCode, providerId, nickname, profileImgUrl);

        return new SecurityUser(user.getId(), providerId, "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        } //providerId가 username으로 등록됨
    }
