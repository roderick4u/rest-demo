package com.restproject.rest_demo.notification;

import com.restproject.rest_demo.notification.dto.NotificationResponse;
import com.restproject.rest_demo.notification.dto.SendNotificationRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> send(
            @RequestHeader("X-API-Key") String apiKey,
            @Valid @RequestBody SendNotificationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(notificationService.send(apiKey, request));
    }

    @GetMapping("/{id}")
    public NotificationResponse getById(@PathVariable String id) {
        return notificationService.getById(id);
    }

    @GetMapping("/client/{clientId}")
    public Page<NotificationResponse> getByClientId(@PathVariable String clientId, Pageable pageable) {
        return notificationService.getByClientId(clientId, pageable);
    }
}
