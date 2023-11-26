package com.example.borys_mankowski_test_10.subscription.model;


import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SubscriptionMapper {

    private final AppUserRepository appUserRepository;

    public SubscriptionDto mapToDto(Subscription subscription) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setBookAuthor(subscription.getBookAuthor());
        dto.setBookCategory(subscription.getBookCategory());
        dto.setAppUserId(subscription.getAppUser().getId());
        dto.setSubscribed(subscription.isSubscribed());
        return dto;
    }

    public Subscription fromDto(CreateSubscriptionCommand command) {
        Subscription subscription = new Subscription();

        AppUser appUser = appUserRepository.findByIdForLock(command.getAppUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id {0} has been not found!" + command.getAppUserId()));

        subscription.setAppUser(appUser);
        subscription.setBookAuthor(command.getAuthor());
        subscription.setBookCategory(command.getCategory());
        return subscription;
    }
}
