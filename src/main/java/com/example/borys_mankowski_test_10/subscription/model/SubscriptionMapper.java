package com.example.borys_mankowski_test_10.subscription.model;


import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SubscriptionMapper {

    public SubscriptionDto mapToDto(Subscription subscription) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setBookAuthor(subscription.getBookAuthor());
        dto.setBookCategory(subscription.getBookCategory());
        dto.setClientId(subscription.getAppUser().getId());
        dto.setSubscribed(subscription.isSubscribed());
        return dto;
    }

    public Subscription fromDto(CreateSubscriptionCommand command, AppUser appUser) {
        Subscription subscription = new Subscription();
        subscription.setAppUser(appUser);
        subscription.setBookAuthor(command.getAuthor());
        subscription.setBookCategory(command.getCategory());
        return subscription;
    }
}
