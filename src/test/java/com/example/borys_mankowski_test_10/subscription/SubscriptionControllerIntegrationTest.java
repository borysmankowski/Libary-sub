package com.example.borys_mankowski_test_10.subscription;

import com.example.borys_mankowski_test_10.subscription.model.CreateSubscriptionCommand;
import com.example.borys_mankowski_test_10.subscription.model.SubscriptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SubscriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateSubscription() throws Exception {
        CreateSubscriptionCommand createSubscriptionCommand = new CreateSubscriptionCommand();
        createSubscriptionCommand.setAppUserId(1L);
        createSubscriptionCommand.setAuthor("Author1");

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(1L);
        subscriptionDto.setAppUserId(1L);
        subscriptionDto.setSubscribed(true);

        when(subscriptionService.createSubscription(any(CreateSubscriptionCommand.class))).thenReturn(subscriptionDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSubscriptionCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCancelSubscription() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/subscriptions/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}