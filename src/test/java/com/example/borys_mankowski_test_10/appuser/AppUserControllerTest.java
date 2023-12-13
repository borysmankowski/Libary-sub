package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.appuser.model.CreateAppUserCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @AfterEach
    void teardown() {
        appUserRepository.deleteAll();
    }


    @Test
    void registerUser() throws Exception {

        CreateAppUserCommand createAppUserCommand = CreateAppUserCommand.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(createAppUserCommand);

        mockMvc.perform(
                        post("/api/v1/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        assertTrue(appUserRepository.existsByEmail("john.doe@example.com"));

        Optional<AppUser> registeredCustomer = appUserRepository.findByEmail("john.doe@example.com");
        assertTrue(registeredCustomer.isPresent());
        assertEquals("John", registeredCustomer.get().getFirstName());
        assertEquals("Doe", registeredCustomer.get().getLastName());
    }

    @Test
    void confirmEmail() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setFirstName("John");
        appUser.setLastName("Doe");
        appUser.setEmail("john.doe@example.com");
        appUser.setEnabled(false);
        appUser.setConfirmationToken(UUID.randomUUID().toString());
        appUserRepository.save(appUser);

        mockMvc.perform(
                        get("/api/v1/user/confirm")
                                .param("token", appUser.getConfirmationToken())
                )
                .andExpect(status().isOk());

        Optional<AppUser> confirmedCustomer = appUserRepository.findById(appUser.getId());
        assertTrue(confirmedCustomer.isPresent());
        assertTrue(confirmedCustomer.get().isEnabled());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCustomers() throws Exception {

        AppUser appUser = new AppUser();
        appUser.setFirstName("John");
        appUser.setLastName("Doe");
        appUser.setEmail("john.doe@example.com");
        appUserRepository.save(appUser);

        AppUser appUser2 = new AppUser();
        appUser2.setFirstName("Mike");
        appUser2.setLastName("Doe");
        appUser2.setEmail("mike.doe@example.com");
        appUserRepository.save(appUser2);

        mockMvc.perform(
                        get("/api/v1/user")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(2)))
                .andExpect(jsonPath("$.content[0].firstName", equalTo("John")))
                .andExpect(jsonPath("$.content[0].lastName", equalTo("Doe")))
                .andExpect(jsonPath("$.content[1].firstName", equalTo("Mike")))
                .andExpect(jsonPath("$.content[1].lastName", equalTo("Doe")));

        long count = appUserRepository.count();
        assertEquals(2, count);
        Optional<AppUser> foundCustomer1 = appUserRepository.findByEmail("john.doe@example.com");
        assertTrue(foundCustomer1.isPresent());
        assertEquals("John", foundCustomer1.get().getFirstName());
        assertEquals("Doe", foundCustomer1.get().getLastName());

        Optional<AppUser> foundCustomer2 = appUserRepository.findByEmail("mike.doe@example.com");
        assertTrue(foundCustomer2.isPresent());
        assertEquals("Mike", foundCustomer2.get().getFirstName());
        assertEquals("Doe", foundCustomer2.get().getLastName());

    }
}