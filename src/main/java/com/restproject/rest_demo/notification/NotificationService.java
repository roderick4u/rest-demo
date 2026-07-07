package com.restproject.rest_demo.notification;

import com.restproject.rest_demo.carrier.CarrierDeliveryResult;
import com.restproject.rest_demo.carrier.CarrierSimulatorService;
import com.restproject.rest_demo.client.ClientAccount;
import com.restproject.rest_demo.client.ClientAccountRepository;
import com.restproject.rest_demo.notification.dto.NotificationResponse;
import com.restproject.rest_demo.notification.dto.SendNotificationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private static final int CRITICAL_MAX_ATTEMPTS = 3;

    private final ClientAccountRepository clientAccountRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final CarrierSimulatorService carrierSimulatorService;

    public NotificationService(ClientAccountRepository clientAccountRepository,
                               NotificationLogRepository notificationLogRepository,
                               CarrierSimulatorService carrierSimulatorService) {
        this.clientAccountRepository = clientAccountRepository;
        this.notificationLogRepository = notificationLogRepository;
        this.carrierSimulatorService = carrierSimulatorService;
    }

    @Transactional
    public NotificationResponse send(String apiKey, SendNotificationRequest request) {
        ClientAccount client = clientAccountRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new ClientNotFoundException("Client not found for API key"));

        if (client.getMessagesSentToday() >= client.getDailyMessageLimit()) {
            throw new RateLimitExceededException("Daily message limit exceeded");
        }

        client.setMessagesSentToday(client.getMessagesSentToday() + 1);
        clientAccountRepository.save(client);

        NotificationLog notificationLog = new NotificationLog(
                client.getId(),
                request.getRecipient(),
                request.getMessage(),
                request.getType()
        );

        int maxAttempts = request.getType() == MessageType.CRITICAL ? CRITICAL_MAX_ATTEMPTS : 1;
        CarrierDeliveryResult lastResult = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            lastResult = carrierSimulatorService.deliver(notificationLog);
            notificationLog.setAttempts(attempt);

            if (lastResult.success()) {
                notificationLog.setStatus(NotificationStatus.DELIVERED);
                notificationLog.setCarrierReference(lastResult.carrierRef());
                notificationLog.setErrorMessage(null);
                return NotificationResponse.from(notificationLogRepository.save(notificationLog));
            }
        }

        notificationLog.setStatus(NotificationStatus.FAILED);
        notificationLog.setErrorMessage(lastResult == null ? "Carrier delivery failed" : lastResult.errorMessage());
        return NotificationResponse.from(notificationLogRepository.save(notificationLog));
    }

    @Transactional(readOnly = true)
    public NotificationResponse getById(String id) {
        return notificationLogRepository.findById(id)
                .map(NotificationResponse::from)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found: " + id));
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getByClientId(String clientId, Pageable pageable) {
        return notificationLogRepository.findByClientId(clientId, pageable)
                .map(NotificationResponse::from);
    }
}
