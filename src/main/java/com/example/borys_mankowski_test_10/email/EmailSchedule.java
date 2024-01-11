package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.book.BookRepository;
import com.example.borys_mankowski_test_10.book.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailSchedule {


    private final EmailService emailService;

    private final BookRepository bookRepository;

    private final AppUserRepository appUserRepository;

    @Scheduled(cron = "${scheduled.email.notification.cron}")
    public void sendScheduledEmailNotification() {
        Pageable usersPageRequest = PageRequest.of(0, 1000);
        Page<AppUser> pageOfUsersToNotify;
        do {
            pageOfUsersToNotify = appUserRepository.findUsersSubscribedForBooksAddedToday(usersPageRequest);
            for (AppUser userToNotify : pageOfUsersToNotify) {
                notifyUser(userToNotify);
            }
            usersPageRequest = usersPageRequest.next();
        } while (pageOfUsersToNotify.hasNext());
    }

    private void notifyUser(AppUser userToNotify) {
        Pageable booksPageRequest = PageRequest.of(0, 1000);
        Page<Book> pageOfBooks;
        List<Book> booksToSendInEmail = new ArrayList<>();
        do {
            pageOfBooks = bookRepository.findAllAddedTodayMatchingUserSubscriptions(userToNotify.getId(), booksPageRequest);
            booksToSendInEmail.addAll(pageOfBooks.getContent());
            booksPageRequest = booksPageRequest.next();
        } while (pageOfBooks.hasNext());

        emailService.sendNotificationIfNewBooks(userToNotify.getEmail(), booksToSendInEmail);
    }
}




