package com.example.newsapplication;

public class HealthStatusDetails {
    private boolean covidPositive;
    private boolean covidExposed;
    private Symptoms symptoms;

    private String ts;


    public HealthStatusDetails(boolean covidPositive, boolean covidExposed, Symptoms symptoms, String ts) {
        this.covidPositive = covidPositive;
        this.covidExposed = covidExposed;
        this.symptoms = symptoms;
        this.ts = ts;
    }

    public boolean isCovidPositive() {
        return covidPositive;
    }

    public void setCovidPositive(boolean covidPositive) {
        this.covidPositive = covidPositive;
    }

    public boolean isCovidExposed() {
        return covidExposed;
    }

    public void setCovidExposed(boolean covidExposed) {
        this.covidExposed = covidExposed;
    }

    public Symptoms getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Symptoms symptoms) {
        this.symptoms = symptoms;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public boolean isHealthy() {
        return !this.covidPositive && !symptoms.hasSymptoms();
    }
}