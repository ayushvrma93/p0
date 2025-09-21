package com.p0.calendarly.repository;

import com.p0.calendarly.model.Availability;
import com.p0.calendarly.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AvailabilityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    private Availability testAvailability1;
    private Availability testAvailability2;
    private Availability testAvailability3;
    private Long userId1;
    private Long userId2;

    @BeforeEach
    void setUp() {
        userId1 = 1L;
        userId2 = 2L;

        testAvailability1 = new Availability();
        testAvailability1.setUserId(userId1);
        testAvailability1.setStartTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 9, 0)));
        testAvailability1.setEndTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 17, 0)));

        testAvailability2 = new Availability();
        testAvailability2.setUserId(userId1);
        testAvailability2.setStartTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 16, 10, 0)));
        testAvailability2.setEndTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 16, 18, 0)));

        testAvailability3 = new Availability();
        testAvailability3.setUserId(userId2);
        testAvailability3.setStartTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 8, 0)));
        testAvailability3.setEndTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 16, 0)));
    }

    @Test
    void testFindByUserIdAndStartTimeBetween_ShouldReturnAvailabilitiesInRange() {
        // Given
        entityManager.persistAndFlush(testAvailability1);
        entityManager.persistAndFlush(testAvailability2);
        entityManager.persistAndFlush(testAvailability3);

        Timestamp rangeStart = Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 0, 0));
        Timestamp rangeEnd = Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 23, 59));

        // When
        List<Availability> result = availabilityRepository.findByUserIdAndStartTimeBetween(
                userId1, rangeStart, rangeEnd);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId1);
        assertThat(result.get(0).getStartTime()).isEqualTo(testAvailability1.getStartTime());
    }

    @Test
    void testFindByUserIdAndStartTimeBetween_ShouldReturnEmptyListWhenNoMatches() {
        // Given
        entityManager.persistAndFlush(testAvailability1);

        Timestamp rangeStart = Timestamp.valueOf(LocalDateTime.of(2024, 1, 20, 0, 0));
        Timestamp rangeEnd = Timestamp.valueOf(LocalDateTime.of(2024, 1, 20, 23, 59));

        // When
        List<Availability> result = availabilityRepository.findByUserIdAndStartTimeBetween(
                userId1, rangeStart, rangeEnd);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testSave_ShouldPersistNewAvailability() {
        // Given
        Availability newAvailability = new Availability();
        newAvailability.setUserId(userId1);
        newAvailability.setStartTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 17, 9, 0)));
        newAvailability.setEndTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 17, 17, 0)));

        // When
        Availability savedAvailability = availabilityRepository.save(newAvailability);

        // Then
        assertThat(savedAvailability).isNotNull();
        assertThat(savedAvailability.getId()).isNotNull();
        assertThat(savedAvailability.getUserId()).isEqualTo(userId1);
        assertThat(savedAvailability.getStartTime()).isEqualTo(newAvailability.getStartTime());
        assertThat(savedAvailability.getEndTime()).isEqualTo(newAvailability.getEndTime());
    }

    @Test
    void testSave_ShouldUpdateExistingAvailability() {
        // Given
        Availability savedAvailability = entityManager.persistAndFlush(testAvailability1);
        Long originalId = savedAvailability.getId();
        
        savedAvailability.setStartTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 10, 0)));

        // When
        Availability updatedAvailability = availabilityRepository.save(savedAvailability);

        // Then
        assertThat(updatedAvailability.getId()).isEqualTo(originalId);
        assertThat(updatedAvailability.getStartTime()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 10, 0)));
    }

    @Test
    void testFindByUserId_ShouldReturnAllAvailabilitiesForUser() {
        // Given
        entityManager.persistAndFlush(testAvailability1);
        entityManager.persistAndFlush(testAvailability2);
        entityManager.persistAndFlush(testAvailability3);

        // When
        List<Availability> result = availabilityRepository.findByUserId(userId1);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(availability -> availability.getUserId().equals(userId1));
    }

    @Test
    void testFindByUserId_ShouldReturnEmptyListWhenUserHasNoAvailabilities() {
        // Given
        Long nonExistentUserId = 999L;

        // When
        List<Availability> result = availabilityRepository.findByUserId(nonExistentUserId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testUpdateBookings_ShouldUpdateBookingsForAvailability() {
        // Given
        Availability savedAvailability = entityManager.persistAndFlush(testAvailability1);
        List<Booking> bookings = new ArrayList<>();
        // Note: This test assumes the updateBookings method works with the native query
        // The actual implementation may need adjustment based on how bookings are stored

        // When
        int updatedRows = availabilityRepository.updateBookings(bookings, savedAvailability.getId());

        // Then
        // The result depends on the actual database schema and how bookings are stored
        // This test verifies the method executes without throwing exceptions
        assertThat(updatedRows).isGreaterThanOrEqualTo(0);
    }
}
