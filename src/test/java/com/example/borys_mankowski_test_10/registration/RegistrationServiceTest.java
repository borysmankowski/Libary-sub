//package com.example.borys_mankowski_test_10.registration;
//
//import com.example.borys_mankowski_test_10.appuser.AppUserRole;
//import com.example.borys_mankowski_test_10.appuser.AppUserService;
//import com.example.borys_mankowski_test_10.appuser.model.AppUser;
//import com.example.borys_mankowski_test_10.email.EmailValidator;
//import com.example.borys_mankowski_test_10.registration.model.RegistrationRequestDto;
//import com.example.borys_mankowski_test_10.registration.token.ConfirmationToken;
//import com.example.borys_mankowski_test_10.registration.token.ConfirmationTokenService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//
//@SpringBootTest
//class RegistrationServiceTest {
//
//    @Mock
//    private EmailValidator emailValidator;
//    @Mock
//    private AppUserService appUserService;
//
//    @InjectMocks
//    private RegistrationService registrationService;
//    @Mock
//    private EmailSender emailSender;
//    @Mock
//    private ConfirmationTokenService confirmationTokenService;
//
//    @Test
//    void testRegisterWithValidEmail() {
//
//        RegistrationRequestDto requestDto = new RegistrationRequestDto();
//        requestDto.setFirstName("Darek");
//        requestDto.setFirstName("Marek");
//        requestDto.setPassword("password");
//        requestDto.setEmail("password@gmail.com");
//        requestDto.setAppUserRole(AppUserRole.CLIENT);
//
//        String tokenForNewUser = "token";
//        when(emailValidator.test("password@gmail.com")).thenReturn(true);
//        AuthenticationResponse response = registrationService.register(requestDto);
//        response.setAccessToken(tokenForNewUser);
//
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(tokenForNewUser, response.getAccessToken());
//        assertEquals("User registered!", response.getAccessMessage());
//        // You may add more specific assertions as needed.
//    }
//
//    @Test
//    void testConfirmTokenSuccess() {
//        // Przygotowanie danych wejściowych
//        String token = "validToken";
//        ConfirmationToken confirmationToken = new ConfirmationToken();
//        confirmationToken.setToken(token);
//        confirmationToken.setConfirmedAt(null); // Token nie jest jeszcze potwierdzony
//        LocalDateTime expiredAt = LocalDateTime.now().plusDays(1);
//        confirmationToken.setExpiresAt(expiredAt);
//
//
//        AppUser appUser = new AppUser();
//        appUser.setEmail("test@example.com");
//        confirmationToken.setAppUser(appUser);
//
//        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));
//        when(appUserService.enableAppUser("test@example.com")).thenReturn(1);
//
//        // Wywołanie metody
//        AuthenticationResponse response = registrationService.confirmToken(token);
//
//        // Sprawdzenie wyników
//        assertNotNull(response);
//        assertEquals("User email address confirmed!", response.getAccessMessage());
//
//        verify(confirmationTokenService, times(1)).setConfirmedAt(token);
//        verify(appUserService, times(1)).enableAppUser(appUser.getEmail());
//    }
//}