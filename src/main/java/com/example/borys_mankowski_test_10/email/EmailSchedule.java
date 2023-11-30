package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.AppUserService;
import com.example.borys_mankowski_test_10.book.BookRepository;
import com.example.borys_mankowski_test_10.book.BookService;
import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.subscription.SubscriptionRepository;
import com.example.borys_mankowski_test_10.subscription.SubscriptionService;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class EmailSchedule {


    private EmailService emailService;

    private BookRepository bookRepository;

    private SubscriptionRepository subscriptionRepository;

    private SubscriptionService subscriptionService;

    private BookService bookService;

    private AppUserService appUserService;

    @Scheduled(cron = "${scheduled.email.notification.cron}")
    public void sendScheduledEmailNotification() {
        LocalDate todayDateTime = LocalDate.now();
        final int pageSize = 10000;
        Page<Book> pageResult;
        Pageable pageable = PageRequest.of(0, pageSize);

        do {
            pageResult = bookRepository.findAllByAddedDateToday(todayDateTime, pageable);
            List<Book> newBooks = pageResult.getContent();

            processSubscriptionsInBatches(newBooks);

            pageable = pageResult.nextPageable();
        } while (pageResult.hasNext());
    }

    private void processSubscriptionsInBatches(List<Book> newBooks) {
        int batchSize = 500;
        int page = 0;

        Pageable subscriptionPageable = PageRequest.of(page, batchSize);
        Page<Subscription> subscriptionPage;

        do {
            subscriptionPage = subscriptionRepository.findAll(subscriptionPageable);
            List<Subscription> subscriptions = subscriptionPage.getContent();

            for (Subscription subscription : subscriptions) {
                Set<Book> matchedBooks = matchBooksToSubscription(subscription, newBooks);
                if (!matchedBooks.isEmpty()) {
                    emailService.sendNotificationIfNewBooks(subscription.getAppUser().getEmail(), new ArrayList<>(matchedBooks));
                }
            }

            subscriptionPageable = subscriptionPage.nextPageable();
            page++;
        } while (subscriptionPage.hasNext());
    }

    private Set<Book> matchBooksToSubscription(Subscription subscription, List<Book> newBooks) {
        Set<Book> matchedBooks = new HashSet<>();
        for (Book book : newBooks) {
            if (book.getAuthor().equals(subscription.getBookAuthor()) ||
                    book.getCategory().equals(subscription.getBookCategory())) {
                matchedBooks.add(book);
            }
        }
        return matchedBooks;
    }


//    @Scheduled(cron = "0 0 0 * * *")
//    public void sendScheduledEmailNotification() {
//
//        LocalDate today = LocalDate.now();
//        final int pageSize = 10000;
//        Page<Book> pageResult;
//        Pageable pageable = PageRequest.of(0, pageSize);
//
//        List<Book> booksAddedToday = bookRepository.findAllByAddedDateToday();
//        Map<String, List<Book>> subscribersMap = new HashMap<>();
//
//        for (Book book : booksAddedToday) {
//            Pageable pageable = PageRequest.of(page, pageSize);
//            List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByAuthorOrCategory(
//                    book.getAuthor(), book.getCategory(), pageable);
//            for (Subscription subscription : subscriptions) {
//
//                subscribersMap
//                        .computeIfAbsent(subscription.getAppUser().getEmail(), k -> new ArrayList<>())
//                        .add(book);
//            }
//        }
//        for (Map.Entry<String, List<Book>> entry : subscribersMap.entrySet()) {
//            emailService.sendNotificationIfNewBooks(entry.getKey(), entry.getValue());
//        }
//    }

//    @Scheduled(cron = "${scheduled.email.notification.cron}")
//    public void sendScheduledEmailNotification() {
//
//        int page = 0;
//        int pageSize = 1000;
//
//        Page<SubscriptionDto> subscriptionDtoPage;
//
//        do {
//            Pageable pageable = PageRequest.of(page, pageSize);
//            subscriptionDtoPage = subscriptionService.getAllSubscriptions(pageable);
//
//            for (SubscriptionDto subscriptionDto : subscriptionDtoPage.getContent()) {
//                List<BookDto> newBooks;
//                if (subscriptionDto.getBookAuthor() != null) {
//                    newBooks = bookService.findBooksByAuthor(subscriptionDto.getBookAuthor());
//                } else if (subscriptionDto.getBookCategory() != null) {
//                    newBooks = bookService.findBooksByCategory(subscriptionDto.getBookCategory());
//                } else {
//                    continue;
//                }
//                if (!newBooks.isEmpty()) {
//                    AppUser appUser = appUserService.findAppUserBySubscriptionsId(subscriptionDto.getId());
//                    emailService.sendNotificationIfNewBooks(appUser.getEmail(), newBooks);
//                }
//            }
//            page++;
//        } while (subscriptionDtoPage.hasNext());
//    }
}


