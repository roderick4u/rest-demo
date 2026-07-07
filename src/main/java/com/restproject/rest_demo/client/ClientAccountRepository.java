package com.restproject.rest_demo.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientAccountRepository extends JpaRepository<ClientAccount, String> {
    Optional<ClientAccount> findByApiKey(String apiKey);
}
