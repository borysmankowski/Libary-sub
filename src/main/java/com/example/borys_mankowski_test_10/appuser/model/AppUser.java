package com.example.borys_mankowski_test_10.appuser.model;

import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Email
    @Column(unique = true)
    private String email;

    private String confirmationToken;
    private String password;

    private boolean enabled;

    @Version
    private Long version;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.REMOVE)
    private Set<Subscription> subscriptions;

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.setAppUser(this);
    }

}
