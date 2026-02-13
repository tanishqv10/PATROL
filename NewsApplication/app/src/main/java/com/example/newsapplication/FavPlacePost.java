package com.example.newsapplication;

public class FavPlacePost {
    private String email;
    private FavPlace favoritePlace;

    public FavPlacePost(String email, FavPlace favPlace) {
        this.email = email;
        this.favoritePlace = favPlace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FavPlace getFavPlace() {
        return favoritePlace;
    }

    public void setFavPlace(FavPlace favPlace) {
        this.favoritePlace = favPlace;
    }
}
