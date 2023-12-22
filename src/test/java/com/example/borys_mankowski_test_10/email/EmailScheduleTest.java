package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.model.AppUserEmailBooks;
import com.example.borys_mankowski_test_10.book.BookRepository;
import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.subscription.SubscriptionRepository;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailScheduleTest {

    @InjectMocks
    private EmailSchedule emailSchedule;

    @Mock
    private EmailService emailService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Test
    public void testSendScheduledEmailNotification() {

        when(bookRepository.findAllByAddedDateToday(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        when(subscriptionRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        emailSchedule.sendScheduledEmailNotification();

        verify(emailService, times(0)).sendNotificationIfNewBooks(anyString(), anyList());
    }

    @Test
    public void testProcessSubscriptionsInBatches() {

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Tomasz");
        book.setCategory("Comedy");
        book.setTitle("Januszex");

        Book book1 = new Book();
        book1.setId(2L);
        book1.setAuthor("Janusz");
        book1.setCategory("Horror");
        book1.setTitle("Tomaszex");

        when(subscriptionRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        List<Book> newBooks = Arrays.asList(book, book1);

        emailSchedule.processSubscriptionsInBatches(newBooks, new ArrayList<>());

        verify(subscriptionRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testUpdateUserEmailBooksList() {

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Tomasz");
        book.setCategory("Comedy");
        book.setTitle("Januszex");

        Book book1 = new Book();
        book1.setId(2L);
        book1.setAuthor("Janusz");
        book1.setCategory("Horror");
        book1.setTitle("Tomaszex");

        String userEmail = "test@example.com";
        Set<Book> matchedBooks = new HashSet<>(Arrays.asList(book, book1));

        List<AppUserEmailBooks> appUserEmailBooksList = new ArrayList<>();
        appUserEmailBooksList.add(new AppUserEmailBooks("other@example.com", Collections.emptyList()));

        emailSchedule.updateUserEmailBooksList(userEmail, matchedBooks, appUserEmailBooksList);

        assertEquals(2, appUserEmailBooksList.size());
        assertEquals(userEmail, appUserEmailBooksList.get(1).getUserEmail());
        assertEquals(matchedBooks, new HashSet<>(appUserEmailBooksList.get(1).getMatchedBooks()));
    }

    @Test
    public void testMatchBooksToSubscription() {

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Author1");
        book.setCategory("Category1");
        book.setTitle("Januszex");

        Book book1 = new Book();
        book1.setId(2L);
        book1.setAuthor("Author1");
        book1.setCategory("Category1");
        book1.setTitle("Tomaszex");

        Subscription subscription = new Subscription();
        subscription.setBookAuthor("Author1");
        subscription.setBookCategory("Category1");

        List<Book> newBooks = Arrays.asList(book, book1);

        Set<Book> matchedBooks = emailSchedule.matchBooksToSubscription(subscription, newBooks);

        assertEquals(2, matchedBooks.size());
        assertTrue(matchedBooks.contains(book));
    }

    @AfterEach
    void teardown() {
        subscriptionRepository.deleteAll();
    }
}