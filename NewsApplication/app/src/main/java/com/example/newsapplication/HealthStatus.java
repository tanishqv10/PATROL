package com.example.newsapplication;

public class HealthStatus {
    private String email;
    private HealthStatusDetails healthStatus;

    public HealthStatus(String email, HealthStatusDetails healthStatus) {
        this.email = email;
        this.healthStatus = healthStatus;
    }

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HealthStatusDetails getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(HealthStatusDetails healthStatus) {
        this.healthStatus = healthStatus;
    }
}