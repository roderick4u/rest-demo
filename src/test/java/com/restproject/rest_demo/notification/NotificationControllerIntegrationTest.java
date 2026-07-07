package com.restproject.rest_demo.notification;

import com.restproject.rest_demo.carrier.CarrierDeliveryResult;
import com.restproject.rest_demo.carrier.CarrierSimulatorService;
import com.restproject.rest_demo.client.ClientAccount;
import com.restproject.rest_demo.client.ClientAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerIntegrationTest {

    private static final String API_KEY = "demo-api-key";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientAccountRepository clientAccountRepository;

    @Autowired
    private NotificationLogRepository notificationLogRepository;

    @MockitoBean
    private CarrierSimulatorService carrierSimulatorService;

    @BeforeEach
    void setUp() {
        notificationLogRepository.deleteAll();
        clientAccountRepository.deleteAll();
        clientAccountRepository.save(new ClientAccount("client-1", "Demo Client", API_KEY, 2, 0));
        when(carrierSimulatorService.deliver(any(NotificationLog.class)))
                .thenReturn(new CarrierDeliveryResult(true, "carrier-ref-1", null));
    }

    @Test
    void sendNotificationReturnsAcceptedAndStoresCarrierReference() throws Exception {
        mockMvc.perform(post("/api/v1/notifications")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "+15551234567",
                                  "message": "Payment received",
                                  "type": "TRANSACTIONAL"
                                }
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.clientId").value("client-1"))
                .andExpect(jsonPath("$.status").value("DELIVERED"))
                .andExpect(jsonPath("$.attempts").value(1))
                .andExpect(jsonPath("$.carrierReference").value("carrier-ref-1"));
    }

    @Test
    void invalidRequestReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/notifications")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "",
                                  "message": "",
                                  "type": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownApiKeyReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/v1/notifications")
                        .header("X-API-Key", "missing-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "+15551234567",
                                  "message": "Payment received",
                                  "type": "TRANSACTIONAL"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void rateLimitReturnsTooManyRequests() throws Exception {
        clientAccountRepository.deleteAll();
        clientAccountRepository.save(new ClientAccount("client-1", "Demo Client", API_KEY, 1, 1));

        mockMvc.perform(post("/api/v1/notifications")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "+15551234567",
                                  "message": "Payment received",
                                  "type": "TRANSACTIONAL"
                                }
                                """))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void getNotificationByIdReturnsLog() throws Exception {
        String id = sendOneNotification();

        mockMvc.perform(get("/api/v1/notifications/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.clientId").value("client-1"));
    }

    @Test
    void getNotificationByIdReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/notifications/{id}", "missing-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNotificationsByClientIdReturnsPagedLogs() throws Exception {
        sendOneNotification();
        sendOneNotification();

        mockMvc.perform(get("/api/v1/notifications/client/{clientId}", "client-1")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(1));
    }

    private String sendOneNotification() throws Exception {
        String response = mockMvc.perform(post("/api/v1/notifications")
                        .header("X-API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "recipient": "+15551234567",
                                  "message": "Payment received",
                                  "type": "TRANSACTIONAL"
                                }
                                """))
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.substring(response.indexOf("\"id\":\"") + 6, response.indexOf("\",\"clientId\""));
    }
}
