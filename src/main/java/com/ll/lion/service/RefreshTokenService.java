package com.ll.lion.service;

import com.ll.lion.entity.RefreshToken;
import com.ll.lion.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository tokenRepository;

    public void saveToken(String token, String userInfo) {
        RefreshToken newToken = new RefreshToken();
        newToken.set(token);
        newToken.setUserInfo(userInfo);
        tokenRepository.save(newToken);
    }

    public Optional<String> getUserInfo(String token) {
        Optional<Token> foundToken = tokenRepository.findById(token);
        return foundToken.map(Token::getUserInfo);
    }
}
