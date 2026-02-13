package com.example.newsapplication;

import java.util.ArrayList;

public class HealthStatusesModal {
    private ArrayList<HealthStatusDetails> healthStatuses;

    public HealthStatusesModal(ArrayList<HealthStatusDetails> healthStatuses) {
        this.healthStatuses = healthStatuses;
    }

    public ArrayList<HealthStatusDetails> getHealthStatuses() {
        return healthStatuses;
    }

    public void setHealthStatuses(ArrayList<HealthStatusDetails> healthStatuses) {
        this.healthStatuses = healthStatuses;
    }
}
