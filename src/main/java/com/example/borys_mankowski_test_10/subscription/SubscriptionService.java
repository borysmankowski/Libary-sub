package com.example.borys_mankowski_test_10.subscription;


import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.exception.DatabaseException;
import com.example.borys_mankowski_test_10.exception.DuplicateResourceException;
import com.example.borys_mankowski_test_10.exception.ResourceNotFoundException;
import com.example.borys_mankowski_test_10.subscription.model.CreateSubscriptionCommand;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionDto;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final AppUserRepository appUserRepository;

    private final SubscriptionMapper subscriptionMapper;


    @Transactional
    public SubscriptionDto createSubscription(CreateSubscriptionCommand createSubscriptionCommand) {

        AppUser appUser = appUserRepository.findById(createSubscriptionCommand.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found for clientId: " + createSubscriptionCommand.getClientId()));

        if (!appUser.isEnabled()) {
            throw new IllegalStateException("App user with id " + appUser.getFirstName() + " is not enabled! Confirm Email address!");
        }

        if ((createSubscriptionCommand.getAuthor() == null || createSubscriptionCommand.getAuthor().isEmpty()) && createSubscriptionCommand.getCategory() == null) {
            throw new ResourceNotFoundException("Either author or category must be provided");
        }

        if (createSubscriptionCommand.getAuthor() != null && subscriptionRepository.existsByAppUserIdAndBookAuthor(createSubscriptionCommand.getClientId(), createSubscriptionCommand.getAuthor())) {
            throw new ResourceNotFoundException("Subscription with this author already exists for the customer");
        }

        if (createSubscriptionCommand.getCategory() != null && subscriptionRepository.existsByAppUserIdAndBookCategory(createSubscriptionCommand.getClientId(), createSubscriptionCommand.getCategory())) {
            throw new DuplicateResourceException("Subscription with this category already exists for the customer");
        }

        Subscription subscriptionNew;

        subscriptionNew = subscriptionMapper.fromDto(createSubscriptionCommand, appUser);

        Set<Subscription> currentSubscriptions = appUser.getSubscriptions();

        currentSubscriptions.add(subscriptionNew);


        appUser.setSubscriptions(currentSubscriptions);
        subscriptionNew.setSubscribed(true);

        try {
            subscriptionNew = subscriptionRepository.save(subscriptionNew);
            return subscriptionMapper.mapToDto(subscriptionNew);

        } catch (DataIntegrityViolationException dive) {
            throw new DuplicateResourceException("A book with this author and title already exists.");

        } catch (Exception e) {
            throw new DatabaseException("An error occurred while trying to save the book");
        }
    }


    @Transactional
    public void cancelSubscription(Long subscriptionId) {

        Subscription subscription = (subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscripton of id" + subscriptionId + " doesnt exist")));

        subscriptionRepository.deleteById(subscription.getId());
    }

    public Page<SubscriptionDto> getAllSubscriptions(Pageable pageable) {
        Page<Subscription> subscriptions = subscriptionRepository.findAll(pageable);
        return subscriptions.map(subscriptionMapper::mapToDto);
    }


}
