package com.ll.lion.user.service;

import com.ll.lion.user.dto.UserRegisterDto;
import com.ll.lion.user.entity.RefreshToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .phoneNumber(userRegisterDto.getPhoneNumber())
                .address(userRegisterDto.getAddress())
                .createdAt(LocalDateTime.now())
                .role("USER")
                .build();

        return userRepository.save(user);
    }

    public boolean hasRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        return user.getRefreshToken() != null;
    }

    public void saveRefreshToken(String email, String tokenKey) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        RefreshToken refreshToken = RefreshToken.builder()
                .key(tokenKey)
                .user(user)
                .build();

        user = user.toBuilder()
                .refreshToken(refreshToken)
                .build();

        userRepository.save(user);
    }
}
