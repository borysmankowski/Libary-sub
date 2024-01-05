package com.example.borys_mankowski_test_10.appuser;

import com.example.borys_mankowski_test_10.appuser.model.AppUser;
import com.example.borys_mankowski_test_10.appuser.model.CreateAppUserCommand;
import com.example.borys_mankowski_test_10.book.model.CreateBookCommand;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.in;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
                .andExpect(jsonPath("$.firstName").value(createAppUserCommand.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(createAppUserCommand.getLastName()))
                .andExpect(jsonPath("$.email").value(createAppUserCommand.getEmail()));

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
    void getAllAppUsers() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setFirstName("John");
        appUser.setLastName("Doe");
        appUser.setEmail("john.doe@example.com");
        appUserRepository.save(appUser);

        mockMvc.perform(get("/api/v1/user")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].firstName", equalTo(appUser.getFirstName())))
                .andExpect(jsonPath("$.content.[0].lastName", equalTo(appUser.getLastName())))
                .andExpect(jsonPath("$.content.[0].email", equalTo(appUser.getEmail())));
    }

    @Test
    void createAppUserFailureBlankName() throws Exception {
        CreateAppUserCommand invalidCreateAppUserCommand = new CreateAppUserCommand("", "Sample Lastname", "test@test.com");

        String exceptionMsg = "FirstName cannot be blank";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateAppUserCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("validation errors"))
                .andExpect(jsonPath("$.violations[0].field").value("firstName"))
                .andExpect(jsonPath("$.violations[0].message").value(exceptionMsg));
    }

    @Test
    void createAppUserFailureBlankLastName() throws Exception {
        CreateAppUserCommand invalidCreateAppUserCommand = new CreateAppUserCommand("First Name", " ", "test@test.com");

        String exceptionMsg = "LastName cannot be blank";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateAppUserCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("validation errors"))
                .andExpect(jsonPath("$.violations[0].field").value("lastName"))
                .andExpect(jsonPath("$.violations[0].message").value(exceptionMsg));
    }

    @Test
    void createAppUserFailureBlankEmail() throws Exception {
        CreateAppUserCommand invalidCreateAppUserCommand = new CreateAppUserCommand("First Name", "Sample Lastname", "");

        String exceptionMsg = "Email cannot be blank";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateAppUserCommand)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("validation errors"))
                .andExpect(jsonPath("$.violations[0].field").value("email"))
                .andExpect(jsonPath("$.violations[0].message").value(exceptionMsg));
    }
}