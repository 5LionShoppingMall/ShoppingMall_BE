package com.ll.lion.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("dev")
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    @DisplayName("이메일이 보내지는 지 확인하는 테스트")
    public void sendSimpleMessageTest() {
        // 이메일 정보 설정
        String to = "rlaruagh030@gmail.com";
        String subject = "Test Subject";
        String text = "Test mail content";

        // 메서드 호출 검증
        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(mailCaptor.capture());
        SimpleMailMessage sentMessage = mailCaptor.getValue();

        // 전송된 메시지 검증
        assertThat(sentMessage.getTo()[0]).isEqualTo(to);
        assertThat(sentMessage.getSubject()).isEqualTo(subject);
        assertThat(sentMessage.getText()).isEqualTo(text);
    }
}