package com.example.newsapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HealthCheckApi {
    @POST("healthStatus")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Void> submitHealthStatus(
            @Body HealthStatus healthStatus
    );

    @GET("healthStatus")
    Call<HealthStatusesModal> getHealthStatuses(
            @Query("email") String email
    );
}