package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.book.BookRepository;
import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.subscription.SubscriptionRepository;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
    private EmailService emailService;


    @AfterEach
    void teardown() {
        subscriptionRepository.deleteAll();
    }


    @Test
    public void testNotifyUserWithNewBooks() {
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

        when(bookRepository.findAllAddedTodayMatchingUserSubscriptions(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(book)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        emailSchedule.notifyUser(appUser);

        verify(emailService, times(1)).sendNotificationIfNewBooks(eq("test@example.com"), eq(Collections.singletonList(book)));
    }

    @Test
    public void testNotifyUserWithoutNewBooks() {

        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setEmail("test@example.com");

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Antek");
        book.setTitle("Nowak");
        book.setAddedDate(LocalDate.now().minusDays(3));

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setBookAuthor("Antek");
        appUser.setSubscriptions(Set.of(subscription));

        when(bookRepository.findAllAddedTodayMatchingUserSubscriptions(eq(1L), any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        emailSchedule.notifyUser(appUser);

        verify(emailService, never()).sendNotificationIfNewBooks(eq("test@example.com"), eq(Collections.singletonList(book)));
    }

}
