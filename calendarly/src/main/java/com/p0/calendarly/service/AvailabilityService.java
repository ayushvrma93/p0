package com.p0.calendarly.service;

import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.User;
import com.p0.calendarly.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Availability createAvailability(User user, Timestamp startTime, Timestamp endTime) {
        Availability availability = new Availability.Builder()
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setUser(user).build();
        return availabilityRepository.save(availability);
    }

    public List<Availability> getUserAvailability(Long userId, Timestamp start, Timestamp end) {
        return availabilityRepository.findByUserIdAndStartTimeBetween(userId, start, end);
    }

    public Optional<Availability> findById(Long id){
        return availabilityRepository.findById(id);
    }
}
