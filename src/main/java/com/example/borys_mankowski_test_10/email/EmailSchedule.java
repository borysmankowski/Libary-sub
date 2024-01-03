package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
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

    private final AppUserRepository appUserRepository;

    @Scheduled(cron = "${scheduled.email.notification.cron}")
    public void sendScheduledEmailNotification() {
        LocalDate todayDateTime = LocalDate.now();
        final int pageSize = 10000;
        int page = 0;
        Pageable appUserPageable = PageRequest.of(page, pageSize);
        Page<String> appUserPage;

        do {

            appUserPage = appUserRepository.findUserEmailsForBooksAddedToday(appUserPageable);
            List<String> appUsers = appUserPage.getContent();

            for (String appUserEmail : appUsers) {
                List<Book> newBooks = bookRepository.findAllByAddedDateToday(todayDateTime, appUserPageable).getContent();
                processSubscriptions(appUserEmail, newBooks);
            }

            page++;
        } while (appUserPage.hasNext());
    }

    public void processSubscriptions(String appUserEmail, List<Book> newBooks) {
        int batchSize = 100;
        int page = 0;
        Pageable subscriptionPageable = PageRequest.of(page, batchSize);
        Page<Subscription> subscriptionPage;

        List<Book> matchedBooks = new ArrayList<>();

        do {
            subscriptionPage = subscriptionRepository.findAllByAppUser_Email(appUserEmail, subscriptionPageable);
            List<Subscription> subscriptions = subscriptionPage.getContent();

            for (Subscription subscription : subscriptions) {
                Set<Book> subscriptionMatchedBooks = matchBooksToSubscription(subscription, newBooks);
                matchedBooks.addAll(subscriptionMatchedBooks);
            }

            subscriptionPageable = subscriptionPage.nextPageable();
            page++;
        } while (subscriptionPage.hasNext());

        if (!matchedBooks.isEmpty()) {
            emailService.sendNotificationIfNewBooks(appUserEmail, new ArrayList<>(matchedBooks));
        }
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




