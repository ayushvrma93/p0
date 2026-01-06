package com.p0.calendarly.repository;

import com.p0.calendarly.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    private Booking testBooking1;
    private Booking testBooking2;
    private Booking testBooking3;

    @BeforeEach
    void setUp() {
        testBooking1 = new Booking();
        testBooking1.setAvailabilityId(1L);
        testBooking1.setBookingTime(LocalDateTime.now());
        testBooking1.setUserEmail("user1@example.com");

        testBooking2 = new Booking();
        testBooking2.setAvailabilityId(2L);
        testBooking2.setBookingTime(LocalDateTime.now().plusHours(1));
        testBooking2.setUserEmail("user2@example.com");

        testBooking3 = new Booking();
        testBooking3.setAvailabilityId(3L);
        testBooking3.setBookingTime(LocalDateTime.now().plusHours(2));
        testBooking3.setUserEmail("user3@example.com");

        entityManager.persistAndFlush(testBooking1);
        entityManager.persistAndFlush(testBooking2);
        entityManager.persistAndFlush(testBooking3);
    }

    @Test
    void findByAvailabilityIdIn_WithValidIds_ShouldReturnMatchingBookings() {
        // Given
        List<Long> availabilityIds = Arrays.asList(1L, 2L);

        // When
        List<Booking> result = bookingRepository.findByAvailabilityIdIn(availabilityIds);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Booking::getAvailabilityId)
                .containsExactlyInAnyOrder(1L, 2L);
        assertThat(result).extracting(Booking::getUserEmail)
                .containsExactlyInAnyOrder("user1@example.com", "user2@example.com");
    }

    @Test
    void findByAvailabilityIdIn_WithSingleId_ShouldReturnSingleBooking() {
        // Given
        List<Long> availabilityIds = Collections.singletonList(1L);

        // When
        List<Booking> result = bookingRepository.findByAvailabilityIdIn(availabilityIds);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAvailabilityId()).isEqualTo(1L);
        assertThat(result.get(0).getUserEmail()).isEqualTo("user1@example.com");
    }

    @Test
    void findByAvailabilityIdIn_WithNonExistentIds_ShouldReturnEmptyList() {
        // Given
        List<Long> availabilityIds = Arrays.asList(999L, 1000L);

        // When
        List<Booking> result = bookingRepository.findByAvailabilityIdIn(availabilityIds);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void save_WithValidBooking_ShouldPersistBooking() {
        // Given
        Booking newBooking = new Booking();
        newBooking.setAvailabilityId(4L);
        newBooking.setBookingTime(LocalDateTime.now().plusHours(3));
        newBooking.setUserEmail("newuser@example.com");

        // When
        Booking savedBooking = bookingRepository.save(newBooking);

        // Then
        assertNotNull(savedBooking);
        assertNotNull(savedBooking.getId());
        assertThat(savedBooking.getAvailabilityId()).isEqualTo(4L);
        assertThat(savedBooking.getUserEmail()).isEqualTo("newuser@example.com");

        // Verify it's actually persisted
        Booking foundBooking = entityManager.find(Booking.class, savedBooking.getId());
        assertThat(foundBooking).isNotNull();
        assertThat(foundBooking.getAvailabilityId()).isEqualTo(4L);
    }

    @Test
    void save_WithExistingBooking_ShouldUpdateBooking() {
        // Given
        testBooking1.setUserEmail("updated@example.com");

        // When
        Booking updatedBooking = bookingRepository.save(testBooking1);

        // Then
        assertThat(updatedBooking.getId()).isEqualTo(testBooking1.getId());
        assertThat(updatedBooking.getUserEmail()).isEqualTo("updated@example.com");
        assertThat(updatedBooking.getAvailabilityId()).isEqualTo(1L);

        // Verify the update is persisted
        entityManager.flush();
        entityManager.clear();
        Booking foundBooking = entityManager.find(Booking.class, testBooking1.getId());
        assertThat(foundBooking.getUserEmail()).isEqualTo("updated@example.com");
    }
}
