package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.model.UserEmailBooks;
import com.example.borys_mankowski_test_10.book.BookRepository;
import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.subscription.SubscriptionRepository;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EmailSchedule {


    private final EmailService emailService;

    private final BookRepository bookRepository;

    private final SubscriptionRepository subscriptionRepository;

//    @Scheduled(cron = "${scheduled.email.notification.cron}")
//    public void sendScheduledEmailNotification() {
//        LocalDate todayDateTime = LocalDate.now();
//        final int pageSize = 10000;
//        Page<Book> pageResult;
//        Pageable pageable = PageRequest.of(0, pageSize);
//
//        do {
//            pageResult = bookRepository.findAllByAddedDateToday(todayDateTime, pageable);
//            List<Book> newBooks = pageResult.getContent();
//
//            processSubscriptionsInBatches(newBooks);
//
//            pageable = pageResult.nextPageable();
//        } while (pageResult.hasNext());
//    }
//
//    private void processSubscriptionsInBatches(List<Book> newBooks) {
//        int batchSize = 500;
//        int page = 0;
//
//        Pageable subscriptionPageable = PageRequest.of(page, batchSize);
//        Page<Subscription> subscriptionPage;
//
//        do {
//            subscriptionPage = subscriptionRepository.findAll(subscriptionPageable);
//            List<Subscription> subscriptions = subscriptionPage.getContent();
//
//            for (Subscription subscription : subscriptions) {
//                Set<Book> matchedBooks = matchBooksToSubscription(subscription, newBooks);
//                if (!matchedBooks.isEmpty()) {
//                    emailService.sendNotificationIfNewBooks(subscription.getAppUser().getEmail(), new ArrayList<>(matchedBooks));
//                }
//            }
//
//            subscriptionPageable = subscriptionPage.nextPageable();
//            page++;
//        } while (subscriptionPage.hasNext());
//    }
//
//    private Set<Book> matchBooksToSubscription(Subscription subscription, List<Book> newBooks) {
//        Set<Book> matchedBooks = new HashSet<>();
//        for (Book book : newBooks) {
//            if (book.getAuthor().equals(subscription.getBookAuthor()) ||
//                    book.getCategory().equals(subscription.getBookCategory())) {
//                matchedBooks.add(book);
//            }
//        }
//        return matchedBooks;
//    }

    @Scheduled(cron = "${scheduled.email.notification.cron}")
    public void sendScheduledEmailNotification() {
        LocalDate todayDateTime = LocalDate.now();
        final int pageSize = 10000;
        Page<Book> pageResult;
        Pageable pageable = PageRequest.of(0, pageSize);

        List<UserEmailBooks> userEmailBooksList = new ArrayList<>(); // List to store matched books for each user

        do {
            pageResult = bookRepository.findAllByAddedDateToday(todayDateTime, pageable);
            List<Book> newBooks = pageResult.getContent();

            processSubscriptionsInBatches(newBooks, userEmailBooksList);

            pageable = pageResult.nextPageable();
        } while (pageResult.hasNext());

        for (UserEmailBooks userEmailBooks : userEmailBooksList) {
            String userEmail = userEmailBooks.getUserEmail();
            List<Book> matchedBooks = userEmailBooks.getMatchedBooks();
            emailService.sendNotificationIfNewBooks(userEmail, new ArrayList<>(matchedBooks));
        }
    }

    private void processSubscriptionsInBatches(List<Book> newBooks, List<UserEmailBooks> userEmailBooksList) {
        int batchSize = 1;
        int page = 0;

        Pageable subscriptionPageable = PageRequest.of(page, batchSize);
        Page<Subscription> subscriptionPage;

        do {
            subscriptionPage = subscriptionRepository.findAll(subscriptionPageable);
            List<Subscription> subscriptions = subscriptionPage.getContent();

            for (Subscription subscription : subscriptions) {
                Set<Book> matchedBooks = matchBooksToSubscription(subscription, newBooks);

                if (!matchedBooks.isEmpty()) {
                    String userEmail = subscription.getAppUser().getEmail();
                    updateUserEmailBooksList(userEmail, matchedBooks, userEmailBooksList);
                }
            }

            subscriptionPageable = subscriptionPage.nextPageable();
            page++;
        } while (subscriptionPage.hasNext());
    }

    private void updateUserEmailBooksList(String userEmail, Set<Book> matchedBooks, List<UserEmailBooks> userEmailBooksList) {
        for (UserEmailBooks userEmailBooks : userEmailBooksList) {
            if (userEmailBooks.getUserEmail().equals(userEmail)) {
                userEmailBooks.getMatchedBooks().addAll(matchedBooks);
                return;
            }
        }
        // If the user is not found, add a new entry
        userEmailBooksList.add(new UserEmailBooks(userEmail, new ArrayList<>(matchedBooks)));
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
}


