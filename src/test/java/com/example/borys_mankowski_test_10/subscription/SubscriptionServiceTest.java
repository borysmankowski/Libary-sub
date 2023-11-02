package com.example.borys_mankowski_test_10.subscription;

import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.subscription.model.CreateSubscriptionCommand;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionDto;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SubscriptionServiceTest {
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper = new SubscriptionMapper();
    private SubscriptionService subscriptionService;

    @BeforeEach
    void init() {
        subscriptionService = new SubscriptionService(subscriptionRepository, appUserRepository, subscriptionMapper);
    }

    @Test
    void testCreateSubscription() {
        CreateSubscriptionCommand createSubscriptionCommand = new CreateSubscriptionCommand();
        createSubscriptionCommand.setClientId(1L);
        createSubscriptionCommand.setCategory("Comedy");
        createSubscriptionCommand.setAuthor("Tomasz");

        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setEnabled(true);

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Tomasz");
        book.setCategory("Comedy");
        book.setTitle("Januszex");
        book.setAvailable(true);

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setAppUser(appUser);
        subscription.setBookAuthor(book.getAuthor());
        subscription.setBookCategory(book.getCategory());

        SubscriptionDto expectedRentalDto = new SubscriptionDto();
        expectedRentalDto.setId(subscription.getId());
        expectedRentalDto.setClientId(subscription.getAppUser().getId());

        Mockito.when(appUserRepository.findById(1L)).thenReturn(Optional.of(appUser));
        Mockito.when(subscriptionRepository.save(Mockito.any(Subscription.class))).thenReturn(subscription);

        SubscriptionDto actualRentalDto = subscriptionService.createSubscription(createSubscriptionCommand);

        Assertions.assertEquals(expectedRentalDto.getId(), actualRentalDto.getId());
        Assertions.assertEquals(expectedRentalDto.getClientId(), actualRentalDto.getClientId());
    }

//    @Test
//    void testCancelSubscriptionSuccess() {
//        Long subscriptionId = 1L;
//        Subscription subscription = new Subscription();
//        subscription.setId(subscriptionId);
//
//        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
//
//        ResponseEntity<String> response = subscriptionService.cancelSubscription(subscriptionId);
//
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Subscription successfully canceled", response.getBody());
//
//        verify(subscriptionRepository, times(1)).deleteById(subscriptionId);
//    }
}