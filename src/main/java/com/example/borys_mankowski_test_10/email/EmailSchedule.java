package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.model.AppUserEmailBooks;
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

    @Scheduled(cron = "${scheduled.email.notification.cron}")
    public void sendScheduledEmailNotification() {
        LocalDate todayDateTime = LocalDate.now();
        final int pageSize = 10000;
        Page<Book> pageResult;
        Pageable pageable = PageRequest.of(0, pageSize);

        List<AppUserEmailBooks> appUserEmailBooksList = new ArrayList<>();

        do {
            pageResult = bookRepository.findAllByAddedDateToday(todayDateTime, pageable);
            List<Book> newBooks = pageResult.getContent();

            processSubscriptionsInBatches(newBooks, appUserEmailBooksList);

            pageable = pageResult.nextPageable();
        } while (pageResult.hasNext());

        for (AppUserEmailBooks appUserEmailBooks : appUserEmailBooksList) {
            String userEmail = appUserEmailBooks.getUserEmail();
            List<Book> matchedBooks = appUserEmailBooks.getMatchedBooks();
            emailService.sendNotificationIfNewBooks(userEmail, new ArrayList<>(matchedBooks));
        }
    }

    public void processSubscriptionsInBatches(List<Book> newBooks, List<AppUserEmailBooks> appUserEmailBooksList) {
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
                    updateUserEmailBooksList(userEmail, matchedBooks, appUserEmailBooksList);
                }
            }

            subscriptionPageable = subscriptionPage.nextPageable();
            page++;
        } while (subscriptionPage.hasNext());
    }

    public void updateUserEmailBooksList(String userEmail, Set<Book> matchedBooks, List<AppUserEmailBooks> appUserEmailBooksList) {
        for (AppUserEmailBooks appUserEmailBooks : appUserEmailBooksList) {
            if (appUserEmailBooks.getUserEmail().equals(userEmail)) {
                appUserEmailBooks.getMatchedBooks().addAll(matchedBooks);
                return;
            }
        }
        appUserEmailBooksList.add(new AppUserEmailBooks(userEmail, new ArrayList<>(matchedBooks)));
    }

    public Set<Book> matchBooksToSubscription(Subscription subscription, List<Book> newBooks) {
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


