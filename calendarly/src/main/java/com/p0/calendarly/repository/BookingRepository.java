package com.p0.calendarly.repository;

import com.p0.calendarly.enums.BookingStatus;
import com.p0.calendarly.exceptions.BookingNotFoundException;
import com.p0.calendarly.model.Booking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @PersistenceContext
    EntityManager entityManager = null;
    List<Booking> findByAvailabilityIdIn(List<Long> availabilityIds);

    default void updateStatus(Long bookingId, BookingStatus newStatus) throws BookingNotFoundException {
        Booking booking = entityManager.find(Booking.class, bookingId);
        if (booking != null) {
            booking.setStatus(newStatus);
            entityManager.merge(booking);
        } else {
            throw new BookingNotFoundException("Booking with ID " + bookingId + " not found");
        }
    }

}

