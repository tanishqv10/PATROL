package com.example.newsapplication.ui.home;

public class Case {
    private String date;
    private int numTotal;
    private int numNew;

    public Case(String date, int numTotal, int numNew) {
        this.date = date;
        this.numTotal = numTotal;
        this.numNew = numNew;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumTotal() {
        return numTotal;
    }

    public void setNumTotal(int numTotal) {
        this.numTotal = numTotal;
    }

    public int getNumNew() {
        return numNew;
    }

    public void setNumNew(int numNew) {
        this.numNew = numNew;
    }
}
