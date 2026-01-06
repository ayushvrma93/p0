package com.p0.calendarly.controller;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.User;
import com.p0.calendarly.model.request.AvailabilityRequest;
import com.p0.calendarly.service.AvailabilityService;
import com.p0.calendarly.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityControllerTest {

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AvailabilityController availabilityController;

    private User testUser;
    private AvailabilityRequest availabilityRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");

        availabilityRequest = new AvailabilityRequest();
        availabilityRequest.setStartTime("2024-01-01T09:00:00");
        availabilityRequest.setEndTime("2024-01-01T17:00:00");
    }

    @Test
    void create_ShouldReturnOk_WhenValidRequest() throws CustomException {
        // Arrange
        Long userId = 1L;
        String expectedResponse = "Availability created successfully";
        
        when(userService.findById(userId)).thenReturn(testUser);
        when(availabilityService.create(eq(testUser), eq(availabilityRequest.getStartTime()), 
                eq(availabilityRequest.getEndTime()))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> response = availabilityController.create(availabilityRequest, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userService).findById(userId);
        verify(availabilityService).create(testUser, availabilityRequest.getStartTime(), 
                availabilityRequest.getEndTime());
    }

    @Test
    void create_ShouldReturnBadRequest_WhenUserServiceThrowsException() throws CustomException {
        // Arrange
        Long userId = 1L;
        String errorMessage = "User not found";
        
        when(userService.findById(userId)).thenThrow(new CustomException(errorMessage));

        // Act
        ResponseEntity<?> response = availabilityController.create(availabilityRequest, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(userService).findById(userId);
        verify(availabilityService, never()).create(any(), any(), any());
    }

    @Test
    void create_ShouldReturnBadRequest_WhenAvailabilityServiceThrowsException() throws CustomException {
        // Arrange
        Long userId = 1L;
        String errorMessage = "Invalid availability time";
        
        when(userService.findById(userId)).thenReturn(testUser);
        when(availabilityService.create(eq(testUser), eq(availabilityRequest.getStartTime()), 
                eq(availabilityRequest.getEndTime()))).thenThrow(new CustomException(errorMessage));

        // Act
        ResponseEntity<?> response = availabilityController.create(availabilityRequest, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(userService).findById(userId);
        verify(availabilityService).create(testUser, availabilityRequest.getStartTime(), 
                availabilityRequest.getEndTime());
    }

    @Test
    void getUserAvailability_ShouldReturnOk_WhenValidRequest() throws CustomException {
        // Arrange
        Long userId = 1L;
        String startTime = "2024-01-01T09:00:00";
        String endTime = "2024-01-01T17:00:00";
        List<String> expectedAvailabilities = Arrays.asList("09:00-12:00", "14:00-17:00");
        
        when(availabilityService.getUserAvailability(userId, startTime, endTime))
                .thenReturn(expectedAvailabilities);

        // Act
        ResponseEntity<?> response = availabilityController.getUserAvailability(userId, startTime, endTime);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAvailabilities, response.getBody());
        verify(availabilityService).getUserAvailability(userId, startTime, endTime);
    }

    @Test
    void getUserAvailability_ShouldReturnBadRequest_WhenServiceThrowsException() throws CustomException {
        // Arrange
        Long userId = 1L;
        String startTime = "2024-01-01T09:00:00";
        String endTime = "2024-01-01T17:00:00";
        String errorMessage = "Invalid time range";
        
        when(availabilityService.getUserAvailability(userId, startTime, endTime))
                .thenThrow(new CustomException(errorMessage));

        // Act
        ResponseEntity<?> response = availabilityController.getUserAvailability(userId, startTime, endTime);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(availabilityService).getUserAvailability(userId, startTime, endTime);
    }

    @Test
    void getOverlappingAvailabilities_ShouldReturnOk_WhenValidRequest() throws CustomException {
        // Arrange
        Long requestingUserId = 1L;
        Long requestedUserId = 2L;
        String startTime = "2024-01-01T09:00:00";
        String endTime = "2024-01-01T17:00:00";
        List<String> expectedOverlaps = Arrays.asList("10:00-12:00", "15:00-16:00");
        
        when(availabilityService.findOverlappingSlots(requestingUserId, requestedUserId, startTime, endTime))
                .thenReturn(expectedOverlaps);

        // Act
        ResponseEntity<?> response = availabilityController.getOverlappingAvailabilities(
                requestingUserId, requestedUserId, startTime, endTime);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOverlaps, response.getBody());
        verify(availabilityService).findOverlappingSlots(requestingUserId, requestedUserId, startTime, endTime);
    }

    @Test
    void getOverlappingAvailabilities_ShouldReturnBadRequest_WhenServiceThrowsException() throws CustomException {
        // Arrange
        Long requestingUserId = 1L;
        Long requestedUserId = 2L;
        String startTime = "2024-01-01T09:00:00";
        String endTime = "2024-01-01T17:00:00";
        String errorMessage = "Users not found";
        
        when(availabilityService.findOverlappingSlots(requestingUserId, requestedUserId, startTime, endTime))
                .thenThrow(new CustomException(errorMessage));

        // Act
        ResponseEntity<?> response = availabilityController.getOverlappingAvailabilities(
                requestingUserId, requestedUserId, startTime, endTime);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(availabilityService).findOverlappingSlots(requestingUserId, requestedUserId, startTime, endTime);
    }
}
