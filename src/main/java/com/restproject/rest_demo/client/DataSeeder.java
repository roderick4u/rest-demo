package com.restproject.rest_demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ClientAccountRepository clientAccountRepository;
    private final String demoApiKey;

    public DataSeeder(ClientAccountRepository clientAccountRepository,
                      @Value("${demo.client.api-key:demo-api-key}") String demoApiKey) {
        this.clientAccountRepository = clientAccountRepository;
        this.demoApiKey = demoApiKey;
    }

    @Override
    public void run(String... args) {
        clientAccountRepository.findById("demo-client")
                .orElseGet(() -> clientAccountRepository.save(
                        new ClientAccount("demo-client", "Demo Telecom Client", demoApiKey, 100, 0)
                ));
    }
}
