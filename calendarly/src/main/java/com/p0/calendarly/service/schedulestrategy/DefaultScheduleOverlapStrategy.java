package com.p0.calendarly.service.schedulestrategy;

import com.p0.calendarly.model.Availability;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class DefaultScheduleOverlapStrategy implements ScheduleOverlapStrategy{
    @Override
    public List<Availability> findOverlappingSlots(List<Availability> requestedByUserAvailabilities, List<Availability> requestedForUserAvailabilities) {
        List<Availability> overlappingSlots = new ArrayList<>();

        // Sort both lists by start time
        Collections.sort(requestedByUserAvailabilities, Comparator.comparing(Availability::getStartTime));
        Collections.sort(requestedForUserAvailabilities, Comparator.comparing(Availability::getStartTime));

        int i = 0, j = 0;
        while (i < requestedByUserAvailabilities.size() && j < requestedForUserAvailabilities.size()) {
            Availability avail1 = requestedByUserAvailabilities.get(i);
            Availability avail2 = requestedForUserAvailabilities.get(j);

            // Check if availabilities overlap
            if (avail1.getEndTime().after(avail2.getStartTime()) && avail1.getStartTime().before(avail2.getEndTime())) {
                // Find the overlapping period
                Timestamp overlapStart = max(avail1.getStartTime(), avail2.getStartTime());
                Timestamp overlapEnd = min(avail1.getEndTime(), avail2.getEndTime());

                // Add the overlapping slot to the result list
                Availability overlap = new Availability();
                overlap.setStartTime(overlapStart);
                overlap.setEndTime(overlapEnd);

                overlappingSlots.add(overlap);
            }

            if (avail1.getEndTime().before(avail2.getEndTime())) {
                i++;
            } else {
                j++;
            }
        }
        return overlappingSlots;
    }

    private Timestamp max(Timestamp time1, Timestamp time2) {
        return time1.after(time2) ? time1 : time2;
    }

    private Timestamp min(Timestamp time1, Timestamp time2) {
        return time1.before(time2) ? time1 : time2;
    }
}
