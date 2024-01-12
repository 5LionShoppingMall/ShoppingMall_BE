package com.ll.lion.common.config;

import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0) {
            IntStream.range(1, 6).forEach(i -> {
                User user = User.builder()
                        .email("test" + i + "@test.com")
                        .password(passwordEncoder.encode("test" + i))
                        .nickname("test" + i)
                        .role("USER") // Role 필드 설정
                        .profilePhotoUrl("https://play-lh.googleusercontent.com/proxy/2tj1HTTkxfLUCHMYCMY7Ik_u9Dv-ctrQ7tteluo8MkL9bUzSFutbEcvkGroJxU6PTS84IHjfzCYjRsCflXcZ5k_CV2OAD2Al4i_fUCrb6cBVNvtB4TZhu97Z=s3840-w3840-h2160")
                        .emailVerified(true) // 이메일 인증 여부를 true로 설정
                        .address("서울특별시")
                        .phoneNumber("01012345678")
                        .build();


                userRepository.save(user);
            });
        }
    }
}
