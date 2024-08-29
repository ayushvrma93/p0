package com.p0.calendarly.repository;

import com.p0.calendarly.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByUserIdAndStartTimeBetween(Long userId, Timestamp startTime, Timestamp endTime);
}
