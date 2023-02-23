package com.tukla.www.tukla;

public class Fare {

    private String code;
    private double price;
    private Boolean isActive;
    private String enteredBy;
    private String createdAt;

    public Fare() {

    }

    public Fare(String code, double price, Boolean isActive, String enteredBy, String createdAt) {
        this.code = code;
        this.price = price;
        this.isActive = isActive;
        this.enteredBy = enteredBy;
        this.createdAt = createdAt;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getEnteredBy() {
        return enteredBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
