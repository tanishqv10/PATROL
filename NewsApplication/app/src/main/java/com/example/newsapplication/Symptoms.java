package com.example.newsapplication;

public class Symptoms {
    private boolean fever;
    private boolean headache;
    private boolean soreThroat;
    private boolean nausea;
    private boolean fatigue;
    private String otherSymptoms;

    public boolean isFever() {
        return fever;
    }

    public void setFever(boolean fever) {
        this.fever = fever;
    }

    public boolean isHeadache() {
        return headache;
    }

    public void setHeadache(boolean headache) {
        this.headache = headache;
    }

    public boolean isSoreThroat() {
        return soreThroat;
    }

    public void setSoreThroat(boolean soreThroat) {
        this.soreThroat = soreThroat;
    }

    public boolean isNausea() {
        return nausea;
    }

    public void setNausea(boolean nausea) {
        this.nausea = nausea;
    }

    public boolean isFatigue() {
        return fatigue;
    }

    public void setFatigue(boolean fatigue) {
        this.fatigue = fatigue;
    }

    public String getOtherSymptoms() {
        return otherSymptoms;
    }

    public void setOtherSymptoms(String otherSymptoms) {
        this.otherSymptoms = otherSymptoms;
    }

    public Symptoms(boolean fever, boolean headache, boolean soreThroat, boolean nausea, boolean fatigue, String otherSymptoms) {
        this.fever = fever;
        this.headache = headache;
        this.soreThroat = soreThroat;
        this.nausea = nausea;
        this.fatigue = fatigue;
        this.otherSymptoms = otherSymptoms;
    }

    public boolean hasSymptoms() {
        return this.fever || this.headache || this.soreThroat || this.nausea || this.fatigue || !this.otherSymptoms.isEmpty();
    }
}
