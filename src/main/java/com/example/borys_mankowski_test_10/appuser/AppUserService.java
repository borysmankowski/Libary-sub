package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.appuser.model.AppUserDto;
import com.example.borys_mankowski_test_10.appuser.model.AppUserMapper;
import com.example.borys_mankowski_test_10.appuser.model.CreateAppUserCommand;
import com.example.borys_mankowski_test_10.email.EmailService;
import com.example.borys_mankowski_test_10.exception.DuplicateResourceException;
import com.example.borys_mankowski_test_10.exception.EmailException;
import com.example.borys_mankowski_test_10.exception.ResourceNotFoundException;
import com.example.borys_mankowski_test_10.exception.UserEnablingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final EmailService emailService;


    public AppUserDto registerAppUser(CreateAppUserCommand createAppUserCommand) {

        String token = UUID.randomUUID().toString();
        AppUser appUser = appUserMapper.fromDto(createAppUserCommand);
        appUser.setConfirmationToken(token);
        appUserRepository.save(appUser);

        try {
            emailService.sendConfirmationEmail(appUser.getEmail(), "Email Confirmation", token);

        } catch (Exception e) {
            throw new EmailException(e.getMessage());
        }
        return appUserMapper.toDTO(appUser);
    }

    @Transactional
    public void confirmToken(String token) {

        if (token.isEmpty()) {
            throw new ResourceNotFoundException("Token cannot be found!");
        }

        AppUser appUserFoundByToken = appUserRepository.findAppUserByConfirmationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for the given token"));

        if (appUserFoundByToken.isEnabled()) {
            throw new DuplicateResourceException("User already enabled!");
        } else {
            appUserFoundByToken.setEnabled(true);
        }

        try {
            enableAppUser(appUserFoundByToken.getEmail());
        } catch (Exception e) {
            throw new UserEnablingException(e.getMessage());
        }
    }

    @Transactional
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    @Transactional(readOnly = true)
    public AppUser findAppUserBySubscriptionsId(Long id) {
        return appUserRepository.findBySubscriptionsId(id).orElseThrow(()
                -> new ResourceNotFoundException("User not found for the given subscription id"));
    }

    @Transactional(readOnly = true)
    public Page<AppUserDto> getAllUsers(Pageable pageable) {
        Page<AppUser> customers = appUserRepository.findAll(pageable);
        return customers.map(appUserMapper::toDTO);
    }
}
