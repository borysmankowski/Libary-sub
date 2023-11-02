package com.example.borys_mankowski_test_10.subscription.model;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appUser_Id")
    private AppUser appUser;


    private String bookCategory;


    private String bookAuthor;

    private boolean subscribed;

    private boolean notificationSent;

    @Version
    private int version;
}
