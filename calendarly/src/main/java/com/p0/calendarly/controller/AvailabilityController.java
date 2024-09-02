package com.p0.calendarly.controller;

import com.p0.calendarly.exceptions.CustomException;
import com.p0.calendarly.model.User;
import com.p0.calendarly.model.request.AvailabilityRequest;
import com.p0.calendarly.service.AvailabilityService;
import com.p0.calendarly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private UserService userService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createAvailability(@RequestBody AvailabilityRequest request,
                                             @PathVariable Long userId) throws CustomException {
        try{
            User user = userService.findById(userId);
            return ResponseEntity.ok(availabilityService.createAvailability(user, request.getStartTime(), request.getEndTime()));
        } catch (CustomException c){
            return ResponseEntity.badRequest().body(c.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserAvailability(@PathVariable Long userId,
                                                  @RequestParam("startTime") String startTime
                                                , @RequestParam("endTime") String endTime) throws CustomException {
        try{
            return ResponseEntity.ok(availabilityService.getUserAvailability(userId, startTime, endTime));
        } catch (CustomException c){
            return ResponseEntity.badRequest().body(c.getMessage());
        }
    }

    @GetMapping("/overlap")
    public ResponseEntity<?> getOverlappingAvailability(@RequestParam Long requestingUserId,
                                                          @RequestParam Long requestedUserId){
        try{
            return ResponseEntity.ok(availabilityService.findOverlappingSlots(requestingUserId, requestedUserId));
        } catch (CustomException c){
            return ResponseEntity.badRequest().body(c.getMessage());
        }
    }
}

