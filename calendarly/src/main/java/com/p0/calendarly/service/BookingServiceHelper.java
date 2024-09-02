package com.p0.calendarly.service;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.request.BookSlotRequest;

public class BookingServiceHelper {

    public static void verifyRequest(Availability availability, BookSlotRequest request) throws CustomException{
        verifyTimings(availability, request);
        verifyUser(availability, request);
    }
    private static void verifyTimings(Availability availability, BookSlotRequest request) throws CustomException{
        if((!request.getStartTime().after(availability.getStartTime()) && !request.getStartTime().equals(availability.getStartTime()))
                || request.getEndTime().after(availability.getEndTime())){
            throw new CustomException("Booking time is not valid for the provided availability slot");
        }
    }

    private static void verifyUser(Availability availability, BookSlotRequest request){
        if(!availability.getUser().getId().equals(request.getUserId())){
            throw new CustomException("Availability and booking users are different");
        }
    }

}
