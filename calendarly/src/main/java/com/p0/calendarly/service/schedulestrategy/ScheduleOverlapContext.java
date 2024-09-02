package com.p0.calendarly.service.schedulestrategy;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.Availability;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleOverlapContext {

    private ScheduleOverlapStrategy strategy;

    public ScheduleOverlapContext(ScheduleOverlapStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ScheduleOverlapStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Availability> findOverlappingSlots(List<Availability> requestedByUserAvailabilities, List<Availability> requestedForUserAvailabilities) throws CustomException {
        return strategy.findOverlappingSlots(requestedByUserAvailabilities, requestedForUserAvailabilities);
    }
}

