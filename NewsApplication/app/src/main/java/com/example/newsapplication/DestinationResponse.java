package com.example.newsapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DestinationResponse {
    @SerializedName("favPlaces") // Use this if the variable name does not match the JSON key
    private List<Destination> favPlaces;

    public List<Destination> getFavPlaces() {
        return favPlaces;
    }

    public void setFavPlaces(List<Destination> favPlaces) {
        this.favPlaces = favPlaces;
    }
}

