package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.email.EmailService;
import com.example.borys_mankowski_test_10.exception.DatabaseException;
import com.example.borys_mankowski_test_10.exception.DuplicateResourceException;
import com.example.borys_mankowski_test_10.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Autowired
    private EmailService emailService;


    @Test
    void enableAppUser() {

        when(appUserRepository.enableAppUser("user@example.com")).thenReturn(1);

        int result = appUserService.enableAppUser("user@example.com");

        verify(appUserRepository).enableAppUser("user@example.com");

        assertEquals(1, result);
    }

    @Test
    public void testConfirmTokenWithEmptyToken() {
        assertThrows(ResourceNotFoundException.class, () -> appUserService.confirmToken(""));
    }

    @Test
    public void testConfirmTokenWithValidToken_UserAlreadyEnabled() {
        String token = "validToken";
        AppUser enabledAppUser = new AppUser();
        enabledAppUser.setEnabled(true);
        enabledAppUser.setConfirmationToken(token);

        when(appUserRepository.findAppUserByConfirmationToken(token)).thenReturn(Optional.of(enabledAppUser));

        assertThrows(DuplicateResourceException.class, () -> appUserService.confirmToken(token));
    }

    @Test
    public void testConfirmTokenWithValidToken_Success() {
        String token = "validToken";
        AppUser disabledAppUser = new AppUser();
        disabledAppUser.setEnabled(false);

        when(appUserRepository.findAppUserByConfirmationToken(token)).thenReturn(Optional.of(disabledAppUser));

        assertDoesNotThrow(() -> appUserService.confirmToken(token));

        // Add verifications here based on your application's behavior
        verify(appUserRepository, times(1)).findAppUserByConfirmationToken(token);
    }

    @Test
    public void testConfirmTokenWithValidToken_EnableAppUser() {
        String token = "validToken";
        AppUser workingAppUser = new AppUser();
        workingAppUser.setEnabled(true);
        workingAppUser.setEmail("test@gmail.com");

        when(appUserRepository.findAppUserByConfirmationToken(token)).thenReturn(Optional.of(new AppUser()));
        when(appUserService.enableAppUser(workingAppUser.getEmail()));

        verify(appUserRepository.findAppUserByConfirmationToken(token));
        verify(appUserService.enableAppUser(workingAppUser.getEmail()));

    }

}
