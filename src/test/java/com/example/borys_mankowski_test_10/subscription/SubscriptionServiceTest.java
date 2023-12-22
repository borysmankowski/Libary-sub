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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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

    @InjectMocks
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

    @Test
    public void cancelSubscription_cancelsExistingSubscription() {
        Long subscriptionId = 1L;
        Subscription existingSubscription = mock(Subscription.class);
        AppUser appUser = mock(AppUser.class);

        when(existingSubscription.getAppUser()).thenReturn(appUser);
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(existingSubscription));

        subscriptionService.cancelSubscription(subscriptionId);

        verify(appUser).removeSubscription(existingSubscription);
        verify(subscriptionRepository).deleteById(existingSubscription.getId());
    }


    @Test
    public void getAllSubscriptions_returnsExpectedSubscriptions() {
        Pageable pageable = PageRequest.of(0, 5);
        Subscription subscription = new Subscription();
        List<Subscription> subscriptionList = Arrays.asList(subscription);

        Page<Subscription> subscriptionPage = new PageImpl<>(subscriptionList);
        SubscriptionDto subscriptionDto = new SubscriptionDto();

        when(subscriptionRepository.findAll(pageable)).thenReturn(subscriptionPage);
        when(subscriptionMapper.mapToDto(subscription)).thenReturn(subscriptionDto);

        Page<SubscriptionDto> result = subscriptionService.getAllSubscriptions(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(subscriptionDto, result.getContent().get(0));

        verify(subscriptionRepository, times(1)).findAll(pageable);
    }

}