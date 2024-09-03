package com.p0.calendarly.service;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.User;
import com.p0.calendarly.repository.AvailabilityRepository;
import com.p0.calendarly.service.schedulestrategy.ScheduleOverlapContext;
import com.p0.calendarly.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ScheduleOverlapContext overlapContext;

    public Availability create(User user, Timestamp startTime, Timestamp endTime) throws CustomException {
        if(startTime.before(Timestamp.from(Instant.now())) || endTime.before(Timestamp.from(Instant.now()))){
            throw new CustomException("Timestamp can not be in ths past");
        }

        Availability availability = new Availability.Builder()
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setUser(user).build();
        try {
            return availabilityRepository.save(availability);
        } catch (Exception e){
            throw new CustomException("Availability with user id: " + user.getId() + ", start time: " + startTime + " and end time" + endTime + " already exists");
        }
    }

    public List<Availability> getUserAvailability(Long userId, String startTime, String endTime) throws CustomException {
        Timestamp startTimestamp;
        Timestamp endTimestamp;

        startTimestamp = DateUtils.parse(startTime);
        endTimestamp = DateUtils.parse(endTime);
        return availabilityRepository.findByUserIdAndStartTimeBetween(userId, startTimestamp, endTimestamp);
    }

    public Optional<Availability> findById(Long id){
        return availabilityRepository.findById(id);
    }

    @Transactional
    public void update(Availability availability){
        availabilityRepository.save(availability);
    }

    public List<Availability> findOverlappingSlots(Long requestingUserId, Long requestedUserId, String startTimeString, String endTimeString) throws CustomException {
        Timestamp startTime = DateUtils.parse(startTimeString);
        Timestamp endTime = DateUtils.parse(endTimeString);

        List<Availability> requestingUserAvailabilities = availabilityRepository.findByUserIdAndStartTimeBetween(requestingUserId, startTime, endTime);
        List<Availability> requestedUserAvailabilities = availabilityRepository.findByUserIdAndStartTimeBetween(requestedUserId, startTime, endTime);

        if(requestingUserAvailabilities == null || requestingUserAvailabilities.size()  == 0 || requestedUserAvailabilities == null || requestedUserAvailabilities.size() == 0){
            throw new CustomException("No availabilities found for one of the users");
        }

        return overlapContext.findOverlappingSlots(requestingUserAvailabilities, requestedUserAvailabilities);
    }

}
