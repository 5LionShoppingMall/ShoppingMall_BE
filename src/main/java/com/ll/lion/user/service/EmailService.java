package com.ll.lion.user.service;

import com.ll.lion.user.config.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;


    @Async
    public void sendVerificationEmail(String email, String token) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, "utf-8");

        String htmlMsg = "<h3>오지는사자 중고 쇼핑몰 이메일 인증</h3>"
                + "<p>이메일 인증을 하시려면 아래 링크를 클릭해주세요 :</p>"
                + "<a href='http://localhost:8082/api/auth/confirm-account?token=" + token + "'>이메일 인증하기</a>";

        try {
            helper.setTo(email);
            helper.setSubject("오지는사자 중고 쇼핑몰 이메일 인증");
            helper.setText(htmlMsg, true); // true를 넣어주면 html 형식으로 메일이 보내집니다.
            helper.setFrom(mailProperties.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(mail);
    }

    @Async
    public void sendPasswordResetEmail(String email, String token) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, "utf-8");

        String resetUrl = "http://localhost:3000/auth/reset-password?token=" + token;
        String htmlMsg = "<h3>오지는사자 중고 쇼핑몰 비밀번호 재설정</h3>"
                + "<p>비밀번호를 재설정하려면 아래 링크를 클릭해주세요 :</p>"
                + "<a href='" + resetUrl + "'>비밀번호 재설정하기</a>";

        try {
            helper.setTo(email);
            helper.setSubject("오지는사자 중고 쇼핑몰 비밀번호 재설정");
            helper.setText(htmlMsg, true); // HTML 형식의 메일
            helper.setFrom(mailProperties.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가 가능
        }

        javaMailSender.send(mail);
    }


}
