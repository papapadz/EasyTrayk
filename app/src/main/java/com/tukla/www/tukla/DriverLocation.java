package com.tukla.www.tukla;

public class DriverLocation {

    private User user;
    private LatLngDefined location;
    private String updatedAt;
    private boolean isActive;

    public DriverLocation() {

    }

    public DriverLocation(User user, LatLngDefined location, String updatedAt, boolean isActive) {
        this.user = user;
        this.location = location;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

    public User getUser() {
        return user;
    }

    public LatLngDefined getLocation() {
        return location;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
