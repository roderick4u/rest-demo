package com.restproject.rest_demo.notification.dto;

import com.restproject.rest_demo.notification.MessageType;
import com.restproject.rest_demo.notification.NotificationLog;
import com.restproject.rest_demo.notification.NotificationStatus;

import java.time.Instant;

public record NotificationResponse(
        String id,
        String clientId,
        String recipient,
        String message,
        MessageType type,
        NotificationStatus status,
        int attempts,
        String carrierReference,
        String errorMessage,
        Instant createdAt
) {
    public static NotificationResponse from(NotificationLog notificationLog) {
        return new NotificationResponse(
                notificationLog.getId(),
                notificationLog.getClientId(),
                notificationLog.getRecipient(),
                notificationLog.getMessage(),
                notificationLog.getType(),
                notificationLog.getStatus(),
                notificationLog.getAttempts(),
                notificationLog.getCarrierReference(),
                notificationLog.getErrorMessage(),
                notificationLog.getCreatedAt()
        );
    }
}
