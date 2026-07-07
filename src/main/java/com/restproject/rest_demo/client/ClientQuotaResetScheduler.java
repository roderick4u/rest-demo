package com.restproject.rest_demo.client;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClientQuotaResetScheduler {

    private final ClientAccountRepository clientAccountRepository;

    public ClientQuotaResetScheduler(ClientAccountRepository clientAccountRepository) {
        this.clientAccountRepository = clientAccountRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetDailyMessageCounts() {
        clientAccountRepository.findAll().forEach(client -> {
            client.setMessagesSentToday(0);
            clientAccountRepository.save(client);
        });
    }
}
