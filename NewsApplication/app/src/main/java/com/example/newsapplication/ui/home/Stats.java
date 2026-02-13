package com.example.newsapplication.ui.home;

import java.util.ArrayList;

public class Stats {
    ArrayList<CountryStats> stats;

    public Stats(ArrayList<CountryStats> stats) {
        this.stats = stats;
    }

    public ArrayList<CountryStats> getStats() {
        return stats;
    }

    public void setStats(ArrayList<CountryStats> stats) {
        this.stats = stats;
    }
}
