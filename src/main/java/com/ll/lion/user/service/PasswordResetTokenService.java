package com.ll.lion.user.service;

import com.ll.lion.user.entity.PasswordResetToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.PasswordResetTokenRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        return tokenRepository.save(resetToken);
    }

    public PasswordResetToken getTokenByToken(String token) {
        return tokenRepository.findByToken(token).orElse(null);
    }

    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }

}
