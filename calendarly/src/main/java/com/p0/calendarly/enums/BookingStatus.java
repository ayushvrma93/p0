package com.p0.calendarly.enums;

public enum BookingStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED");

    private final String status;

    BookingStatus(String status){
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
