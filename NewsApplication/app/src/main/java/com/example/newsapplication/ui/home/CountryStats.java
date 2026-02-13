package com.example.newsapplication.ui.home;

import java.util.ArrayList;

public class CountryStats {
    private String country;
    private ArrayList<Case> cases;

    public CountryStats(String country, ArrayList<Case> cases) {
        this.country = country;
        this.cases = cases;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<Case> getCases() {
        return cases;
    }

    public void setCases(ArrayList<Case> cases) {
        this.cases = cases;
    }
}
