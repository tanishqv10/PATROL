package com.example.newsapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FavPlaces {
    @GET("getFavPlaces")
    Call<DestinationResponse> getFavPlaces(@Query("email") String email);
}



