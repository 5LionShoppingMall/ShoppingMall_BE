package com.ll.lion.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OauthService {

    public Mono<String> getAccessToken(String code) {
        // 카카오 토큰 발급 API URL
        String kakaoTokenApiUrl = "https://kauth.kakao.com/oauth/token";

        // 카카오 앱의 클라이언트 아이디와 리다이렉트 URI
        String clientId = "YOUR_KAKAO_APP_CLIENT_ID";
        String redirectUri = "YOUR_KAKAO_APP_REDIRECT_URI";

        // WebClient 생성
        WebClient webClient = WebClient.builder()
                .baseUrl(kakaoTokenApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        // API 요청
        return webClient.post()
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(rootNode -> rootNode.get("access_token").asText());
    }


}
