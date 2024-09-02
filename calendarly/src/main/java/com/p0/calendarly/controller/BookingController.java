package com.p0.calendarly.controller;

import com.p0.calendarly.enums.BookingStatus;
import com.p0.calendarly.exceptions.BookingNotFoundException;
import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.User;
import com.p0.calendarly.model.request.BookSlotRequest;
import com.p0.calendarly.service.BookingService;
import com.p0.calendarly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@ControllerAdvice
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody BookSlotRequest request) throws CustomException {
        try{
            return ResponseEntity.ok(bookingService.create(request));
        } catch (CustomException c){
            return ResponseEntity.badRequest().body(c.getMessage());
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> getSlot(@PathVariable("id") Long id) {
        try{
            return ResponseEntity.ok(bookingService.get(id));
        } catch (BookingNotFoundException b){
            return ResponseEntity.badRequest().body(b.getMessage());
        }

    }

    @DeleteMapping("{id}/decline")
    public ResponseEntity<?> cancel(@PathVariable("id") Long id) throws BookingNotFoundException {
        try{
            bookingService.updateStatus(id, BookingStatus.DECLINED);
            return ResponseEntity.ok().build();
        } catch (BookingNotFoundException b){
            return ResponseEntity.badRequest().body(b.getMessage());
        }
    }

    @PutMapping("{id}/accept")
    public ResponseEntity<?> accept(@PathVariable("id") Long id) throws BookingNotFoundException {
        try{
            bookingService.updateStatus(id, BookingStatus.ACCEPTED);
            return ResponseEntity.ok().build();
        } catch (BookingNotFoundException b){
            return ResponseEntity.badRequest().body(b.getMessage());
        }
    }
}

