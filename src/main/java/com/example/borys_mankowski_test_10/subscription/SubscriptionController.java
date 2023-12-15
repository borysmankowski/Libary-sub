package com.example.borys_mankowski_test_10.subscription;

import com.example.borys_mankowski_test_10.subscription.model.CreateSubscriptionCommand;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionDto;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionMessage;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody @Valid CreateSubscriptionCommand createSubscriptionCommand) {
        SubscriptionDto createdSubscription = subscriptionService.createSubscription(createSubscriptionCommand);
        return new ResponseEntity<>(createdSubscription, HttpStatus.CREATED);
    }

    @DeleteMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SubscriptionMessage> deleteSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok(new SubscriptionMessage("Subscription successfully canceled"));
    }

    @GetMapping
    public ResponseEntity<Page<SubscriptionDto>> getAllSubscriptions(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions(pageable);
        return ResponseEntity.ok(subscriptions);
    }
}
