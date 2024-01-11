package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
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
        int appUserBatchSize = 1000;
        int bookBatchSize = 1000;

        int appUserPageNumber = 0;
        int bookPageNumber;

        Page<AppUser> appUsers;
        do {
            Pageable appUserPageable = PageRequest.of(appUserPageNumber, appUserBatchSize);
            appUsers = appUserRepository.findUsersSubscribedForBooksAddedToday(todayDateTime, appUserPageable);

            for (AppUser appUser : appUsers) {
                bookPageNumber = 0;
                Pageable bookPageable = PageRequest.of(bookPageNumber, bookBatchSize);

                Page<Subscription> subscriptions = subscriptionRepository.findAllByAppUser_Email(appUser.getEmail(), appUserPageable);

                for (Subscription subscription : subscriptions) {
                    Page<Book> newBooks = bookRepository.findAllByAddedDateAndCategoryOrAuthor(
                            todayDateTime,
                            subscription.getBookCategory(),
                            subscription.getBookAuthor(),
                            bookPageable
                    );

                    while (newBooks.hasContent()) {
                        emailService.sendNotificationIfNewBooks(appUser.getEmail(), newBooks.getContent());
                        bookPageNumber++;
                        bookPageable = PageRequest.of(bookPageNumber, bookBatchSize);
                        newBooks = bookRepository.findAllByAddedDateAndCategoryOrAuthor(
                                todayDateTime,
                                subscription.getBookCategory(),
                                subscription.getBookAuthor(),
                                bookPageable
                        );
                    }
                }
            }
            appUserPageNumber++;

        } while (!appUsers.isEmpty());
    }
}




