package com.p0.calendarly.controller;

import com.p0.calendarly.enums.BookingStatus;
import com.p0.calendarly.exceptions.BookingNotFoundException;
import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.request.BookSlotRequest;
import com.p0.calendarly.model.Booking;
import com.p0.calendarly.model.User;
import com.p0.calendarly.service.BookingService;
import com.p0.calendarly.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @PostMapping("/book")
    public Booking bookSlot(@RequestBody BookSlotRequest request) throws CustomException {
        User user = userService.findById(request.getUserId());
        return bookingService.bookSlot(request.getAvailabilityId(), user, request.getName(), request.getDescription(), request.getParticipants());
    }

    @DeleteMapping("/decline")
    public void cancel(@PathParam("booking_id") Long id) throws BookingNotFoundException {
        bookingService.updateStatus(id, BookingStatus.DECLINED);
    }

    @PutMapping("/accept")
    public void accept(@PathParam("booking_id") Long id) throws BookingNotFoundException {
        bookingService.updateStatus(id, BookingStatus.ACCEPTED);
    }
}

