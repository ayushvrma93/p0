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

    @Autowired
    private UserService userService;

    public Booking create(BookSlotRequest bookSlotRequest) throws CustomException {
        Optional<Availability> availabilityOptional = availabilityService.findById(bookSlotRequest.getAvailabilityId());

        if (availabilityOptional.isEmpty()) {
            throw new CustomException("No such availability slot exists");
        }

        Availability availability = availabilityOptional.get();

        User bookedBy = userService.findById(bookSlotRequest.getUserId());
        User otherParticipant = userService.findById(bookSlotRequest.getOtherParticipantId());


        List<Booking> existingBookings = availability.getBookings();
        BookingServiceHelper.verifyRequest(availability, bookSlotRequest, existingBookings, bookedBy);

        Booking booking = new Booking.Builder()
                .setBookedBy(bookedBy)
                .setStatus(BookingStatus.PENDING)
                .setName(bookSlotRequest.getName())
                .setDescription(bookSlotRequest.getDescription())
                .setOtherParticipant(otherParticipant)
                .setStartTime(bookSlotRequest.getStartTime())
                .setEndTime(bookSlotRequest.getEndTime())
                .build();


        availability.getBookings().add(booking);
        booking = bookingRepository.save(booking);
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
