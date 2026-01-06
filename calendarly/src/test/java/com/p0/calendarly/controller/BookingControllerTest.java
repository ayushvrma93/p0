package com.p0.calendarly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.p0.calendarly.enums.BookingStatus;
import com.p0.calendarly.exceptions.BookingNotFoundException;
import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.request.BookSlotRequest;
import com.p0.calendarly.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void create_ShouldReturnOk_WhenBookingCreatedSuccessfully() throws Exception {
        // Given
        BookSlotRequest request = new BookSlotRequest();
        String expectedResponse = "Booking created successfully";
        when(bookingService.create(any(BookSlotRequest.class))).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + expectedResponse + "\""));

        verify(bookingService, times(1)).create(any(BookSlotRequest.class));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenCustomExceptionThrown() throws Exception {
        // Given
        BookSlotRequest request = new BookSlotRequest();
        String errorMessage = "Invalid booking request";
        when(bookingService.create(any(BookSlotRequest.class))).thenThrow(new CustomException(errorMessage));

        // When & Then
        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(bookingService, times(1)).create(any(BookSlotRequest.class));
    }

    @Test
    void getSlot_ShouldReturnOk_WhenBookingFound() throws Exception {
        // Given
        Long bookingId = 1L;
        String expectedResponse = "Booking details";
        when(bookingService.get(bookingId)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/booking/{id}", bookingId))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + expectedResponse + "\""));

        verify(bookingService, times(1)).get(bookingId);
    }

    @Test
    void getSlot_ShouldReturnBadRequest_WhenBookingNotFound() throws Exception {
        // Given
        Long bookingId = 1L;
        String errorMessage = "Booking not found";
        when(bookingService.get(bookingId)).thenThrow(new BookingNotFoundException(errorMessage));

        // When & Then
        mockMvc.perform(get("/booking/{id}", bookingId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(bookingService, times(1)).get(bookingId);
    }

    @Test
    void decline_ShouldReturnOk_WhenBookingDeclinedSuccessfully() throws Exception {
        // Given
        Long bookingId = 1L;
        doNothing().when(bookingService).updateStatus(bookingId, BookingStatus.DECLINED);

        // When & Then
        mockMvc.perform(delete("/booking/{id}/decline", bookingId))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).updateStatus(bookingId, BookingStatus.DECLINED);
    }

    @Test
    void decline_ShouldReturnBadRequest_WhenBookingNotFound() throws Exception {
        // Given
        Long bookingId = 1L;
        String errorMessage = "Booking not found";
        doThrow(new BookingNotFoundException(errorMessage))
                .when(bookingService).updateStatus(bookingId, BookingStatus.DECLINED);

        // When & Then
        mockMvc.perform(delete("/booking/{id}/decline", bookingId))
                .andExpected(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(bookingService, times(1)).updateStatus(bookingId, BookingStatus.DECLINED);
    }

    @Test
    void accept_ShouldReturnOk_WhenBookingAcceptedSuccessfully() throws Exception {
        // Given
        Long bookingId = 1L;
        doNothing().when(bookingService).updateStatus(bookingId, BookingStatus.ACCEPTED);

        // When & Then
        mockMvc.perform(put("/booking/{id}/accept", bookingId))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).updateStatus(bookingId, BookingStatus.ACCEPTED);
    }

    @Test
    void accept_ShouldReturnBadRequest_WhenBookingNotFound() throws Exception {
        // Given
        Long bookingId = 1L;
        String errorMessage = "Booking not found";
        doThrow(new BookingNotFoundException(errorMessage))
                .when(bookingService).updateStatus(bookingId, BookingStatus.ACCEPTED);

        // When & Then
        mockMvc.perform(put("/booking/{id}/accept", bookingId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(bookingService, times(1)).updateStatus(bookingId, BookingStatus.ACCEPTED);
    }

    @Test
    void create_ShouldHandleNullRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSlot_ShouldHandleInvalidId() throws Exception {
        // When & Then
        mockMvc.perform(get("/booking/invalid"))
                .andExpect(status().isBadRequest());
    }
}
