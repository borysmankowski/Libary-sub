package com.example.borys_mankowski_test_10.subscription;


import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.exception.DuplicateResourceException;
import com.example.borys_mankowski_test_10.exception.ResourceNotFoundException;
import com.example.borys_mankowski_test_10.exception.UserEnablingException;
import com.example.borys_mankowski_test_10.subscription.model.CreateSubscriptionCommand;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionDto;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final AppUserRepository appUserRepository;

    private final SubscriptionMapper subscriptionMapper;


    @Transactional
    public SubscriptionDto createSubscription(CreateSubscriptionCommand createSubscriptionCommand) {

        AppUser appUser = appUserRepository.findByIdForLock(createSubscriptionCommand.getAppUserId())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found for clientId: " + createSubscriptionCommand.getAppUserId()));

        if (!appUser.isEnabled()) {
            throw new UserEnablingException("App user with id " + appUser.getId() + " is not enabled! Confirm Email address!");
        }

        if ((createSubscriptionCommand.getAuthor() == null || createSubscriptionCommand.getAuthor().isEmpty()) && createSubscriptionCommand.getCategory() == null) {
            throw new ResourceNotFoundException("Either author or category must be provided");
        }

        if (createSubscriptionCommand.getAuthor() != null && subscriptionRepository.existsByAppUserIdAndBookAuthor(createSubscriptionCommand.getAppUserId(), createSubscriptionCommand.getAuthor())) {
            throw new ResourceNotFoundException("Subscription with this author already exists for the customer");
        }

        if (createSubscriptionCommand.getCategory() != null && subscriptionRepository.existsByAppUserIdAndBookCategory(createSubscriptionCommand.getAppUserId(), createSubscriptionCommand.getCategory())) {
            throw new DuplicateResourceException("Subscription with this category already exists for the customer");
        }

        Subscription subscriptionNew = subscriptionMapper.fromDto(createSubscriptionCommand);

        appUser.addSubscription(subscriptionNew);

        subscriptionNew.setSubscribed(true);

        return subscriptionMapper.mapToDto(subscriptionRepository.save(subscriptionNew));
    }


    @Transactional
    public void cancelSubscription(Long subscriptionId) {

        Subscription subscription = (subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscripton of id " + subscriptionId + " doesnt exist")));

        subscription.getAppUser().removeSubscription(subscription);

        subscriptionRepository.deleteById(subscription.getId());
    }

    @Transactional(readOnly = true)
    public Page<SubscriptionDto> getAllSubscriptions(Pageable pageable) {
        Page<Subscription> subscriptions = subscriptionRepository.findAll(pageable);
        return subscriptions.map(subscriptionMapper::mapToDto);
    }
}
