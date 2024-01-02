package com.example.borys_mankowski_test_10.subscription.model;

import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
public class CreateSubscriptionCommand {

    @NotNull(message = "App User Id cannot be null! ")
    private Long appUserId;

    private String author;

    private String category;

}
