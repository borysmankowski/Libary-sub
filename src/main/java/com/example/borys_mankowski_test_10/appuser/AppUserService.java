package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.appuser.model.AppUserDto;
import com.example.borys_mankowski_test_10.appuser.model.AppUserMapper;
import com.example.borys_mankowski_test_10.appuser.model.CreateAppUserCommand;
import com.example.borys_mankowski_test_10.email.EmailService;
import com.example.borys_mankowski_test_10.email.EmailValidator;
import com.example.borys_mankowski_test_10.exception.DatabaseException;
import com.example.borys_mankowski_test_10.exception.DuplicateResourceException;
import com.example.borys_mankowski_test_10.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final EmailService emailService;
    private final EmailValidator emailValidator;


    public AppUserDto registerAppUser(CreateAppUserCommand createAppUserCommand) {


        if (appUserRepository.existsByEmail(createAppUserCommand.getEmail())) {
            throw new DuplicateResourceException("Email is already in use! Use a different email to register");
        }

        String token = UUID.randomUUID().toString();
        AppUser appUser = appUserMapper.fromDto(createAppUserCommand);
        appUser.setConfirmationToken(token);
        appUser.setAppUserRole(AppUserRole.CLIENT);

        try {

            appUserRepository.save(appUser);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("User with those details already exists!");
        }

        try {

            emailService.sendConfirmationEmail(appUser.getEmail(), "Email Confirmation", token);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return appUserMapper.toDTO(appUser);
    }

    @Transactional
    public void confirmToken(String token) {

        if (token.isEmpty()) {
            throw new ResourceNotFoundException("Token cannot be found!");
        }

        Optional<AppUser> appUserFoundByToken = appUserRepository.findAppUserByConfirmationToken(token);

        AppUser appUser = appUserFoundByToken.get();

        if (appUser.isEnabled()) {
            throw new DuplicateResourceException("User already enabled!");
        } else {
            appUser.setEnabled(true);

        }

        try {
            enableAppUser(appUser.getEmail());
        } catch (Exception exception) {
            throw new DatabaseException("Error occured when saving the user");
        }
    }

    @Transactional
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public Optional<AppUser> findAppUserBySubscriptionsId(Long id) {
        return appUserRepository.findAppUserBySubscriptionsId(id);
    }

    public Page<AppUserDto> getAllUsers(Pageable pageable) {
        Page<AppUser> customers = appUserRepository.findAll(pageable);
        return customers.map(appUserMapper::toDTO);
    }
}
