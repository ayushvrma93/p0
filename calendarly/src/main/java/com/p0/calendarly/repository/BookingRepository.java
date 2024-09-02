package com.p0.calendarly.repository;

import com.p0.calendarly.model.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @EntityGraph(attributePaths = {"bookings"})
    List<Booking> findByAvailabilityIdIn(List<Long> availabilityIds);
    Booking save(Booking booking);

}

