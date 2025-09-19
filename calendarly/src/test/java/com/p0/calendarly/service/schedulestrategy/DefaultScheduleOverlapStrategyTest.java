package com.p0.calendarly.service.schedulestrategy;

import com.p0.calendarly.model.Availability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DefaultScheduleOverlapStrategyTest {

    private DefaultScheduleOverlapStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new DefaultScheduleOverlapStrategy();
    }

    @Test
    void testFindOverlappingSlots_WithOverlappingAvailabilities() {
        // Arrange
        List<Availability> user1Availabilities = new ArrayList<>();
        List<Availability> user2Availabilities = new ArrayList<>();

        Availability avail1 = new Availability();
        avail1.setStartTime(Timestamp.valueOf("2023-12-01 09:00:00"));
        avail1.setEndTime(Timestamp.valueOf("2023-12-01 12:00:00"));
        user1Availabilities.add(avail1);

        Availability avail2 = new Availability();
        avail2.setStartTime(Timestamp.valueOf("2023-12-01 10:00:00"));
        avail2.setEndTime(Timestamp.valueOf("2023-12-01 14:00:00"));
        user2Availabilities.add(avail2);

        // Act
        List<Availability> result = strategy.findOverlappingSlots(user1Availabilities, user2Availabilities);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Timestamp.valueOf("2023-12-01 10:00:00"), result.get(0).getStartTime());
        assertEquals(Timestamp.valueOf("2023-12-01 12:00:00"), result.get(0).getEndTime());
    }

    @Test
    void testFindOverlappingSlots_WithNoOverlap() {
        // Arrange
        List<Availability> user1Availabilities = new ArrayList<>();
        List<Availability> user2Availabilities = new ArrayList<>();

        Availability avail1 = new Availability();
        avail1.setStartTime(Timestamp.valueOf("2023-12-01 09:00:00"));
        avail1.setEndTime(Timestamp.valueOf("2023-12-01 10:00:00"));
        user1Availabilities.add(avail1);

        Availability avail2 = new Availability();
        avail2.setStartTime(Timestamp.valueOf("2023-12-01 11:00:00"));
        avail2.setEndTime(Timestamp.valueOf("2023-12-01 12:00:00"));
        user2Availabilities.add(avail2);

        // Act
        List<Availability> result = strategy.findOverlappingSlots(user1Availabilities, user2Availabilities);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindOverlappingSlots_WithEmptyLists() {
        // Arrange
        List<Availability> user1Availabilities = new ArrayList<>();
        List<Availability> user2Availabilities = new ArrayList<>();

        // Act
        List<Availability> result = strategy.findOverlappingSlots(user1Availabilities, user2Availabilities);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindOverlappingSlots_WithOneEmptyList() {
        // Arrange
        List<Availability> user1Availabilities = new ArrayList<>();
        List<Availability> user2Availabilities = new ArrayList<>();

        Availability avail1 = new Availability();
        avail1.setStartTime(Timestamp.valueOf("2023-12-01 09:00:00"));
        avail1.setEndTime(Timestamp.valueOf("2023-12-01 10:00:00"));
        user1Availabilities.add(avail1);

        // Act
        List<Availability> result = strategy.findOverlappingSlots(user1Availabilities, user2Availabilities);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindOverlappingSlots_WithMultipleOverlaps() {
        // Arrange
        List<Availability> user1Availabilities = Arrays.asList(
            createAvailability("2023-12-01 09:00:00", "2023-12-01 11:00:00"),
            createAvailability("2023-12-01 14:00:00", "2023-12-01 16:00:00")
        );

        List<Availability> user2Availabilities = Arrays.asList(
            createAvailability("2023-12-01 10:00:00", "2023-12-01 12:00:00"),
            createAvailability("2023-12-01 15:00:00", "2023-12-01 17:00:00")
        );

        // Act
        List<Availability> result = strategy.findOverlappingSlots(user1Availabilities, user2Availabilities);

        // Assert
        assertEquals(2, result.size());
        assertEquals(Timestamp.valueOf("2023-12-01 10:00:00"), result.get(0).getStartTime());
        assertEquals(Timestamp.valueOf("2023-12-01 11:00:00"), result.get(0).getEndTime());
        assertEquals(Timestamp.valueOf("2023-12-01 15:00:00"), result.get(1).getStartTime());
        assertEquals(Timestamp.valueOf("2023-12-01 16:00:00"), result.get(1).getEndTime());
    }

    @Test
    void testFindOverlappingSlots_WithExactSameTime() {
        // Arrange
        List<Availability> user1Availabilities = Arrays.asList(
            createAvailability("2023-12-01 09:00:00", "2023-12-01 11:00:00")
        );

        List<Availability> user2Availabilities = Arrays.asList(
            createAvailability("2023-12-01 09:00:00", "2023-12-01 11:00:00")
        );

        // Act
        List<Availability> result = strategy.findOverlappingSlots(user1Availabilities, user2Availabilities);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Timestamp.valueOf("2023-12-01 09:00:00"), result.get(0).getStartTime());
        assertEquals(Timestamp.valueOf("2023-12-01 11:00:00"), result.get(0).getEndTime());
    }

    @Test
    void testFindOverlappingSlots_WithAdjacentTimes() {
        // Arrange
        List<Availability> user1Availabilities = Arrays.asList(
            createAvailability("2023-12-01 09:00:00", "2023-12-01 10:00:00")
        );

        List<Availability> user2Availabilities = Arrays.asList(
            createAvailability("2023-12-01 10:00:00", "2023-12-01 11:00:00")
        );

        // Act
        List<Availability> result = strategy.findOverlappingSlots(user1Availabilities, user2Availabilities);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testMax_WithFirstTimestampLater() {
        // Arrange
        Timestamp time1 = Timestamp.valueOf("2023-12-01 10:00:00");
        Timestamp time2 = Timestamp.valueOf("2023-12-01 09:00:00");

        // Act
        Timestamp result = strategy.max(time1, time2);

        // Assert
        assertEquals(time1, result);
    }

    @Test
    void testMax_WithSecondTimestampLater() {
        // Arrange
        Timestamp time1 = Timestamp.valueOf("2023-12-01 09:00:00");
        Timestamp time2 = Timestamp.valueOf("2023-12-01 10:00:00");

        // Act
        Timestamp result = strategy.max(time1, time2);

        // Assert
        assertEquals(time2, result);
    }

    @Test
    void testMax_WithEqualTimestamps() {
        // Arrange
        Timestamp time1 = Timestamp.valueOf("2023-12-01 10:00:00");
        Timestamp time2 = Timestamp.valueOf("2023-12-01 10:00:00");

        // Act
        Timestamp result = strategy.max(time1, time2);

        // Assert
        assertEquals(time1, result);
    }

    @Test
    void testMin_WithFirstTimestampEarlier() {
        // Arrange
        Timestamp time1 = Timestamp.valueOf("2023-12-01 09:00:00");
        Timestamp time2 = Timestamp.valueOf("2023-12-01 10:00:00");

        // Act
        Timestamp result = strategy.min(time1, time2);

        // Assert
        assertEquals(time1, result);
    }

    @Test
    void testMin_WithSecondTimestampEarlier() {
        // Arrange
        Timestamp time1 = Timestamp.valueOf("2023-12-01 10:00:00");
        Timestamp time2 = Timestamp.valueOf("2023-12-01 09:00:00");

        // Act
        Timestamp result = strategy.min(time1, time2);

        // Assert
        assertEquals(time2, result);
    }

    @Test
    void testMin_WithEqualTimestamps() {
        // Arrange
        Timestamp time1 = Timestamp.valueOf("2023-12-01 10:00:00");
        Timestamp time2 = Timestamp.valueOf("2023-12-01 10:00:00");

        // Act
        Timestamp result = strategy.min(time1, time2);

        // Assert
        assertEquals(time1, result);
    }

    private Availability createAvailability(String startTime, String endTime) {
        Availability availability = new Availability();
        availability.setStartTime(Timestamp.valueOf(startTime));
        availability.setEndTime(Timestamp.valueOf(endTime));
        return availability;
    }
}
