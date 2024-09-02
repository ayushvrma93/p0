package com.p0.calendarly.service;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.User;
import com.p0.calendarly.repository.AvailabilityRepository;
import com.p0.calendarly.service.schedulestrategy.ScheduleOverlapContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ScheduleOverlapContext overlapContext;

    public Availability createAvailability(User user, Timestamp startTime, Timestamp endTime) throws CustomException {
        if(startTime.before(Timestamp.from(Instant.now())) || endTime.before(Timestamp.from(Instant.now()))){
            throw new CustomException("Timestamp can not be in ths past");
        }

        Availability availability = new Availability.Builder()
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setUser(user).build();
        return availabilityRepository.save(availability);
    }

    public List<Availability> getUserAvailability(Long userId, String startTime, String endTime) throws CustomException {
        Timestamp startTimestamp;
        Timestamp endTimestamp;

        try{
            startTimestamp = Timestamp.from(Instant.parse(startTime));
            endTimestamp = Timestamp.from(Instant.parse(endTime));

        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date format", e);
        }
        return availabilityRepository.findByUserIdAndStartTimeBetween(userId, startTimestamp, endTimestamp);
    }

    public Optional<Availability> findById(Long id){
        return availabilityRepository.findById(id);
    }

    @Transactional
    public void update(Availability availability){
        availabilityRepository.save(availability);
    }

    public List<Availability> findOverlappingSlots(Long userId1, Long userId2) throws CustomException {
        List<Availability> requestingUserAvailabilities = availabilityRepository.findByUserId(userId1);
        List<Availability> requestedUserAvailabilities = availabilityRepository.findByUserId(userId2);

        if(requestingUserAvailabilities == null || requestingUserAvailabilities.size()  == 0 || requestedUserAvailabilities == null || requestedUserAvailabilities.size() == 0){
            throw new CustomException("No availabilities found for one of the users");
        }

        return overlapContext.findOverlappingSlots(requestingUserAvailabilities, requestedUserAvailabilities);
    }

}
