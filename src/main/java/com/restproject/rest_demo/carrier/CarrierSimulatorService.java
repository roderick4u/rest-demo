package com.restproject.rest_demo.carrier;

import com.restproject.rest_demo.notification.NotificationLog;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarrierSimulatorService {

    public CarrierDeliveryResult deliver(NotificationLog notificationLog) {
        return new CarrierDeliveryResult(true, "carrier-" + UUID.randomUUID(), null);
    }
}
