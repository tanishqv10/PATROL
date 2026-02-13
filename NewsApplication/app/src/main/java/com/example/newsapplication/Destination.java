package com.example.newsapplication;

public class Destination {
    private String name;
    private String riskLevel;


    public Destination(String name, String riskLevel) {
        this.name = name;
        this.riskLevel = riskLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
