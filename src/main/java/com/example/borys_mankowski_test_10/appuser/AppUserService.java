package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.appuser.model.AppUserDto;
import com.example.borys_mankowski_test_10.appuser.model.AppUserMapper;
import com.example.borys_mankowski_test_10.appuser.model.CreateAppUserCommand;
import com.example.borys_mankowski_test_10.email.EmailService;
import com.example.borys_mankowski_test_10.exception.DuplicateResourceException;
import com.example.borys_mankowski_test_10.exception.ResourceNotFoundException;
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

        emailService.sendConfirmationEmail(appUser.getEmail(), "Email Confirmation", token);

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
    }

    @Transactional(readOnly = true)
    public Page<AppUserDto> getAllUsers(Pageable pageable) {
        Page<AppUser> customers = appUserRepository.findAll(pageable);
        return customers.map(appUserMapper::toDTO);
    }
}
