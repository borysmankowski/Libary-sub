package com.example.borys_mankowski_test_10.appuser.model;

import com.example.borys_mankowski_test_10.appuser.AppUserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class CreateAppUserCommand {

    @NotBlank(message = "FirstName cannot be blank")
    private String firstName;

    @NotBlank(message = "LastName cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

}
