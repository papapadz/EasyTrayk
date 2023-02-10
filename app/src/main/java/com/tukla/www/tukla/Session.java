package com.tukla.www.tukla;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Session {

    private User driver;
    private Booking booking;
    private String startedAt;
    private LatLngDefined driverLocation;
    private Boolean isDriverArrived;
    private Boolean isDone;
    private Boolean isCancelled;
    private Boolean is500meters;
    private Boolean is50meters;
    private Boolean isAccepted;

    public Session() {

    }

    public Session(User driver, Booking booking, String startedAt, LatLngDefined driverLocation, Boolean isDriverArrived, Boolean isDone, Boolean isCancelled, Boolean is500meters, Boolean is50meters, Boolean isAccepted) {
        this.driver = driver;
        this.booking = booking;
        this.startedAt = startedAt;
        this.driverLocation = driverLocation;
        this.isDriverArrived = isDriverArrived;
        this.isDone = isDone;
        this.isCancelled = isCancelled;
        this.is500meters = is500meters;
        this.is50meters = is50meters;
        this.isAccepted = isAccepted;
    }

    public User getDriver() {
        return driver;
    }

    public Booking getBooking() {
        return booking;
    }

    public Boolean getIsDriverArrived() {
        return isDriverArrived;
    }

    public LatLngDefined getDriverLocation() {
        return driverLocation;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public boolean isDriverEmpty() {
        return driver == null;
    }

    public boolean isBookingEmpty() {
        return  booking == null;
    }

    public boolean getIsCancelled() {
        return isCancelled;
    }

    public Boolean getIs500meters() {
        return is500meters;
    }

    public Boolean getIs50meters() {
        return is50meters;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }
}
