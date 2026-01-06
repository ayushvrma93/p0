package com.p0.calendarly.repository;

import com.p0.calendarly.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("password123");
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        Optional<User> result = userRepository.findByEmail("test@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getName()).isEqualTo("Test User");
    }

    @Test
    void findByEmail_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByEmail_WhenEmailIsNull_ShouldReturnEmpty() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        Optional<User> result = userRepository.findByEmail(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByEmail_WhenEmailIsEmpty_ShouldReturnEmpty() {
        // Given
        entityManager.persistAndFlush(testUser);

        // When
        Optional<User> result = userRepository.findByEmail("");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByEmail_WhenMultipleUsersExist_ShouldReturnCorrectUser() {
        // Given
        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setName("Another User");
        anotherUser.setPassword("password456");

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(anotherUser);

        // When
        Optional<User> result = userRepository.findByEmail("another@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("another@example.com");
        assertThat(result.get().getName()).isEqualTo("Another User");
    }
}
