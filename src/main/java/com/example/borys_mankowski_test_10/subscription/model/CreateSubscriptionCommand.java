package com.example.borys_mankowski_test_10.subscription.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateSubscriptionCommand {

    @NotNull(message = "Client Id cannot be null! ")
    private Long clientId;

    private String author;

    private String category;

}
