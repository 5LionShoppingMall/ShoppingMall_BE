package com.ll.lion.user.service;

import com.ll.lion.user.dto.UserInfoDto;
import com.ll.lion.user.dto.UserRegisterDto;
import com.ll.lion.user.entity.RefreshToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.RefreshTokenRepository;
import com.ll.lion.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

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

    public RefreshToken findRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다"));
        return user.getRefreshToken();
    }

    public void saveRefreshToken(String email, String tokenKey) {
        User user = userRepository.findByEmail(email)
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

    public UserInfoDto getUserByEmailAndMakeDto(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        return userByEmail.map(this::userToUserDTO).orElse(null);
    }

    private UserInfoDto userToUserDTO(User user) {
        UserInfoDto userDTO = new UserInfoDto();
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        return userDTO;
    }
}
