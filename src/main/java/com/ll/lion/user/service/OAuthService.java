package com.ll.lion.user.service;

import com.ll.lion.user.dto.LoginResponseDto;
import com.ll.lion.user.dto.SocialLoginDto;
import com.ll.lion.user.entity.RefreshToken;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.entity.VerificationToken;
import com.ll.lion.user.repository.UserRepository;
import com.ll.lion.user.repository.VerificationTokenRepository;
import com.ll.lion.user.security.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public User whenSocialLogin(SocialLoginDto socialLoginDto) {
        Optional<User> optUser = userService.getUserByEmail(socialLoginDto.getEmail());

        if (optUser.isPresent()) return optUser.get();

        return socialRegister(socialLoginDto);
    }

    public User socialRegister(SocialLoginDto socialLoginDto) {
        if (userService.checkIfEmailExist(socialLoginDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(socialLoginDto.getEmail())
                .password(passwordEncoder.encode(socialLoginDto.getProviderId()))
                .nickname(socialLoginDto.getNickname())
                .socialProvider(socialLoginDto.getProviderTypeCode())
                .providerId(socialLoginDto.getProviderId())
                .role("USER")
                .emailVerified(true)
                .build();

        User savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, savedUser);
        verificationTokenRepository.save(verificationToken);

        return savedUser;
    }

    public boolean isNullExistInProfile(String email) {
        Optional<User> optUser = userService.getUserByEmail(email);
        //null처리
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (user.getAddress() == null || user.getPhoneNumber() == null) return true;
        }

        return false;
    }

    @Transactional
    public LoginResponseDto authenticate(String email) {
        Optional<User> userByEmail = userService.getUserByEmail(email);
        if (!userByEmail.isPresent()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다."); // 사용자가 존재하지 않는 경우
        }
        User user = userByEmail.get();

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException("이메일이 아직 인증되지 않았습니다."); // 이메일이 인증되지 않은 경우
        }

        // 로그인 성공 시 JWT 토큰 생성
        String accessToken = jwtTokenUtil.createAccessToken(email, List.of("USER"));

        RefreshToken foundRefreshToken = userService.findRefreshToken(email);
        String refreshToken;
        if (foundRefreshToken != null) {
            refreshToken = foundRefreshToken.getKeyValue();
        } else {
            refreshToken = jwtTokenUtil.createRefreshToken(email, List.of("USER"));
            userService.saveRefreshToken(email, refreshToken);
        }

        return new LoginResponseDto(accessToken, refreshToken);
    }

    public String getUrlAndRemoveCookie(HttpServletRequest request, HttpServletResponse response) {
        String redirectUrlAfterSocialLogin = null;

        Optional<Cookie> optCookie = Arrays
                .stream(request.getCookies())
                .filter(c -> c.getName().equals("redirectUrlAfterSocialLogin"))
                .findFirst();

        if (optCookie.isPresent()) {
            redirectUrlAfterSocialLogin = optCookie.get().getValue();
        }

        ResponseCookie redirectUrlAfterSocialLoginCookie = ResponseCookie.from("redirectUrlAfterSocialLogin", "")
                .path("/")
                .maxAge(0) // 쿠키를 즉시 만료시키기 위해 Max-Age를 0으로 설정
                .build();
        response.addHeader("Set-Cookie", redirectUrlAfterSocialLoginCookie.toString());

        return redirectUrlAfterSocialLogin;
    }
}
