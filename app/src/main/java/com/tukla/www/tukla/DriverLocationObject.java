package com.tukla.www.tukla;

public class DriverLocationObject {
    private DriverLocation driverLocation;
    private String eta;

    public DriverLocationObject() {

    }

    public DriverLocationObject(DriverLocation driverLocation, String eta) {
        this.driverLocation = driverLocation;
        this.eta = eta;
    }

    public DriverLocation getDriverLocation() {
        return driverLocation;
    }

    public String getEta() {
        return eta;
    }
}
