package com.tukla.www.tukla;

public class Driver {

    private String toda;
    private String plateNumber;
    private String tricycleNumber;

    public Driver() {

    }

    public Driver(
            String toda,
            String plateNumber,
            String tricycleNumber) {

        this.toda = toda;
        this.plateNumber = plateNumber;
        this.tricycleNumber = tricycleNumber;
    }

    public String getToda() {
        return this.toda;
    }

    public String getPlateNumber() {
        return this.plateNumber;
    }

    public String getTricycleNumber() {
        return this.tricycleNumber;
    }

}
