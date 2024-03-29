package com.ll.lion.user.service;

import com.ll.lion.user.dto.UserInfoDto;
import com.ll.lion.user.dto.UserRegisterDto;
import com.ll.lion.user.dto.UserUpdateDto;
import com.ll.lion.user.entity.RefreshToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.entity.VerificationToken;
import com.ll.lion.user.repository.RefreshTokenRepository;
import com.ll.lion.user.repository.UserRepository;
import com.ll.lion.user.repository.VerificationTokenRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService; // 가상의 이메일 서비스
    private final VerificationTokenRepository verificationTokenRepository;

    public User register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .nickname(userRegisterDto.getNickname())
                .phoneNumber(userRegisterDto.getPhoneNumber())
                .address(userRegisterDto.getAddress())
                .profilePhotoUrl(userRegisterDto.getProfilePictureUrl())
                .createdAt(LocalDateTime.now())
                .role("USER")
                .emailVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, savedUser);
        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(savedUser.getEmail(), token);

        return savedUser;
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

    public void verifyEmail(User user) {
        user.verifyEmail();
        userRepository.save(user);
    }

    public boolean isNicknameExist(String nickname) {
        return userRepository.existsByNickname(nickname);

    }

    public boolean checkIfEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }


    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User updateUserInfo(String email, UserUpdateDto userUpdateDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        user.update(userUpdateDto);
        return user;
    }
    public UserInfoDto userToUserDTO(String email) {
        Optional<User> userByEmail = getUserByEmail(email);
        UserInfoDto userDTO = new UserInfoDto();

        if (userByEmail.isPresent()) {
            User user = userByEmail.get();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setNickname(user.getNickname());
            userDTO.setAddress(user.getAddress());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setProfileImageUrl(user.getProfilePhotoUrl());
        }
        return userDTO;
    }


}
