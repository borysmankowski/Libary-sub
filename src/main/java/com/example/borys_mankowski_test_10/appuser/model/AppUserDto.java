package com.example.borys_mankowski_test_10.appuser.model;

import com.example.borys_mankowski_test_10.appuser.AppUserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AppUserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private boolean enabled;
}
