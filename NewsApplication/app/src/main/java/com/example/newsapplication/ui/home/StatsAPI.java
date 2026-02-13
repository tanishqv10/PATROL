package com.example.newsapplication.ui.home;

import com.example.newsapplication.HealthStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface StatsAPI {
    @GET("stats")
    Call<Stats> fetchStats();

    @GET("getCovidData")
    Call<CovidStats> fetchCovidStats();

}
