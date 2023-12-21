package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.book.model.Book;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;


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

        verify(mailSender).send(expectedMessage);
    }

    @Test
    void sendConfirmationEmail() {
        String to = "recipient@example.com";
        String subject = "Confirmation Subject";
        String token = "someToken";
        String baseUrl = "null";

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(to);
        expectedMessage.setSubject(subject);

        String link = baseUrl + "?token=" + token;
        String expectedContent = "<p>Thank you for registering, please confirm your email address!</p>\n" +
                "<p>Click the button to confirm:</p>" + link;

        expectedMessage.setText(expectedContent);

        emailService.sendConfirmationEmail(to, subject, token);

        verify(mailSender).send(expectedMessage);
    }

    @Test
    void sendNotificationIfNewBooks() {
        String to = "recipient@example.com";
        String subject = "New books added to the book store!";

        List<Book> books = Arrays.asList(
                new Book(1L, "Author1", "Title1", "Category1", LocalDate.now(), 1),
                new Book(2L, "Author2", "Title2", "Category2", LocalDate.now(), 1)
        );

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(to);
        expectedMessage.setSubject(subject);

        StringBuilder textBuilder = new StringBuilder("New books added to the store, take a look at the new positions below! :\n");
        for (Book book : books) {
            textBuilder.append("Title: ").append(book.getTitle())
                    .append(" Author: ").append(book.getAuthor())
                    .append(" Category: ").append(book.getCategory())
                    .append("\n\n");
        }

        expectedMessage.setText(textBuilder.toString());

        emailService.sendNotificationIfNewBooks(to, books);

        verify(mailSender).send(expectedMessage);
    }
}
