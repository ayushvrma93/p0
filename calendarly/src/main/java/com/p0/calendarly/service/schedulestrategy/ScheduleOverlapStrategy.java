package com.p0.calendarly.service.schedulestrategy;

import com.p0.calendarly.model.Availability;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ScheduleOverlapStrategy {
    List<Availability> findOverlappingSlots(List<Availability> requestedByUserAvailabilities
            , List<Availability> requestedForUserAvailabilities);
}
