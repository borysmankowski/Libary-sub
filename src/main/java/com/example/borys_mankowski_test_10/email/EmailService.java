package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.book.model.Book;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${library.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender, @Value("${library.base-url}") String baseUrl) {
        this.mailSender = mailSender;
        this.baseUrl = baseUrl;
    }

    public void sendConfirmationEmail(String to, String subject, String token) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");


        try {
            helper.setTo(to);
            helper.setSubject(subject);

            String link = baseUrl + "?token=" + token;

            StringBuilder messageContent = new StringBuilder("<p>Thank you for registering, please confirm your email address!</p>\n" +
                    "<p>Click the button to confirm:</p>" + link);


            helper.setText(String.valueOf(messageContent), true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new IllegalArgumentException("Failed to send email for: " + e);
        }
    }


    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to.trim());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendNotificationIfNewBooks(String to, List<Book> books) {

        String subject = "New books added to the book store!";

        StringBuilder textBuilder = new StringBuilder("New books added to the store, take a look at the new positions below! :\n");
        for (Book book : books) {
            textBuilder.append("Title: ").append(book.getTitle())
                    .append(" Author: ").append(book.getAuthor())
                    .append(" Category: ").append(book.getCategory())
                    .append("\n\n");

        }

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textBuilder.toString(), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Email was not sent due to an error! ", e);
        }
    }
}
