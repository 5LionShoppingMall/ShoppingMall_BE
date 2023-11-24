package com.ll.lion.user.service;

import com.ll.lion.user.config.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;


    public void sendVerificationEmail(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Email Verification");
        mailMessage.setText("This is a verification email.");
        mailMessage.setFrom(mailProperties.getUsername());

        javaMailSender.send(mailMessage);
    }
}
