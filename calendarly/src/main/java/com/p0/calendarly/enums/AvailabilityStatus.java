package com.p0.calendarly.enums;

public enum AvailabilityStatus {
    AVAILABLE("AVAILABLE"),
    BOOKED("BOOKED");

    private final String status;

    AvailabilityStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
