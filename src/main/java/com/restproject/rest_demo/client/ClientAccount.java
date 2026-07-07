package com.restproject.rest_demo.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_accounts")
public class ClientAccount {

    @Id
    private String id;

    private String name;

    @Column(nullable = false, unique = true)
    private String apiKey;

    private int dailyMessageLimit;

    private int messagesSentToday;

    public ClientAccount() {
    }

    public ClientAccount(String id, String name, String apiKey, int dailyMessageLimit, int messagesSentToday) {
        this.id = id;
        this.name = name;
        this.apiKey = apiKey;
        this.dailyMessageLimit = dailyMessageLimit;
        this.messagesSentToday = messagesSentToday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getDailyMessageLimit() {
        return dailyMessageLimit;
    }

    public void setDailyMessageLimit(int dailyMessageLimit) {
        this.dailyMessageLimit = dailyMessageLimit;
    }

    public int getMessagesSentToday() {
        return messagesSentToday;
    }

    public void setMessagesSentToday(int messagesSentToday) {
        this.messagesSentToday = messagesSentToday;
    }
}
