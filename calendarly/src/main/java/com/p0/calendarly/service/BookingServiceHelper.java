package com.p0.calendarly.service;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.Booking;
import com.p0.calendarly.model.User;
import com.p0.calendarly.model.request.BookSlotRequest;
import com.p0.calendarly.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookingServiceHelper {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private static UserService userService;

    public static void verifyRequest(Availability availability, BookSlotRequest request, List<Booking> existingBookings
        , User bookedBy) throws CustomException{
        verifyTimings(availability, request);
        verifyUsers(availability, request);
        verifyBooking(existingBookings, bookedBy);
    }

    //had to do like below as timestamp.before() was returning true for equal condition
    private static void verifyTimings(Availability availability, BookSlotRequest request) throws CustomException{
        if((!request.getStartTime().after(availability.getStartTime()) && !request.getStartTime().equals(availability.getStartTime()))
                || request.getEndTime().after(availability.getEndTime())){
            throw new CustomException("Booking time is not valid for the provided availability slot");
        }
    }

    private static void verifyUsers(Availability availability, BookSlotRequest request){
        if(!availability.getUser().getId().equals(request.getUserId())){
            throw new CustomException("Availability and booking users are different");
        }
        if(request.getUserId().equals(request.getOtherParticipantId())){
            throw new CustomException("Both participants can not be same");
        }
    }

    private static void verifyBooking(List<Booking> existingBookings, User bookedBy){
        //checking if the same user has created another booking against the same availability id
        for(Booking booking : existingBookings){
            if(booking.getBookedBy().equals(bookedBy)){
                throw new CustomException("Booking already exists");
            }
        }
    }

}
