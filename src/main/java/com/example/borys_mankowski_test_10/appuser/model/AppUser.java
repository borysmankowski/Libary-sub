package com.example.borys_mankowski_test_10.appuser.model;

import com.example.borys_mankowski_test_10.appuser.AppUserRole;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "app_user"
)

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Email // TODO: 26/11/2023 check if there is an exception which will be thrown if the email is repeated
    @Column(unique = true)
    private String email;

    private String confirmationToken;
    private String password;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    @Version
    private Long version;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.REMOVE)
    private Set<Subscription> subscriptions;

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.setAppUser(this);
    }

}
