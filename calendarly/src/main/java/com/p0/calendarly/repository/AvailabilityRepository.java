package com.p0.calendarly.repository;

import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByUserIdAndStartTimeBetween(Long userId, Timestamp startTime, Timestamp endTime);

    Availability save(Availability availability);

    List<Availability> findByUserId(Long userId);

    @Modifying
    @Query(value = "UPDATE availability SET bookings = :bookings WHERE id = :id", nativeQuery = true)
    int updateBookings(@Param("bookings") List<Booking> bookings, @Param("id") Long availabilityId);
}
