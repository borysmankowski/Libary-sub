package com.example.borys_mankowski_test_10.subscription.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionDto {

    private Long id;
    private Long clientId;
    private String bookCategory;
    private String bookAuthor;
    private boolean subscribed;
}
