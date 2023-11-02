package com.example.borys_mankowski_test_10.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    JavaMailSender mailSender;
    @InjectMocks
    EmailService emailService;
    @Test
    void sendSimpleMessage() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(to);
        expectedMessage.setSubject(subject);
        expectedMessage.setText(text);

        emailService.sendSimpleMessage(to, subject, text);

        Mockito.verify(mailSender).send(expectedMessage);
    }
}
