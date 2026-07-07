package com.restproject.rest_demo.notification.dto;

import com.restproject.rest_demo.notification.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendNotificationRequest {

    @NotBlank
    private String recipient;

    @NotBlank
    private String message;

    @NotNull
    private MessageType type;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
