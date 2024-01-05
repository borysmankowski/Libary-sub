package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.book.BookRepository;
import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.subscription.SubscriptionRepository;
import com.example.borys_mankowski_test_10.subscription.model.AppUserIdValidator;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailScheduleTest {

    @InjectMocks
    private EmailSchedule emailSchedule;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private EmailService emailService;


    @AfterEach
    void teardown() {
        subscriptionRepository.deleteAll();
    }


    @Test
    public void testSendScheduledEmailNotificationWithNewBooks() {
        LocalDate todayDateTime = LocalDate.now();

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Tomasz");
        book.setCategory("Comedy");
        book.setTitle("Januszex");
        book.setAddedDate(todayDateTime);

        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setFirstName("Jan");
        appUser.setLastName("test");
        appUser.setEmail("test@example.com");

        Subscription subscription = new Subscription();
        subscription.setAppUser(appUser);
        subscription.setBookAuthor("jan");
        subscription.setBookCategory("old");

        when(appUserRepository.findUsersForBooksAddedToday(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(appUser)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        when(subscriptionRepository.findAllByAppUser_Email(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(subscription)));

        when(bookRepository.findAllByAddedDateAndCategoryOrAuthor(any(LocalDate.class), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(book)));

        emailSchedule.sendScheduledEmailNotification();

        verify(emailService, times(1)).sendNotificationIfNewBooks(eq("test@example.com"), eq(Collections.singletonList(book)));
    }

    @Test
    public void testSendScheduledEmailNotificationWithoutNewBooks() {

        when(appUserRepository.findUsersForBooksAddedToday(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        emailSchedule.sendScheduledEmailNotification();

        verify(emailService, never()).sendNotificationIfNewBooks(anyString(), anyList());
    }

}
