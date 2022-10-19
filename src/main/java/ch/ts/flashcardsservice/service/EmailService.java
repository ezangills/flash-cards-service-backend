package ch.ts.flashcardsservice.service;

import ch.ts.flashcardsservice.dto.VerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendVerificationEmail(String email, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailFrom);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Email verification");
        simpleMailMessage.setText(code);
        javaMailSender.send(simpleMailMessage);
    }
}
