package com.example.borys_mankowski_test_10.appuser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private boolean enabled;
}
