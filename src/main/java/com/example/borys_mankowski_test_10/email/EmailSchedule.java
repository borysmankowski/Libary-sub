package com.example.borys_mankowski_test_10.email;

import com.example.borys_mankowski_test_10.appuser.AppUserService;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.book.BookRepository;
import com.example.borys_mankowski_test_10.book.BookService;
import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.book.model.BookDto;
import com.example.borys_mankowski_test_10.subscription.SubscriptionRepository;
import com.example.borys_mankowski_test_10.subscription.SubscriptionService;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
public class EmailSchedule {


    private EmailService emailService;

//    private BookRepository bookRepository;
//
//    private SubscriptionRepository subscriptionRepository;

    private SubscriptionService subscriptionService;

    private BookService bookService;

    private AppUserService appUserService;


//    @Scheduled(cron = "0 0 0 * * *")
//    public void sendScheduledEmailNotification() {
//
//        int page = 0;
//        int pageSize = 1000;
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

    @Scheduled(cron = "1 * * * * *")
    public void sendScheduledEmailNotification() {

        int page = 0;
        int pageSize = 1000;

        Page<SubscriptionDto> subscriptionDtoPage;

        do{
            Pageable pageable = PageRequest.of(page,pageSize);
            subscriptionDtoPage = subscriptionService.getAllSubscriptions(pageable);

            for (SubscriptionDto subscriptionDto : subscriptionDtoPage.getContent()){
                List<BookDto> newBooks;
                if(subscriptionDto.getBookAuthor() != null){
                    newBooks = bookService.findBooksByAuthor(subscriptionDto.getBookAuthor());
                } else if (subscriptionDto.getBookCategory() != null) {
                    newBooks = bookService.findBooksByCategory(subscriptionDto.getBookCategory());
                } else {
                    continue;
                }
                if (!newBooks.isEmpty()){
                    Optional<AppUser> appUser = appUserService.findAppUserBySubscriptionsId(subscriptionDto.getId());
                    emailService.sendNotificationIfNewBooks(appUser.get().getEmail(),newBooks);
                }
            }
            page++;
        }while (subscriptionDtoPage.hasNext());
    }
}


