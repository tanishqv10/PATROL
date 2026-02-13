package com.example.newsapplication;

public class Hospitals {
    private String name;
    private String address;
    private int numberOfBeds;
    private boolean vaccineAvailable;
    private String location;
    private String openStatus;
    private String imageUrl;
    private String googleMapsLink;

    // Constructor
    public Hospitals(String name, String address, int numberOfBeds, boolean vaccineAvailable,
                     String location, String openStatus, String imageUrl, String googleMapsLink) {
        this.name = name;
        this.address = address;
        this.numberOfBeds = numberOfBeds;
        this.vaccineAvailable = vaccineAvailable;
        this.location = location;
        this.openStatus = openStatus;
        this.imageUrl = imageUrl;
        this.googleMapsLink = googleMapsLink;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public boolean isVaccineAvailable() {
        return vaccineAvailable;
    }

    public String getLocation() {
        return location;
    }

    public String getopenStatus() {
        return openStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGoogleMapsLink() {
        return googleMapsLink;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public void setVaccineAvailable(boolean vaccineAvailable) {
        this.vaccineAvailable = vaccineAvailable;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setopenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setGoogleMapsLink(String googleMapsLink) {
        this.googleMapsLink = googleMapsLink;
    }
}
