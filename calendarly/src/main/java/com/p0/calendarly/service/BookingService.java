package com.p0.calendarly.service;

import com.p0.calendarly.enums.BookingStatus;
import com.p0.calendarly.exceptions.BookingNotFoundException;
import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.Booking;
import com.p0.calendarly.model.User;
import com.p0.calendarly.model.request.BookSlotRequest;
import com.p0.calendarly.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AvailabilityService availabilityService;

//    @Transactional
    public Booking create(BookSlotRequest bookSlotRequest, User bookedBy) throws CustomException {
        Optional<Availability> availabilityOptional = availabilityService.findById(bookSlotRequest.getAvailabilityId());

        if (availabilityOptional.isEmpty()) {
            throw new CustomException("No such availability slot exists");
        }

        Availability availability = availabilityOptional.get();

        BookingServiceHelper.verifyTimings(availability, bookSlotRequest);
        BookingServiceHelper.verifyUser(availability, bookSlotRequest);

        List<Booking> existingBookings = availability.getBookings();

        for(Booking booking : existingBookings){
            if(booking.getBookedBy().equals(bookedBy)){
                throw new CustomException("Booking already exists");
            }
        }

        Booking booking = new Booking.Builder()
                .setBookedBy(bookedBy)
                .setStatus(BookingStatus.PENDING)
                .setName(bookSlotRequest.getName())
                .setDescription(bookSlotRequest.getDescription())
                .setOtherParticipant(bookSlotRequest.getOtherParticipant())
                .setStartTime(bookSlotRequest.getStartTime())
                .setEndTime(bookSlotRequest.getEndTime())
                .build();


        availability.getBookings().add(booking);
        booking = bookingRepository.save(booking);
//        availability.setBookings();
//        availabilityService.addBooking(existingBookings, booking, availability);
        availabilityService.update(availability);
        return booking;
    }

    public void updateStatus(Long id, BookingStatus status) throws BookingNotFoundException {
        Booking existingBooking = get(id);
        existingBooking.setStatus(status);
        bookingRepository.save(existingBooking);
    }

    public Booking get(Long id) throws BookingNotFoundException {
        Optional<Booking> existingBookingOptional = bookingRepository.findById(id);
        if(existingBookingOptional.isEmpty()){
            throw new BookingNotFoundException("No such booking exists");
        }
        Booking existingBooking = existingBookingOptional.get();
        return existingBooking;
    }
}
