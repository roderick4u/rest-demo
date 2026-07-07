package com.restproject.rest_demo.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, String> {
    Page<NotificationLog> findByClientId(String clientId, Pageable pageable);
}
