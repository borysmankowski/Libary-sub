package com.example.borys_mankowski_test_10.appuser.model;

import com.example.borys_mankowski_test_10.appuser.AppUserRole;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"})
        }
)

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Email

    private String email;

    private String confirmationToken;
    private String password;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private boolean locked;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.REMOVE)
    private Set<Subscription> subscriptions;

}
