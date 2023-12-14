package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.book.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    public void sendConfirmationEmail(String to, String subject, String token) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);

        String link = baseUrl + "?token=" + token;

        StringBuilder messageContent = new StringBuilder("<p>Thank you for registering, please confirm your email address!</p>\n" +
                "<p>Click the button to confirm:</p>" + link);

        message.setText(String.valueOf(messageContent));
        mailSender.send(message);

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

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(textBuilder.toString());

        mailSender.send(message);

    }
}
