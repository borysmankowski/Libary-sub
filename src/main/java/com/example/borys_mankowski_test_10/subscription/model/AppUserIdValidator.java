package com.example.borys_mankowski_test_10.subscription.model;


import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class AppUserIdValidator implements ConstraintValidator<ExistingAppUser, Long> {

    private final AppUserRepository appUserRepository;

    @Override
    public void initialize(ExistingAppUser constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long appUserId, ConstraintValidatorContext context) {
        return appUserId != null && appUserRepository.existsById(appUserId);
    }
}

