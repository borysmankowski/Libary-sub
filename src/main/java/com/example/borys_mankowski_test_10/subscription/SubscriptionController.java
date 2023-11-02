package com.example.borys_mankowski_test_10.subscription;

import com.example.borys_mankowski_test_10.subscription.model.CreateSubscriptionCommand;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody CreateSubscriptionCommand createSubscriptionCommand) {
        SubscriptionDto createdSubscription = subscriptionService.createSubscription(createSubscriptionCommand);
        return new ResponseEntity<>(createdSubscription, HttpStatus.CREATED);
    }

    @DeleteMapping("{subscriptionId}/cancel")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> deleteSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok("Subscription successfully canceled");
    }

    @GetMapping
    public ResponseEntity<Page<SubscriptionDto>> getAllSubscriptions(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions(pageRequest);
        return ResponseEntity.ok(subscriptions);
    }
}
