package com.example.newsapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HospitalService {
    @GET("nearby-hospitals")
    Call<List<Hospitals>> getNearbyHospitals(@Query("radius") int radius);
}
