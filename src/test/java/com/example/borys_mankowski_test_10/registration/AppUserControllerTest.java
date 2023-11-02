//package com.example.borys_mankowski_test_10.registration;
//
//import com.example.borys_mankowski_test_10.appuser.AppUserRepository;
//import com.example.borys_mankowski_test_10.appuser.AppUserRole;
//import com.example.borys_mankowski_test_10.appuser.model.AppUser;
//import com.example.borys_mankowski_test_10.registration.model.RegistrationRequestDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.hasSize;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class AppUserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private AppUserRepository appUserRepository;
//
//    @MockBean
//    private ConfirmationTokenService confirmationTokenService;
//
//    @AfterEach
//    void teardown() {
//        appUserRepository.deleteAll();
//    }
//
//
//    @Test
//    void testRegister() throws Exception {
//        // Przygotowanie danych testowych
//        RegistrationRequestDto requestDto = new RegistrationRequestDto();
//        requestDto.setFirstName("John");
//        requestDto.setLastName("Doe");
//        requestDto.setEmail("johndoe@example.com");
//        requestDto.setPassword("securePassword");
//        requestDto.setAppUserRole(AppUserRole.CLIENT);
//
//        // Konwersja obiektu DTO na JSON
//        String requestJson = objectMapper.writeValueAsString(requestDto);
//
//        // Wywołanie metody API i sprawdzenie odpowiedzi
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/v1/registration")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//        // Konwersja odpowiedzi JSON na obiekt AuthenticationResponse
//        String content = result.getResponse().getContentAsString();
//        AuthenticationResponse response = objectMapper.readValue(content, AuthenticationResponse.class);
//
//        // Możesz teraz dokładniej przetestować treść odpowiedzi i upewnić się, że dane są poprawne
//    }
//
//    @Test
//    void testConfirmToken() throws Exception {
//
//        String token = UUID.randomUUID().toString();
//
//        ConfirmationToken confirmationToken = new ConfirmationToken();
//        confirmationToken.setToken(token);
//        confirmationToken.setAppUser(new AppUser());
//        confirmationToken.setConfirmedAt(null);
//        confirmationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
//
//        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));
//
//        // Wywołanie metody API i sprawdzenie odpowiedzi
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/api/v1/registration/confirm")
//                        .param("token", token))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.accessMessage").value("User email address confirmed!"));
//
//        // Możesz dodać inne przypadki testowe w celu pokrycia wszystkich scenariuszy
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void testGetAllCustomers() throws Exception {
//        AppUser customer1 = new AppUser();
//        customer1.setFirstName("John");
//        customer1.setLastName("Doe");
//        customer1.setEmail("john1.doe@example.com");
//        appUserRepository.save(customer1);
//
//        AppUser customer2 = new AppUser();
//        customer2.setFirstName("Jane");
//        customer2.setLastName("Doe");
//        customer2.setEmail("jane.doe@example.com");
//        appUserRepository.save(customer2);
//
//        mockMvc.perform(
//                        get("/api/v1/registration/all")
//                                .param("page", "0")
//                                .param("size", "10")
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[*]", hasSize(2)))
//                .andExpect(jsonPath("$.content[0].firstName", equalTo("John")))
//                .andExpect(jsonPath("$.content[0].lastName", equalTo("Doe")))
//                .andExpect(jsonPath("$.content[1].firstName", equalTo("Jane")))
//                .andExpect(jsonPath("$.content[1].lastName", equalTo("Doe")));
//
//        long count = appUserRepository.count();
//        assertEquals(2, count);
//        Optional<AppUser> foundCustomer1 = appUserRepository.findByEmail("john1.doe@example.com");
//        assertTrue(foundCustomer1.isPresent());
//        assertEquals("John", foundCustomer1.get().getFirstName());
//        assertEquals("Doe", foundCustomer1.get().getLastName());
//
//        Optional<AppUser> foundCustomer2 = appUserRepository.findByEmail("jane.doe@example.com");
//        assertTrue(foundCustomer2.isPresent());
//        assertEquals("Jane", foundCustomer2.get().getFirstName());
//        assertEquals("Doe", foundCustomer2.get().getLastName());
//    }
//}