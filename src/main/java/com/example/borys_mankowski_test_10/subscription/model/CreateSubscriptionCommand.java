package com.example.borys_mankowski_test_10.subscription.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
public class CreateSubscriptionCommand {

    @ExistingAppUser
    private Long appUserId;

    private String author;

    private String category;

}
