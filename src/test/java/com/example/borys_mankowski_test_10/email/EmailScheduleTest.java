//package com.example.borys_mankowski_test_10.email;
//
//import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
//import com.example.borys_mankowski_test_10.appuser.model.AppUser;
//import com.example.borys_mankowski_test_10.book.BookRepository;
//import com.example.borys_mankowski_test_10.book.model.Book;
//import com.example.borys_mankowski_test_10.subscription.SubscriptionRepository;
//import com.example.borys_mankowski_test_10.subscription.model.Subscription;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.atLeastOnce;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class EmailScheduleTest {
//
//    @InjectMocks
//    private EmailSchedule emailSchedule;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private SubscriptionRepository subscriptionRepository;
//
//    @Mock
//    private AppUserRepository appUserRepository;
//
//    @Test
//    public void testSendScheduledEmailNotification() {
//        // Define the batch size
//        final int batchSize = 10000;
//
//        // Create a sample book
//        Book book = new Book();
//        book.setId(1L);
//        book.setAuthor("Tomasz");
//        book.setCategory("Comedy");
//        book.setTitle("Januszex");
//        book.setAddedDate(LocalDate.now());
//
//        int page = 0;
//
//        // Mock the behavior of appUserRepository
//        when(appUserRepository.findUserEmailsForBooksAddedToday(any(Pageable.class)))
//                .thenReturn(new PageImpl<>(Collections.singletonList("test@example.com")));
//
//        // Mock the behavior of bookRepository
//        when(bookRepository.findAllByAddedDateToday(any(LocalDate.class), any(Pageable.class)))
//                .thenReturn(new PageImpl<>(Collections.singletonList(book)));
//
//        // Mock the behavior of subscriptionRepository
//        Pageable subscriptionPageable = PageRequest.of(page, batchSize);
//        Page<Subscription> subscriptionPage = new PageImpl<>(Collections.emptyList()); // Provide a non-null value
//        when(subscriptionRepository.findAllByAppUser_Email(anyString(), any(Pageable.class)))
//                .thenReturn(subscriptionPage);
//
//        // Call the scheduled method
//        emailSchedule.sendScheduledEmailNotification();
//
//        // Verify that processSubscriptions was called with the correct arguments
//        verify(emailSchedule, times(1)).processSubscriptions(eq("test@example.com"), eq(Collections.singletonList(book)));
//    }
//
//
////    @Test
////    public void testProcessSubscriptionsInBatches() {
////
////        Book book = new Book();
////        book.setId(1L);
////        book.setAuthor("Tomasz");
////        book.setCategory("Comedy");
////        book.setTitle("Januszex");
////
////        Book book1 = new Book();
////        book1.setId(2L);
////        book1.setAuthor("Janusz");
////        book1.setCategory("Horror");
////        book1.setTitle("Tomaszex");
////
////        AppUser appUser = new AppUser();
////        appUser.setId(1L);
////        appUser.setEmail("test@example.com");
////
////
////        when(subscriptionRepository.findAllByAppUser_Email(any(String.class), any(Pageable.class)))
////                .thenReturn(new PageImpl<>(Collections.emptyList()));
////
////        List<Book> newBooks = Arrays.asList(book, book1);
////
////        emailSchedule.processSubscriptions(appUser.getEmail(), newBooks);
////
////        verify(subscriptionRepository, atLeastOnce()).findAllByAppUser_Email(any(String.class), any(Pageable.class));
////
////    }
//
//    @Test
//    public void testMatchBooksToSubscription() {
//
//        Book book = new Book();
//        book.setId(1L);
//        book.setAuthor("Author1");
//        book.setCategory("Category1");
//        book.setTitle("Januszex");
//
//        Book book1 = new Book();
//        book1.setId(2L);
//        book1.setAuthor("Author1");
//        book1.setCategory("Category1");
//        book1.setTitle("Tomaszex");
//
//        Subscription subscription = new Subscription();
//        subscription.setBookAuthor("Author1");
//        subscription.setBookCategory("Category1");
//
//        List<Book> newBooks = Arrays.asList(book, book1);
//
//        Set<Book> matchedBooks = emailSchedule.matchBooksToSubscription(subscription, newBooks);
//
//        assertEquals(2, matchedBooks.size());
//        assertTrue(matchedBooks.contains(book));
//    }
//
//    @AfterEach
//    void teardown() {
//        subscriptionRepository.deleteAll();
//    }
//}