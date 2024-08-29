package com.p0.calendarly.service;

import com.p0.calendarly.enums.BookingStatus;
import com.p0.calendarly.exceptions.BookingNotFoundException;
import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.Booking;
import com.p0.calendarly.model.User;
import com.p0.calendarly.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AvailabilityService availabilityService;

    public Booking bookSlot(Long availabilityId, User bookedBy, String name, String description, List<User> participants) throws CustomException {
        Optional<Availability> availabilityOptional = availabilityService.findById(availabilityId);

        if (availabilityOptional.isEmpty()) {
            throw new CustomException("No such availability slot exists");
        }

        Availability availability = availabilityOptional.get();

        Booking booking = new Booking.Builder()
                .setAvailability(availability)
                .setBookedBy(bookedBy)
                .setStatus(BookingStatus.PENDING)
                .setName(name)
                .setDescription(description)
                .setParticipants(participants)
                .build();

        booking = bookingRepository.save(booking);

        List<Booking> bookingsList = new ArrayList<>();
        bookingsList.add(booking);
        availability.setBookings(bookingsList);
        availabilityService.createAvailability(availability.getUser(), availability.getStartTime(), availability.getEndTime()); // Save the updated availability
        return booking;
    }

    public void updateStatus(Long id, BookingStatus status) throws BookingNotFoundException {
        bookingRepository.updateStatus(id, status);
    }
}
