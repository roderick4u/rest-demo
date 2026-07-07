package com.restproject.rest_demo.notification;

import com.restproject.rest_demo.carrier.CarrierDeliveryResult;
import com.restproject.rest_demo.carrier.CarrierSimulatorService;
import com.restproject.rest_demo.client.ClientAccount;
import com.restproject.rest_demo.client.ClientAccountRepository;
import com.restproject.rest_demo.notification.dto.SendNotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private ClientAccountRepository clientAccountRepository;

    @Mock
    private NotificationLogRepository notificationLogRepository;

    @Mock
    private CarrierSimulatorService carrierSimulatorService;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(
                clientAccountRepository,
                notificationLogRepository,
                carrierSimulatorService
        );
    }

    @Test
    void criticalMessagesRetryUpToMaxAttempts() {
        SendNotificationRequest request = request(MessageType.CRITICAL);
        when(clientAccountRepository.findByApiKey("api-key"))
                .thenReturn(Optional.of(new ClientAccount("client-1", "Demo Client", "api-key", 10, 0)));
        when(carrierSimulatorService.deliver(any(NotificationLog.class)))
                .thenReturn(new CarrierDeliveryResult(false, null, "carrier unavailable"));
        when(notificationLogRepository.save(any(NotificationLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = notificationService.send("api-key", request);

        assertThat(response.status()).isEqualTo(NotificationStatus.FAILED);
        assertThat(response.attempts()).isEqualTo(3);
        verify(carrierSimulatorService, times(3)).deliver(any(NotificationLog.class));
    }

    @Test
    void marketingMessagesDoNotRetry() {
        SendNotificationRequest request = request(MessageType.MARKETING);
        when(clientAccountRepository.findByApiKey("api-key"))
                .thenReturn(Optional.of(new ClientAccount("client-1", "Demo Client", "api-key", 10, 0)));
        when(carrierSimulatorService.deliver(any(NotificationLog.class)))
                .thenReturn(new CarrierDeliveryResult(false, null, "carrier unavailable"));
        when(notificationLogRepository.save(any(NotificationLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = notificationService.send("api-key", request);

        assertThat(response.status()).isEqualTo(NotificationStatus.FAILED);
        assertThat(response.attempts()).isEqualTo(1);
        verify(carrierSimulatorService, times(1)).deliver(any(NotificationLog.class));
    }

    private SendNotificationRequest request(MessageType type) {
        SendNotificationRequest request = new SendNotificationRequest();
        request.setRecipient("+15551234567");
        request.setMessage("Test message");
        request.setType(type);
        return request;
    }
}
