package com.example.borys_mankowski_test_10.subscription;

import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.subscription.model.CreateSubscriptionCommand;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionDto;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionMapper subscriptionMapper;
    private SubscriptionService subscriptionService;

    @Captor
    private ArgumentCaptor<Subscription> subscriptionCaptor;

    @BeforeEach
    void init() {
        subscriptionService = new SubscriptionService(subscriptionRepository, appUserRepository, subscriptionMapper);
    }

    @Test
    void testCreateSubscription() {
        CreateSubscriptionCommand createSubscriptionCommand = new CreateSubscriptionCommand();
        createSubscriptionCommand.setAppUserId(1L);
        createSubscriptionCommand.setCategory("Comedy");
        createSubscriptionCommand.setAuthor("Tomasz");


        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Tomasz");
        book.setCategory("Comedy");
        book.setTitle("Januszex");

        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setEmail("john.doe@example.com");
        appUser.setConfirmationToken("token");
        appUser.setEnabled(true);
        appUser.setSubscriptions(new HashSet<>());

        Subscription expectedSubscription = new Subscription();
        expectedSubscription.setAppUser(appUser);
        expectedSubscription.setBookAuthor(book.getAuthor());
        expectedSubscription.setBookCategory(book.getCategory());
        expectedSubscription.setVersion(1);

        SubscriptionDto expectedSubscriptionDTO = new SubscriptionDto();

        when(appUserRepository.findByIdForLock(createSubscriptionCommand.getAppUserId())).thenReturn(Optional.of(appUser));
        when(subscriptionMapper.fromDto(createSubscriptionCommand)).thenReturn(expectedSubscription);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(expectedSubscription);
        when(subscriptionMapper.mapToDto(any(Subscription.class))).thenReturn(expectedSubscriptionDTO);

        SubscriptionDto result = subscriptionService.createSubscription(createSubscriptionCommand);

        assertNotNull(result);
        assertEquals(expectedSubscriptionDTO, result);

        verify(subscriptionRepository).save(subscriptionCaptor.capture());

        Subscription savedSubscription = subscriptionCaptor.getValue();

        assertEquals(createSubscriptionCommand.getAuthor(), savedSubscription.getBookAuthor());
        assertEquals(createSubscriptionCommand.getAppUserId(), savedSubscription.getAppUser().getId());
        assertEquals(createSubscriptionCommand.getCategory(), savedSubscription.getBookCategory());
        assertNotNull(savedSubscription.getVersion());

    }

}