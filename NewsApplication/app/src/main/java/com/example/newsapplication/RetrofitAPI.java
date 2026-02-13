package com.example.newsapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET("news")
    Call<NewsModal> getAllNews();

    @POST("favPlaces")
    Call<Void> addFavPlace(
            @Body FavPlacePost favPlacePost
    );
}
